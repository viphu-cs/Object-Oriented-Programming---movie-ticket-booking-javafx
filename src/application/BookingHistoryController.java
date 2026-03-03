package application;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class BookingHistoryController {

    @FXML
    private TableView<Booking> bookedTable;
    @FXML
    private TableColumn<Booking, String> movieColumn;
    @FXML
    private TableColumn<Booking, String> dateColumn;
    @FXML
    private TableColumn<Booking, String> timeColumn;
    @FXML
    private TableColumn<Booking, String> seatColumn;
    @FXML
    private TableColumn<Booking, String> statusColumn;
    @FXML
    private TableColumn<Booking, String> ticketpriceColumn;
    @FXML
    Label totalprice;


    private ObservableList<Booking> bookingList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        movieColumn.setCellValueFactory(new PropertyValueFactory<>("movie"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        seatColumn.setCellValueFactory(new PropertyValueFactory<>("seat"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        ticketpriceColumn.setCellValueFactory(new PropertyValueFactory<>("ticketprice"));

        loadUserBookings();
        totalprice();
    }

    private void loadUserBookings() {
        String username = loginController.getLoggedInUsername();
        if (username == null) return;

        Set<Booking> allBookings = JSONManager.loadBookedSeats();
        List<Booking> userBookings = allBookings.stream()
                .filter(booking -> booking.getUsername().equals(username))
                .collect(Collectors.toList());

        bookingList.setAll(userBookings);
        bookedTable.setItems(bookingList);
    }

    @FXML
    public void cancelBooking(ActionEvent event) {
        Booking booking = bookedTable.getSelectionModel().getSelectedItem();
        if (booking == null) return; // Avoid NullPointerException if no booking is selected

        booking.setStatus("Cancelled");

        // Update data in JSON file
        Set<Booking> allBookings = JSONManager.loadBookedSeats();
        for (Booking b : allBookings) {
            if (b.getID().equals(booking.getID())) {
                b.setStatus("Cancelled");
                break;
            }
        }

        JSONManager.saveBookedSeats(allBookings);
        bookedTable.refresh();
        totalprice();
    }
    
    public void Movie(ActionEvent event) throws IOException {
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


    public void ContectUS(ActionEvent event) throws IOException {
        Main.switchScene(event, "CustomerContectUS.fxml");
    }

    public void BookingHistory(ActionEvent event) throws IOException {
        Main.switchScene(event, "CustomerBookingHistory.fxml");
    }

    public void Home(ActionEvent event) throws IOException {
        Main.switchScene(event, "CustomerHome.fxml");
    }

    public void logoutbotton(ActionEvent event) throws IOException {
        Main.switchScene(event, "loginscene.fxml");
    }
    //เงินที่ต้องจ่าย
    public void totalprice() {
        // Calculate the total price of all bookings that have been confirmed or booked
        double total = bookingList.stream()
                .filter(booking -> booking.getStatus().equals("Booked"))  // Filter only booked tickets
                .mapToDouble(booking -> booking.getTicketprice())  // Extract the ticket price and sum it up
                .sum();

        // Update the total price label
        totalprice.setText(String.valueOf(total));
    }

}
