package application;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BookingMovieController {
    @FXML
    private Label A1, A2, A3, A4, A5, B1, B2, B3, B4, B5, C1, C2, C3, C4, C5, D1, D2, D3, D4, D5;
    @FXML
    private ImageView poster;
    @FXML
    private DatePicker date;
    @FXML
    private ComboBox<String> title, time;
    @FXML
    private Label ticketprice;

    private final ObservableList<String> movieTitles = FXCollections.observableArrayList();
    private final ObservableList<String> movieTimes = FXCollections.observableArrayList();
    
    private final Set<Label> selectedSeats = new HashSet<>();
    private final Set<Label> bookedSeats = new HashSet<>();
    private final Set<Booking> bookings = new HashSet<>();

    @FXML
    public void initialize() {
        title.setDisable(true);
        time.setDisable(true);

        date.setOnAction(e -> {
            resetSelections();
            if (date.getValue() != null) {
                loadMoviesByDate(date.getValue());
            }
        });

        title.setOnAction(e -> {
            resetTimeSelection();
            if (title.getValue() != null) {
                showMovieDetails(title.getValue());
                loadTimesByMovie(title.getValue());
            }
        });

        time.setOnAction(e -> {
            if (time.getValue() != null) {
                loadBookedSeats(title.getValue(), date.getValue().toString(), time.getValue());
            }
        });

        Label[] seats = {A1, A2, A3, A4, A5, B1, B2, B3, B4, B5, C1, C2, C3, C4, C5, D1, D2, D3, D4, D5};
        for (Label seat : seats) {
            seat.setOnMouseClicked(this::handleSeatClick);
        }
    }

    private void resetSelections() {
        title.getItems().clear();
        time.getItems().clear();
        movieTitles.clear();
        movieTimes.clear();
        title.setDisable(true);
        time.setDisable(true);
    }

    private void resetTimeSelection() {
        time.getItems().clear();
        movieTimes.clear();
        time.setDisable(true);
    }

    private void handleSeatClick(MouseEvent event) {
        Label seat = (Label) event.getSource();

        if (bookedSeats.contains(seat)) {
            return;
        }

        if (selectedSeats.contains(seat)) {
            seat.setTextFill(Color.BLACK);
            selectedSeats.remove(seat);
        } else {
            seat.setTextFill(Color.RED);
            selectedSeats.add(seat);
        }
    }

    @FXML
    public void handleBooking(ActionEvent event) {
        if (selectedSeats.isEmpty() || title.getValue() == null || time.getValue() == null || date.getValue() == null) {
            return;
        }

        String selectedMovie = title.getValue();
        String selectedTime = time.getValue();
        String selectedDate = date.getValue().toString();
        String username = loginController.getLoggedInUsername();
        String firstName = JSONManager.getUserByUsername(username).getString("firstname");
        String lastName = JSONManager.getUserByUsername(username).getString("lastname");
        
        // ✅ ตรวจสอบว่าตัวแปร ticketprice มีค่า
        double ticketPrice = 0.0;
        if (ticketprice != null && ticketprice.getText() != null && !ticketprice.getText().isEmpty()) {
            ticketPrice = Double.parseDouble(ticketprice.getText());
        }

        Set<Booking> allBookings = JSONManager.loadBookedSeats();

        for (Label seat : selectedSeats) {
            seat.setTextFill(Color.GRAY);
            bookedSeats.add(seat);
            String bookingID = UUID.randomUUID().toString();

            // ตรวจสอบว่ามีการจองเก่าอยู่แล้วหรือไม่
            Booking existingBooking = allBookings.stream()
                .filter(b -> b.getMovie().equals(selectedMovie) && 
                             b.getDate().equals(selectedDate) &&
                             b.getTime().equals(selectedTime) &&
                             b.getStatus().equals("Booked") &&
                             b.getSeat().equals(seat.getId()))
                .findFirst()
                .orElse(null);

            if (existingBooking != null) {
                existingBooking.setStatus("Booked");
            } else {
                // ✅ ใช้ constructor ใหม่ที่รองรับ 10 พารามิเตอร์
                allBookings.add(new Booking("Booked", username, firstName, lastName, 
                                            selectedMovie, selectedDate, selectedTime, 
                                            seat.getId(), bookingID, ticketPrice));
            }

            Main.showAlert(AlertType.CONFIRMATION, "Booked Successfully", "You have successfully booked a movie ticket.");
        }

        JSONManager.saveBookedSeats(allBookings);
        selectedSeats.clear();
    }



    private void loadMoviesByDate(LocalDate selectedDate) {
        JSONArray movies = JSONManager.readMovies();
        for (int i = 0; i < movies.length(); i++) {
            JSONObject movie = movies.getJSONObject(i);
            LocalDate startDate = LocalDate.parse(movie.getString("startDate"));
            LocalDate endDate = LocalDate.parse(movie.getString("endDate"));

            if (!selectedDate.isBefore(startDate) && !selectedDate.isAfter(endDate)) {
                movieTitles.add(movie.getString("title"));
            }
        }
        title.setItems(movieTitles);
        title.setDisable(false);
    }

    private void showMovieDetails(String selectedTitle) {
        JSONArray movies = JSONManager.readMovies();
        for (int i = 0; i < movies.length(); i++) {
            JSONObject movie = movies.getJSONObject(i);
            if (movie.getString("title").equals(selectedTitle)) {
                File file = new File(movie.getString("posterPath"));
                poster.setImage(file.exists() ? new Image(file.toURI().toString()) : null);
                ticketprice.setText(String.valueOf(movie.getDouble("ticketprice")));
                break;
            }
        }
    }

    private void loadTimesByMovie(String selectedMovie) {
        JSONArray movies = JSONManager.readMovies();
        movieTimes.clear();
        for (int i = 0; i < movies.length(); i++) {
            JSONObject movie = movies.getJSONObject(i);
            if (movie.getString("title").equals(selectedMovie)) {
                JSONArray timesArray = movie.getJSONArray("times");
                for (int j = 0; j < timesArray.length(); j++) {
                    movieTimes.add(timesArray.getString(j));
                }
                break;
            }
        }
        time.setItems(movieTimes);
        time.setDisable(false);
    }

    private void loadBookedSeats(String selectedMovie, String selectedDate, String selectedTime) {
        bookings.clear();
        bookings.addAll(JSONManager.loadBookedSeats());

        for (Label seat : new Label[]{A1, A2, A3, A4, A5, B1, B2, B3, B4, B5, C1, C2, C3, C4, C5, D1, D2, D3, D4, D5}) {
            seat.setTextFill(Color.BLACK);
            bookedSeats.remove(seat);
        }

        for (Booking booking : bookings) {
            if (booking.getMovie().equals(selectedMovie) &&
                booking.getDate().equals(selectedDate) &&
                booking.getTime().equals(selectedTime)) {

                for (Label seat : new Label[]{A1, A2, A3, A4, A5, B1, B2, B3, B4, B5, C1, C2, C3, C4, C5, D1, D2, D3, D4, D5}) {
                    if (seat.getId().equals(booking.getSeat())) {
                        if (booking.getStatus().equals("Cancelled")) {
                            seat.setTextFill(Color.BLACK); // คืนสถานะให้สามารถเลือกใหม่ได้
                            bookedSeats.remove(seat);
                        } else {
                            seat.setTextFill(Color.GRAY);
                            bookedSeats.add(seat);
                        }
                    }
                }
            }
        }
    }

    
    @FXML
    public void goBack(ActionEvent event) throws IOException {
    	String filePath = "movies.json";
		List<String> posterPaths = new ArrayList<>();
		List<String> titlemovie = new ArrayList<>();
		List<String> descriptions = new ArrayList<>();
		List<String> startDates = new ArrayList<>();
		List<String> endDates = new ArrayList<>();
		try {
            // อ่านไฟล์ JSON
            FileReader reader = new FileReader(filePath);
            StringBuilder sb = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            String jsonString = sb.toString();
            JSONArray moviesArray = new JSONArray(jsonString); 
            
            for (int i = 0; i < moviesArray.length(); i++) {
                JSONObject movie = moviesArray.getJSONObject(i);


                String posterPath = movie.getString("posterPath");
                String title = movie.getString("title");
                String description = movie.getString("description");
                String startDate = movie.getString("startDate");
                String endDate = movie.getString("endDate");
                posterPaths.add(posterPath);
                titlemovie.add(title);
                descriptions.add(description);
                startDates.add(startDate);
                endDates.add(endDate);
                
 
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerMovieShow.fxml"));
            Parent root = loader.load();


            CustomerMovieShowController controller = loader.getController();



            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show(); 
            controller.setPosterPaths(posterPaths,titlemovie,descriptions,startDates,endDates);

 
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
