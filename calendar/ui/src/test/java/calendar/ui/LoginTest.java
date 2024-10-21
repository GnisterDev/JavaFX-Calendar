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

import calendar.core.Core;
import calendar.core.SceneCore;
import calendar.types.User;
import calendar.types.UserStore;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginTest extends ApplicationTest {

    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();

        usernameField = (TextField) root.lookup("#usernameField");
        passwordField = (PasswordField) root.lookup("#passwordField");
        messageLabel = (Label) root.lookup("#messageLabel");

        stage.setScene(new Scene(root));
        stage.show();

        SceneCore.setLoader(url -> this.getClass().getResource(url));
        SceneCore.setStage(stage);
    }

    @BeforeEach
    public void setUp() throws Exception {

        // Mock UserStore

        UserStore mockUserStore = mock(UserStore.class);

        Core.userStore = mockUserStore;

        UUID validUserId = UUID.randomUUID();
        User validUser = mock(User.class);

        when(mockUserStore.getUserId("validUser")).thenReturn(Optional.of(validUserId));
        when(mockUserStore.getUser(validUserId)).thenReturn(Optional.of(validUser));

        when(validUser.checkPassword("validPass")).thenReturn(true);

        when(mockUserStore.getUserId("invalidUser")).thenReturn(Optional.empty());

    }

    @Test
    public void testValidLogin() {
        // try (MockedStatic<SceneCore> sceneCoreMock =
        // Mockito.mockStatic(SceneCore.class)) {
        // Mock the setScene method to do nothing (since we don't want an actual scene
        // change)
        // ceneCoreMock.when(() ->
        // SceneCore.setScene(anyString())).thenAnswer(invocation -> null);

        assertNotNull(usernameField, "usernameField should be initialized!");
        assertNotNull(passwordField, "passwordField should be initialized!");
        assertNotNull(messageLabel, "messageLabel should be initialized!");

        clickOn("#usernameField").write("validUser");
        clickOn("#passwordField").write("validPass");

        clickOn("#loginButton");

        // Verify the message label text is updated for a successful login
        assertEquals("Login successful!", messageLabel.getText());

        // Verify that the scene is switched to Calendar.fxml
        // sceneCoreMock.verify(() -> SceneCore.setScene("Calendar.fxml"), times(1));
        // }

    }

    @Test
    public void testInvalidPassword() {
        clickOn("#usernameField").write("validUser");
        clickOn("#passwordField").write("invalidPass");

        clickOn("#loginButton");

        assertEquals("Username or password is incorrect.", messageLabel.getText());

    }

    @Test
    public void testInvalidUsername() {
        clickOn("#usernameField").write("invalidUser");
        clickOn("#passwordField").write("validPass");

        clickOn("#loginButton");

        assertEquals("Username or password is incorrect.", messageLabel.getText());

    }
}
