package application;

import java.io.IOException;
import java.util.Scanner;

import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.FileReader;

public class loginController {

    @FXML
    private TextField namefield;

    @FXML
    private PasswordField passfield;
    private static String loggedInUsername = null;

    // ปุ่ม signup
    @FXML
    public void signupbutton(ActionEvent event) throws IOException {
    	Main.switchScene(event, "signupscene.fxml");
    }

    public boolean loginuser() {
        String username = namefield.getText();
        String password = passfield.getText();

        // ตรวจสอบจากทั้งสองไฟล์
        return checkCredentials("Customer.json", username, password) || checkCredentials("Employee.json", username, password);
    }

    private boolean checkCredentials(String filename, String username, String password) {
        try (Scanner scanner = new Scanner(new FileReader(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                try {
                    JSONObject user = new JSONObject(line);
                    if (user.getString("username").equals(username) && user.getString("password").equals(password)) {
                        return true;
                    }
                } catch (org.json.JSONException e) {
                    System.err.println("Invalid JSON in " + filename + ": " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + filename);
            e.printStackTrace();
        }
        return false;
    }


    // ปุ่ม login
    @FXML
    public void loginbutton(ActionEvent event) throws IOException {
        String username = namefield.getText();
        String password = passfield.getText();

        // เช็คว่าเป็นลูกค้าหรือพนักงาน
        if (checkCredentials("Customer.json", username, password)) {
            Main.switchScene(event, "CustomerHome.fxml");
            loggedInUsername = username;
            
        } else if (checkCredentials("Employee.json", username, password)) {
            Main.switchScene(event, "EmployeeDashboard.fxml");
            loggedInUsername = username;
        } else {
            Main.showAlert(AlertType.ERROR, "Warning", "Username or password is incorrect.");
            
        }
    }
    
    public static String getLoggedInUsername() {
        return loggedInUsername;
    }
    



}
