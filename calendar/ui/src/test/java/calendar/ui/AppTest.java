package calendar.ui;

import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.base.WindowMatchers;

import javafx.stage.Stage;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

/**
 * Integration test for the {@link App} class to verify the application launch,
 * UI setup,
 * and initial conditions. This class uses the TestFX framework to interact with
 * the UI elements
 * and verify their presence and properties.
 */
public class AppTest extends ApplicationTest {

    /**
     * Initializes and launches the application for testing.
     * This method manually starts the {@link App} instance on the provided stage.
     *
     * @param stage the JavaFX {@link Stage} used to set up and show the
     *              application's window.
     * @throws Exception if an error occurs during the application start.
     */
    @Start
    public void start(Stage stage) throws Exception {
        // Manually initialize and start the application
        App app = new App();
        app.start(stage);
    }

    /**
     * Verifies that the application window has the correct title set.
     * This test checks that the window with the title "Calendar" is displayed upon
     * launch.
     */
    @Test
    public void testStageTitleIsSetCorrectly() {
        FxAssert.verifyThat(window("Calendar"), WindowMatchers.isShowing());
    }

    /**
     * Verifies that the initial scene displayed is the login scene.
     * This test checks that a UI element with the ID "loginButton" is visible when
     * the application starts,
     * confirming that the login scene is displayed by default.
     */
    @Test
    public void testInitialSceneIsLoginScene() {
        FxAssert.verifyThat("#loginButton", NodeMatchers.isVisible());
    }
}
