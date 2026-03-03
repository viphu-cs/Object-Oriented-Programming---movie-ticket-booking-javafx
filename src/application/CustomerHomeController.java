package application;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CustomerHomeController {

    @FXML
    private ImageView sliderImageView;
    
    @FXML
    private StackPane imageSliderPane;
    
    @FXML
    private Button prevButton;
    
    @FXML
    private Button nextButton;
    
    @FXML
    private Button playPauseButton;
    
    private List<Image> images = new ArrayList<>();
    private int currentImageIndex = 0;
    private Timeline slideshow;
    private boolean isPlaying = true;
    
    @FXML
    public void initialize() {
        // โหลดรูปภาพสำหรับสไลด์โชว์ (เปลี่ยนเป็นเส้นทางที่ถูกต้องสำหรับรูปภาพของคุณ)
        loadImages();
        
        // แสดงรูปภาพแรกเมื่อโหลดหน้า
        if (!images.isEmpty()) {
            sliderImageView.setImage(images.get(0));
        }
        
        // สร้าง Timeline สำหรับการเปลี่ยนรูปอัตโนมัติ
        setupSlideshow();
    }
    
    private void loadImages() {
        try {
            // เปลี่ยนเส้นทางเหล่านี้เป็นเส้นทางที่ถูกต้องสำหรับรูปภาพของคุณ
            String[] imagePaths = {
                "src/application/Image/promo1.png",
                "src/application/Image/promo2.png",
                "src/application/Image/promo3.png",
       
            };
            
            for (String path : imagePaths) {
                File file = new File(path);
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    images.add(image);
                } else {
                    System.err.println("ไม่พบไฟล์รูปภาพ: " + path);
                }
            }
            
            // ถ้าไม่พบรูปภาพใด ๆ ให้แสดงข้อความข้อผิดพลาด
            if (images.isEmpty()) {
                System.err.println("ไม่สามารถโหลดรูปภาพใด ๆ สำหรับสไลด์โชว์");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setupSlideshow() {
        // สร้าง Timeline ที่เปลี่ยนรูปทุก 3 วินาที
        slideshow = new Timeline(new KeyFrame(Duration.seconds(4), e -> nextImage()));
        slideshow.setCycleCount(Timeline.INDEFINITE);
        slideshow.play();
    }
    
    @FXML
    public void nextImage() {
        if (images.isEmpty()) return;
        
        currentImageIndex = (currentImageIndex + 1) % images.size();
        sliderImageView.setImage(images.get(currentImageIndex));
    }
    
    @FXML
    public void previousImage() {
        if (images.isEmpty()) return;
        
        currentImageIndex = (currentImageIndex - 1 + images.size()) % images.size();
        sliderImageView.setImage(images.get(currentImageIndex));
    }
    
    @FXML
    public void toggleSlideshow() {
        if (isPlaying) {
            slideshow.pause();
            playPauseButton.setText("Play");
        } else {
            slideshow.play();
            playPauseButton.setText("Pause");
        }
        isPlaying = !isPlaying;
    }
    
    // เมธอดเดิมที่คุณมี
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
    
    public void ContectUS(ActionEvent event) throws IOException {
        Main.switchScene(event, "CustomerContectUS.fxml");
    }
    
    public void logoutbotton(ActionEvent event) throws IOException {
        Main.switchScene(event, "loginscene.fxml");
    }
}