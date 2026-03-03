package application;


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
import javafx.stage.Stage;

	
public class CustomerMenuController {
	
	
	public void ContectUS(ActionEvent event) throws IOException {
		Main.switchScene(event, "CustomerContectUS.fxml");
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
	

	public void BookingHistory(ActionEvent event) throws IOException {
		Main.switchScene(event, "CustomerBookingHistory.fxml");
	}

	public void Home(ActionEvent event) throws IOException {
		Main.switchScene(event, "CustomerHome.fxml");
	}
	

    public void logoutbotton(ActionEvent event) throws IOException {
    	Main.switchScene(event, "loginscene.fxml");
    }
	
    
	
	
}
