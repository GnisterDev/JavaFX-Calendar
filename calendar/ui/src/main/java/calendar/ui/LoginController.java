package calendar.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import calendar.core.Core;
import calendar.core.SceneCore;
import javafx.event.ActionEvent;

/**
 * The {@code LoginController} class is a JavaFX controller responsible for handling user login interactions.
 * It manages the login form, validates user credentials, and switches scenes upon successful login or registration.
 */
public class LoginController {

    @FXML
    protected TextField usernameField;

    @FXML
    protected PasswordField passwordField;

    @FXML
    protected Label messageLabel;

    @FXML
    protected Label signUp;

    /**
     * Initializes the controller. Sets up the event handler for the sign-up label, which switches to the "Sign Up" scene when clicked.
     */
    @FXML
    public void initialize() {
        signUp.setOnMouseClicked(event -> {
            SceneCore.setScene("SignUp.fxml");
        });
    }

    /**
     * Handles the login action when the login button is clicked.
     * It validates the credentials entered in the {@code usernameField} and {@code passwordField}.
     * If the credentials are correct, it logs the user in and switches to the calendar scene. If not, it displays an error message.
     * 
     * @param event the {@code ActionEvent} triggered by the login button
     */
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
