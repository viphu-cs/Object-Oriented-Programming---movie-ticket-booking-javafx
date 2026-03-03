package application;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SignupController {

    @FXML
    private TextField firstnamefield;
    @FXML
    private TextField lastnamefield;
    @FXML
    private TextField usernamefield;
    @FXML
    private TextField emailfield;
    @FXML
    private PasswordField passfield;
    @FXML
    private PasswordField conpassfield;

    // เช็คข้อมูลและบันทึกลงไฟล์
    public boolean signupUser() {
        String firstname = firstnamefield.getText();
        String lastname = lastnamefield.getText();
        String username = usernamefield.getText();
        String email = emailfield.getText();
        String password = passfield.getText();
        String conpassword = conpassfield.getText();

        if (!password.equals(conpassword)) {
            Main.showAlert(AlertType.ERROR, "Signup Failed", "Passwords do not match.");
            
            return false;
        }

        if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Main.showAlert(AlertType.ERROR, "Signup Failed", "Please fill in all fields.");
            return false;
        }

        if (JSONManager.isUsernameExists("Customer.json", username)) {
            Main.showAlert(AlertType.ERROR, "Signup Failed", "Username already exists.");
            return false;
        }
        if (JSONManager.isUsernameExists("Employee.json", username)) {
            Main.showAlert(AlertType.ERROR, "Signup Failed", "Username already exists.");
            return false;
        }

        JSONManager.addUser("Customer.json", new User(firstname, lastname, username, email, password));
        return true;
    }

    @FXML
    public void signupbutton(ActionEvent event) throws IOException {
        if (signupUser()) {
            Main.showAlert(AlertType.CONFIRMATION, "Signup Successfully", "Your account has been created.");
        }
    }

    @FXML
    public void backbutton(ActionEvent event) throws IOException {
        Main.switchScene(event, "loginscene.fxml");
    }

 
}
