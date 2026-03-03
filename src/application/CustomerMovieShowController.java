package application;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerMovieShowController {
	@FXML
	private AnchorPane center1;
	@FXML
	private AnchorPane center2;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TitledPane titledPane;
	
    private String poster;
    private List<String> posterPaths;
    private List<String> titlemovie;
    private List<String> descriptions;
    private List<String> startDates;
    private List<String> endDates;

	

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
	
	String filePath = "movies.json";

		    

    public void setPosterPaths(List<String> posterPaths,List<String> titlemovie,List<String> descriptions,List<String> startDates,List<String> endDates) {
        this.posterPaths = posterPaths;
        this.titlemovie = titlemovie;
        this.descriptions = descriptions;
        this.startDates = startDates;
        this.endDates = endDates;
        displayMoviePosters();
    }
    
    public void displayMoviePosters() {
    	if (center2 == null || posterPaths == null) {
            return;
        }
        

        
        FlowPane movieFlow = new FlowPane();
        movieFlow.setPrefWidth(center2.getPrefWidth());
        movieFlow.setMinHeight(100);
        movieFlow.setPrefHeight(FlowPane.USE_COMPUTED_SIZE);
        movieFlow.setHgap(20);
        movieFlow.setVgap(20);
        movieFlow.setPadding(new javafx.geometry.Insets(20));
        
        for (String poster : posterPaths) {
            VBox movieBox = new VBox(5);
            movieBox.setAlignment(Pos.CENTER);
            
            try {
                String imageUrl = new File(poster).toURI().toString();
                ImageView pos = new ImageView(new Image(imageUrl));
                
                pos.setFitWidth(150);
                pos.setFitHeight(200);
                pos.setPreserveRatio(true);
                
                pos.setOnMouseClicked(this::handleMovieClick);
                
                pos.setUserData(poster);
                
                movieBox.getChildren().add(pos);
                movieFlow.getChildren().add(movieBox);
            } catch (Exception e) {
                System.err.println("ไม่สามารถโหลดรูปภาพได้: " + poster);
                e.printStackTrace();
            }
        }
        
        int numPosters = posterPaths.size();
        int postersPerRow = Math.max(1, (int)((center2.getPrefWidth() - 40) / 170));
        int numRows = (int) Math.ceil((double) numPosters / postersPerRow);
        double flowHeight = numRows * 240;
        
        movieFlow.setMinHeight(flowHeight);
        
        center2.getChildren().add(movieFlow);
        AnchorPane.setTopAnchor(movieFlow, 0.0);
        AnchorPane.setLeftAnchor(movieFlow, 0.0);
        AnchorPane.setRightAnchor(movieFlow, 0.0);
        AnchorPane.setBottomAnchor(movieFlow, 0.0);
        
        center2.setPrefHeight(Math.max(center2.getPrefHeight(), flowHeight + 40));
    }
    
    private void handleMovieClick(MouseEvent event) {
        try {
            ImageView clickedImage = (ImageView) event.getSource();
            String posterPath = (String) clickedImage.getUserData();
            
            // หาชื่อหนังจาก posterPath
            int index = posterPaths.indexOf(posterPath);
            String title = (index >= 0 && index < titlemovie.size()) ? titlemovie.get(index) : "ไม่พบชื่อหนัง";
            String description = (index >= 0 && index < descriptions.size()) ? descriptions.get(index): "ไม่พบรายละเอียด";
            String startDate = (index >= 0 && index < startDates.size()) ? startDates.get(index): "ไม่พบเวลา";
            String endDate = (index >= 0 && index < endDates.size()) ? endDates.get(index): "ไม่พบเวลา";
            
            System.out.println("คลิกที่หนัง: " + title + " (" + posterPath + ")");
            
            // เรียก method เพื่อเปลี่ยนหน้าพร้อมส่งข้อมูล
            switchToMovieDetailScene(event, posterPath, title,description,startDate,endDate);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void switchToMovieDetailScene(MouseEvent event, String posterPath, String title,String description,String startDate,String endDate) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CustomerMovieDetail.fxml"));
        Parent root = loader.load();
        
        MovieDetailController detailController = loader.getController();
        
        detailController.setMovieData(posterPath, title,description,startDate,endDate);
     
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
    

