package calendar.ui;

import calendar.core.RestHelper;
import calendar.core.SceneCore;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * The {@code SignUpController} class is a JavaFX controller responsible for
 * handling user registration interactions. It manages the sign-up form,
 * validates user input, registers new users, and switches scenes upon
 * successful registration or login.
 */
public class SignUpController {

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
     * A node responisble for sending the user to the login scene if clicked.
     */
    @FXML
    private Label signIn;

    /**
     * Initializes the controller. Sets the text color of the
     * {@code messageLabel} to red for displaying errors. Also sets up an event
     * handler for the sign-in label, which switches to the login scene when
     * clicked.
     */
    @FXML
    public void initialize() {
        messageLabel.setTextFill(Color.RED);
        signIn.setOnMouseClicked(event -> {
            SceneCore.setScene("Login.fxml");
        });
    }

    /**
     * Handles the sign-up action when the sign-up button is clicked.
     * It validates the entered username and password, and attempts to register the
     * user.
     * If registration succeeds, the user is logged in and the scene switches to the
     * calendar view.
     * If registration fails, an error message is displayed.
     */
    @FXML
    private void handleLogin() {
        RestHelper.setCredentials(usernameField.getText(), passwordField.getText());
        RestHelper.addUser().consumeError(messageLabel::setText)
                .runIfSuccess(() -> SceneCore.setScene("Calendar.fxml"));
    }
}
