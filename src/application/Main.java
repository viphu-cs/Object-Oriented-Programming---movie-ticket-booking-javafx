package application;
	
import java.io.IOException;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;



public class Main extends Application {
	//private static final Preferences prefs = Preferences.userNodeForPackage(Main.class);
    //เปลี่ยน scene
	public static void switchScene(ActionEvent event, String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFile));  // ใช้ Main.class
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();      
    }
	public static void showAlert(AlertType type, String title, String content) {
	    Alert alert = new Alert(type);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(content);
	    alert.showAndWait();
	}	
	
	

	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("loginscene.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			Image icon = new Image(getClass().getResource("Image/Minor Logo.png").toExternalForm());
			primaryStage.getIcons().add(icon); 
			 // 🔹 โหลดตำแหน่งล่าสุดของหน้าต่าง
            /*double lastX = prefs.getDouble("windowX", 100);
            double lastY = prefs.getDouble("windowY", 100);
            primaryStage.setX(lastX);
            primaryStage.setY(lastY);

            // บันทึกตำแหน่งทุกครั้งที่มีการเปลี่ยนแปลง
            primaryStage.xProperty().addListener((obs, oldVal, newVal) -> prefs.putDouble("windowX", newVal.doubleValue()));
            primaryStage.yProperty().addListener((obs, oldVal, newVal) -> prefs.putDouble("windowY", newVal.doubleValue()));*/

            primaryStage.show();
			primaryStage.setTitle("MinorCineplex");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
