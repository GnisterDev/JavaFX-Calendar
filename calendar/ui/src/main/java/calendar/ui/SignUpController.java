package calendar.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private Button createAccountButton;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validate the inputs
        if (username.isEmpty()) {
            messageLabel.setText("Username cannot be empty.");
            messageLabel.setTextFill(Color.RED);
            return;
        }

        if (password.isEmpty()) {
            messageLabel.setText("Password cannot be empty.");
            messageLabel.setTextFill(Color.RED);
            return;
        }

        // Check if the password meets a certain criteria
        if (password.length() < 6) {
            messageLabel.setText("Password must be at least 6 characters long.");
            messageLabel.setTextFill(Color.RED);
            return;
        }

        // Logic to create the account
        boolean accountCreated = createNewAccount(username, password);

        if (accountCreated) {
            messageLabel.setText("Account has successfully been created!");
            messageLabel.setTextFill(Color.GREEN);
        } else {
            messageLabel.setText("Failed creating account. Try again.");
            messageLabel.setTextFill(Color.RED);
        }
    }

    private boolean createNewAccount(String username, String password) {
        // Add logic to save the account to file
        // For now just return true to simulate successful account creation
        return true;
    }
}