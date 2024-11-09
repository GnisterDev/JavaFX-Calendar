package calendar.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import calendar.core.SceneCore;
import calendar.types.RestUser;
import calendar.types.UserStore;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginTest extends ApplicationTest {
    private String usernameID = "#usernameField";
    private String passwordID = "#passwordField";
    private String loginButtonID = "#loginButton";

    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;
    private Label signUp;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();

        usernameField = (TextField) root.lookup(usernameID);
        passwordField = (PasswordField) root.lookup(passwordID);
        messageLabel = (Label) root.lookup("#messageLabel");
        signUp = (Label) root.lookup("#signUp");

        stage.setScene(new Scene(root));
        stage.show();

        SceneCore.setLoader(url -> this.getClass().getResource(url));
        SceneCore.setStage(stage);
    }

    @BeforeEach
    public void setUp() throws Exception {

        UserStore mockUserStore = mock(UserStore.class);

        UUID validUserId = UUID.randomUUID();
        RestUser validUser = mock(RestUser.class);

        when(mockUserStore.getUserId("validUser")).thenReturn(Optional.of(validUserId));
        when(mockUserStore.getUser(validUserId)).thenReturn(Optional.of(validUser));
        when(validUser.checkPassword("validPass")).thenReturn(true);
        when(mockUserStore.getUserId("invalidUser")).thenReturn(Optional.empty());

    }

    @Test
    public void testValidLogin() {
        assertNotNull(usernameField, "usernameField should be initialized!");
        assertNotNull(passwordField, "passwordField should be initialized!");
        assertNotNull(messageLabel, "messageLabel should be initialized!");

        clickOn(usernameID).write("validUser");
        clickOn(passwordID).write("validPass");

        clickOn(loginButtonID);
    }

    @Test
    public void testInvalidPassword() {
        clickOn(usernameID).write("validUser");
        clickOn(passwordID).write("invalidPass");
        clickOn(loginButtonID);
    }

    @Test
    public void testInvalidUsername() {
        clickOn(usernameID).write("invalidUser");
        clickOn(passwordID).write("validPass");
        clickOn(loginButtonID);
    }

    @Test
    public void testGotoSignup() {
        clickOn(signUp);
    }
}
