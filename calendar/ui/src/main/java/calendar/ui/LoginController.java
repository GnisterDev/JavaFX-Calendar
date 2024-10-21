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
    protected TextField usernameField;

    @FXML
    protected PasswordField passwordField;

    @FXML
    protected Label messageLabel;

    @FXML
    protected Label signUp;

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

        // checks if password and username is correct
        if (!Core.correctCredentials(username, password)) {
            messageLabel.setText("Invalid username or password.");
            return;
        }

        // Log user in
        messageLabel.setText("Login successful!");
        Core.logInAsUser(username);

        // Switch scene
        SceneCore.setScene("Calendar.fxml");
    }
}
