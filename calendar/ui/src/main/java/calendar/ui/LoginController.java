package calendar.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import calendar.core.RestHelper;
import calendar.core.SceneCore;
import javafx.event.ActionEvent;

/**
 * The {@code LoginController} class is a JavaFX controller responsible for
 * handling user login interactions. It manages the login form, validates user
 * credentials, and switches scenes upon successful login or registration.
 */
public class LoginController {

    /** The input for the username. */
    @FXML
    private TextField usernameField;

    /** The input for the password. */
    @FXML
    private PasswordField passwordField;

    /**
     * The label resposible for displaying if there has been an error in
     * creating the event.
     */
    @FXML
    private Label messageLabel;

    /**
     * A node responisble for sending the user to the signUp scene if clicked.
     */
    @FXML
    private Label signUp;

    /**
     * Initializes the controller. Sets up the event handler for the sign-up
     * label, which switches to the "Sign Up" scene when clicked.
     */
    @FXML
    public void initialize() {
        signUp.setOnMouseClicked(event -> {
            SceneCore.setScene("SignUp.fxml");
        });
    }

    /**
     * Handles the login action when the login button is clicked.
     * It validates the credentials entered in the {@code usernameField} and
     * {@code passwordField}.
     * If the credentials are correct, it logs the user in and switches to the
     * calendar scene. If not, it displays an error message.
     * 
     * @param event the {@code ActionEvent} triggered by the login button
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        RestHelper.setCredentials(usernameField.getText(), usernameField.getText());

        RestHelper.getUser().consumeError(messageLabel::setText)
                .runIfSuccess(() -> SceneCore.setScene("Calendar.fxml"));

    }
}
