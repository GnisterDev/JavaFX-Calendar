package calendar.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import calendar.core.Core;
import calendar.core.Error;
import calendar.core.SceneCore;
import calendar.types.User;
import calendar.types.UserStore;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SignUpTest extends ApplicationTest {

    private Label messageLabel;
    private Label login;

    private String usernameID = "#usernameField";
    private String passwordID = "#passwordField";
    private String signupButtonID = "#signUpButton";

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
        Parent root = loader.load();

        messageLabel = (Label) root.lookup("#messageLabel");
        login = (Label) root.lookup("#signIn");

        stage.setScene(new Scene(root));
        stage.show();

        SceneCore.setLoader(url -> this.getClass().getResource(url));
        SceneCore.setStage(stage);
    }

    @BeforeEach
    public void setUp() throws Exception {
        UserStore mockUserStore = mock(UserStore.class);
        Core.userStore = mockUserStore;

        User validUser = mock(User.class);

        // Simulate successful registration of a new user
        when(mockUserStore.hasUsername("newUser")).thenReturn(false); // The username doesn't exist initially
        when(mockUserStore.hasUsername("existingUser")).thenReturn(true);
        when(mockUserStore.addUser(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            when(mockUserStore.getUserId(user.getUsername())).thenReturn(Optional.of(user.getUserId()));
            when(mockUserStore.getUser(user.getUserId())).thenReturn(Optional.of(user));
            return true;
        });

        // Simulate an existing user
        when(validUser.checkPassword("validPass")).thenReturn(true);
    }

    @Test
    public void testExistingUser() {
        clickOn(usernameID).write("existingUser");
        clickOn(passwordID).write("validPass");

        clickOn(signupButtonID);

        // Verify the error message for existing username
        assertEquals(Error.SIGNUP_USERNAME_ALREADY_EXISTS, messageLabel.getText());
    }

    @Test
    public void testShortPassword() {
        clickOn(usernameID).write("newUser");
        clickOn(passwordID).write("short");

        clickOn(signupButtonID);

        // Verify the error message for short password
        assertEquals(Error.SIGNUP_PASSWORD_TOO_SHORT, messageLabel.getText());
    }

    @Test
    public void testValidSignUp() {
        clickOn(usernameID).write("newUser");
        clickOn(passwordID).write("validPass");

        clickOn(signupButtonID);
    }

    @Test
    public void testEmptyUsername() {
        clickOn(usernameID).write("");
        clickOn(passwordID).write("validPass");

        clickOn(signupButtonID);

        // Verify error message for empty username
        assertEquals(Error.SIGNUP_USERNAME_IS_EMPTY, messageLabel.getText());
    }

    @Test
    public void testEmptyPassword() {
        clickOn(usernameID).write("newUser");
        clickOn(passwordID).write("");

        clickOn(signupButtonID);

        // Verify error message for empty password
        assertEquals(Error.SIGNUP_PASSWORD_IS_EMPTY, messageLabel.getText());
    }

    @Test
    public void testGotoSignup() {
        clickOn(login);
    }
}
