package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ManageMovieController {

    @FXML
    private ListView<String> movieListView;

    @FXML
    private ImageView posterImageView;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionArea, movieDetailsArea;

    @FXML
    private DatePicker startDatePicker, endDatePicker;

    @FXML
    private ComboBox<String> timesField1; 
    @FXML
    private ComboBox<String> timesField2; 
    @FXML
    private ComboBox<Double> ticketprice; 

    private ObservableList<String> movieTitles = FXCollections.observableArrayList();
    private String selectedImagePath = null;

    @FXML
    public void initialize() {
        loadMovies();
        movieListView.setItems(movieTitles);

        movieListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showPosterAndDetails(newVal);
            }
        });

        // ตัวอย่างการเพิ่มเวลาให้ ComboBox (ถ้าอยากใส่ตัวเลือกเริ่มต้น)
        timesField1.getItems().addAll("10:00", "12:00", "14:00", "16:00", "18:00", "20:00");
        timesField2.getItems().addAll("10:00", "12:00", "14:00", "16:00", "18:00", "20:00");
        ticketprice.getItems().addAll(80.00,85.00,90.00,95.00,100.00,105.00,110.00,115.00,120.00,125.00,130.00);
    }

    private void loadMovies() {
        JSONArray movies = JSONManager.readMovies();
        for (int i = 0; i < movies.length(); i++) {
            movieTitles.add(movies.getJSONObject(i).getString("title"));
        }
    }

    private void showPosterAndDetails(String title) {
        JSONArray movies = JSONManager.readMovies();
        for (int i = 0; i < movies.length(); i++) {
            JSONObject movie = movies.getJSONObject(i);
            if (movie.getString("title").equals(title)) {
                File file = new File(movie.getString("posterPath"));
                posterImageView.setImage(file.exists() ? new Image(file.toURI().toString()) : null);

                movieDetailsArea.setText(
                    "Title: " + movie.getString("title") + "\n" +
                    "Description: " + movie.getString("description") + "\n" +
                    "Start Date: " + movie.getString("startDate") + "\n" +
                    "End Date: " + movie.getString("endDate") + "\n" +
                    "TicketPrice:" + movie.getDouble("ticketprice")+"\n"+
                    "Times: " + movie.getJSONArray("times").join(", ")

                );
                break;
            }
        }
    }

    @FXML
    public void uploadbutton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File file = fileChooser.showOpenDialog(((Button) event.getSource()).getScene().getWindow());

        if (file != null) {
            selectedImagePath = file.getAbsolutePath();
            posterImageView.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    public void addMovie() {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String startDate = startDatePicker.getValue() != null ? startDatePicker.getValue().toString() : "";
        String endDate = endDatePicker.getValue() != null ? endDatePicker.getValue().toString() : "";
        String times1 = timesField1.getValue();
        String times2 = timesField2.getValue();// รับจาก ComboBox
        double Ticketprice = ticketprice.getValue();

        if (selectedImagePath == null) {
            Main.showAlert(Alert.AlertType.ERROR, "Error", "Please upload a poster!");
            return;
        }

        if (title.isEmpty() || description.isEmpty() || times1 == null || times2 == null) {
            Main.showAlert(Alert.AlertType.ERROR, "Error", "All fields are required!");
            return;
        }

        Movie movie = new Movie(title, description, startDate, endDate, selectedImagePath, new String[]{times1,times2},Ticketprice);
        JSONManager.addMovie(movie);

        movieTitles.clear();
        loadMovies();
    }

    @FXML
    public void removeMovie() {
        String selectedMovie = movieListView.getSelectionModel().getSelectedItem();
        if (selectedMovie != null) {
            JSONManager.removeMovie(selectedMovie);
            movieTitles.remove(selectedMovie);
            posterImageView.setImage(null);
            movieDetailsArea.clear();
        } else {
            Main.showAlert(Alert.AlertType.WARNING, "Warning", "Please select a movie to remove.");
        }
    }

    @FXML
    public void Backbotton(ActionEvent event) throws IOException {
    	Main.switchScene(event, "EmployeeDashboard.fxml");
    }
}
