package calendar.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import calendar.core.Core;
import calendar.core.SceneCore;
import javafx.event.ActionEvent;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private Label signUp;

    @FXML
    public void initialize() {
        signUp.setOnMouseClicked(event -> {
            SceneCore.setScene("SignUp.fxml");
        });
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (!Core.correctCredentials(username, password)) {
            messageLabel.setText("Username or password is incorrect.");
            return;
        }
        messageLabel.setText("Login successful!");

        Core.logInAsUser(username);
        SceneCore.setScene("Calendar.fxml");
    }
}
