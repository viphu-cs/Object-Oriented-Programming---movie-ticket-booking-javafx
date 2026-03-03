package application;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EmployeeMovieDetailController {

    @FXML
    private AnchorPane mainPane;
    
    @FXML
    private ImageView moviePoster;
    
    @FXML
    private Label movieTitle;
    
    @FXML
    private Label movieDescription;
    
    @FXML
    private Label movieStartDate;
    
    @FXML
    private Label movieEndDate;
    
    @FXML
    private Button backButton;

    private String posterPath;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    
    @FXML
	public void ManageMoviebotton(ActionEvent event) throws IOException {
		Main.switchScene(event, "ManageMoviescene.fxml");
	}
	@FXML
    public void logoutbotton(ActionEvent event) throws IOException {
    	Main.switchScene(event, "loginscene.fxml");
    }
	@FXML
    public void ManageBookingbotton(ActionEvent event) throws IOException {
    	Main.switchScene(event, "ManageBooking.fxml");
    }
	@FXML
    public void Dashbotton(ActionEvent event) throws IOException {
    	Main.switchScene(event, "EmployeeDashboard.fxml");
	}

    public void setMovieData(String posterPath, String title,String description,String startDate,String endDate) {
        this.posterPath = posterPath;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        
        displayMovieDetails();
    }
    
    private void displayMovieDetails() {

        movieTitle.setText(title);
        movieDescription.setText(description);
        movieStartDate.setText(startDate);
        movieEndDate.setText(endDate);
        

        try {
            String imageUrl = new File(posterPath).toURI().toString();
            Image image = new Image(imageUrl);
            moviePoster.setImage(image);
        } catch (Exception e) {
            System.err.println("ไม่สามารถโหลดรูปภาพได้: " + posterPath);
            e.printStackTrace();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EmployeeMovie.fxml"));
            Parent root = loader.load();


            EmployeeMovieShowController controller = loader.getController();



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