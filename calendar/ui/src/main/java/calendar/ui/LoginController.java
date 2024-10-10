package calendar.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
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
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/calendar/ui/SignUp.fxml"));
                Scene calendarScene = new Scene(loader.load());

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                stage.setScene(calendarScene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
