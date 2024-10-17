package calendar.ui;

import java.util.Optional;

import calendar.core.Core;
import calendar.core.SceneCore;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class SignUpController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    @FXML
    private Label signIn;

    @FXML
    public void initialize() {
        messageLabel.setTextFill(Color.RED);
        signIn.setOnMouseClicked(event -> {
            SceneCore.setScene("Login.fxml");
        });
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        Optional<String> createdUserError = Core.registerUser(username, password);
        createdUserError.ifPresentOrElse(l -> messageLabel.setText(l), () -> {
            Core.logInAsUser(username);
            SceneCore.setScene("Calendar.fxml");
        });
    }
}
