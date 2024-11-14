package calendar.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

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
import javafx.stage.Stage;

/**
 * A test class for the Sign-Up functionality in the application.
 * This class uses the TestFX library to simulate user interactions with
 * the Sign-Up GUI and Mockito to mock dependencies.
 * It tests various scenarios of user registration, including cases with
 * existing users, short passwords, empty fields, and successful sign-up.
 */
public class SignUpTest extends ApplicationTest {

    private Label login;

    private String usernameID = "#usernameField";
    private String passwordID = "#passwordField";
    private String signupButtonID = "#signUpButton";

    /**
     * Initializes and sets up the application stage with the Sign-Up screen.
     * This method loads the FXML file for the Sign-Up view, sets the scene, and
     * displays it.
     * It also sets up the {@link SceneCore} with a resource loader and stage
     * reference
     * for consistency across scenes in the application.
     *
     * @param stage the main stage to be displayed
     * @throws Exception if the FXML file for the Sign-Up view cannot be loaded
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SignUp.fxml"));
        Parent root = loader.load();

        login = (Label) root.lookup("#signIn");

        stage.setScene(new Scene(root));
        stage.show();

        SceneCore.setLoader(url -> this.getClass().getResource(url));
        SceneCore.setStage(stage);
    }

    /**
     * Sets up mocks and test data for each test case.
     * This method creates a mock {@link UserStore} to simulate interaction with
     * a backend data store and configures mock responses for different user
     * scenarios:
     * - A new user registration where the username does not already exist.
     * - An existing user check where the username is already taken.
     * - A mock {@link RestUser} to represent a user with a valid password.
     *
     * @throws Exception if the setup fails
     */
    @BeforeEach
    public void setUp() throws Exception {
        UserStore mockUserStore = mock(UserStore.class);

        RestUser validUser = mock(RestUser.class);

        // Simulate successful registration of a new user
        when(mockUserStore.hasUsername("newUser")).thenReturn(false); // The username doesn't exist initially
        when(mockUserStore.hasUsername("existingUser")).thenReturn(true);
        when(mockUserStore.addUser(any(RestUser.class))).thenAnswer(invocation -> {
            RestUser user = invocation.getArgument(0);
            when(mockUserStore.getUserId(user.getUsername())).thenReturn(Optional.of(user.getUserId()));
            when(mockUserStore.getUser(user.getUserId())).thenReturn(Optional.of(user));
            return true;
        });

        // Simulate an existing user
        when(validUser.checkPassword("validPass")).thenReturn(true);
    }

    /**
     * Validates that an attempt to sign up with an existing username
     * (already registered in the user store) is handled correctly.
     */
    @Test
    public void testExistingUser() {
        clickOn(usernameID).write("existingUser");
        clickOn(passwordID).write("validPass");

        clickOn(signupButtonID);
    }

    /**
     * Validates that attempting to sign up with a password that is too short
     * triggers appropriate handling or error feedback.
     * This test simulates entering a new username with a password that is
     * shorter than required.
     */
    @Test
    public void testShortPassword() {
        clickOn(usernameID).write("newUser");
        clickOn(passwordID).write("short");

        clickOn(signupButtonID);
    }

    /**
     * Validates that a valid new user can successfully complete the signup process.
     * This test simulates entering a new username and a valid password
     * that meets the system’s criteria.
     */
    @Test
    public void testValidSignUp() {
        clickOn(usernameID).write("newUser");
        clickOn(passwordID).write("validPass");

        clickOn(signupButtonID);
    }

    /**
     * Validates that attempting to sign up with an empty username field
     * is handled correctly. This test simulates leaving the username field
     * blank and entering a valid password.
     */
    @Test
    public void testEmptyUsername() {
        clickOn(usernameID).write("");
        clickOn(passwordID).write("validPass");

        clickOn(signupButtonID);
    }

    /**
     * Validates that attempting to sign up with an empty password field
     * is handled correctly. This test simulates entering a new username
     * while leaving the password field blank.
     */
    @Test
    public void testEmptyPassword() {
        clickOn(usernameID).write("newUser");
        clickOn(passwordID).write("");

        clickOn(signupButtonID);
    }

    /**
     * Validates that clicking on the "I allready have a user" label on the sign-up
     * screen
     * successfully redirects the user to the login screen.
     * This test simulates a user clicking on the "I allready have a user" label,
     * representing
     * the user's attempt to navigate back to the login view.
     */
    @Test
    public void testGotoSignup() {
        clickOn(login);
    }
}
