package calendar.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import calendar.core.Core;
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

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
        Parent root = loader.load();

        messageLabel = (Label) root.lookup("#messageLabel");

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
        clickOn("#usernameField").write("existingUser");
        clickOn("#passwordField").write("validPass");

        clickOn("#signUpButton");

        // Verify the error message for existing username
        assertEquals("Username already exists.", messageLabel.getText());
    }

    @Test
    public void testShortPassword() {
        clickOn("#usernameField").write("newUser");
        clickOn("#passwordField").write("short");

        clickOn("#signUpButton");

        // Verify the error message for short password
        assertEquals("Password must be at least 6 characters long.", messageLabel.getText());
    }

    @Test
    public void testValidSignUp() {
        clickOn("#usernameField").write("newUser");
        clickOn("#passwordField").write("validPass");

        clickOn("#signUpButton");

    }

    @Test
    public void testEmptyUsername() {
        clickOn("#usernameField").write("");
        clickOn("#passwordField").write("validPass");

        clickOn("#signUpButton");

        // Verify error message for empty username
        assertEquals("Username cannot be empty", messageLabel.getText());
    }

    @Test
    public void testEmptyPassword() {
        clickOn("#usernameField").write("newUser");
        clickOn("#passwordField").write("");

        clickOn("#signUpButton");

        // Verify error message for empty password
        assertEquals("Password cannot be empty.", messageLabel.getText());
    }
}
