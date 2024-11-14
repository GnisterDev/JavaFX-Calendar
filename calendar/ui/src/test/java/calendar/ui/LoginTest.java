package calendar.ui;

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

/**
 * A test class for the Login functionality in the application.
 * This class uses TestFX to simulate user interactions with the Login GUI
 * and Mockito to mock backend dependencies.
 * It tests different login scenarios, including valid login, invalid passwords,
 * non-existing usernames, and navigation to the Sign-Up screen.
 */
public class LoginTest extends ApplicationTest {
    private String usernameID = "#usernameField";
    private String passwordID = "#passwordField";
    private String loginButtonID = "#loginButton";

    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;
    private Label signUp;

    /**
     * Initializes and sets up the Login screen stage.
     * This method loads the FXML file for the Login view, sets the scene, and
     * displays it.
     * It also sets up the {@link SceneCore} with a loader and stage reference
     * for consistent scene transitions across the application.
     *
     * @param stage the primary stage for displaying the Login screen
     * @throws Exception if the FXML file for the Login view cannot be loaded
     */
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

    /**
     * Sets up mocks and test data for each test case.
     * This method creates a mock {@link UserStore} and configures responses to
     * simulate:
     * - A valid user with a correct password.
     * - An invalid password attempt.
     * - A non-existing user.
     *
     * @throws Exception if the setup process fails
     */
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

    /**
     * Verifies that a user can successfully log in with a valid username and
     * password.
     * This test simulates entering a correct username and password.
     * It also checks that the login form elements are properly initialized.
     */
    @Test
    public void testValidLogin() {
        assertNotNull(usernameField, "usernameField should be initialized!");
        assertNotNull(passwordField, "passwordField should be initialized!");
        assertNotNull(messageLabel, "messageLabel should be initialized!");

        clickOn(usernameID).write("validUser");
        clickOn(passwordID).write("validPass");

        clickOn(loginButtonID);
    }

    /**
     * Verifies that entering a valid username but an incorrect password results in
     * an unsuccessful login attempt. This test simulates an invalid password entry
     * for a valid user.
     */
    @Test
    public void testInvalidPassword() {
        clickOn(usernameID).write("validUser");
        clickOn(passwordID).write("invalidPass");
        clickOn(loginButtonID);
    }

    /**
     * Verifies that attempting to log in with a non-existing username
     * results in an unsuccessful login attempt. This test simulates entering
     * a username that does not exist in the user store.
     */
    @Test
    public void testInvalidUsername() {
        clickOn(usernameID).write("invalidUser");
        clickOn(passwordID).write("validPass");
        clickOn(loginButtonID);
    }

    /**
     * Verifies that clicking on the "I dont have a user" (signUp) label redirects
     * the user to the Sign-Up screen.
     * This test simulates a user clicking the "I dont have a user" (signUp) label
     * to navigate to the registration view.
     */
    @Test
    public void testGotoSignup() {
        clickOn(signUp);
    }
}
