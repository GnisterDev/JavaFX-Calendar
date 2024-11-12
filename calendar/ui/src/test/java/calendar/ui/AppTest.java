package calendar.ui;

import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.base.WindowMatchers;

import javafx.stage.Stage;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

/**
 * UI test class for the main application window of the Calendar application.
 * <p>
 * This class uses the TestFX framework to verify the initial state and behavior
 * of the application's main stage and UI components.
 */

public class AppTest extends ApplicationTest {

    /**
     * Starts the application by manually initializing and displaying the main
     * stage.
     * <p>
     * This method overrides the default TestFX application start behavior to
     * directly
     * launch the `App` class instance, ensuring that the UI components are
     * available
     * for testing.
     *
     * @param stage the main JavaFX stage for the application
     * @throws Exception if an error occurs during application startup
     */

    @Start
    public void start(Stage stage) throws Exception {
        // Manually initialize and start the application
        App app = new App();
        app.start(stage);
    }

    /**
     * Tests that the application window's title is correctly set to "Calendar"
     * when the application starts.
     * <p>
     * This test verifies that the main window is visible and has the expected
     * title,
     * ensuring proper initialization of the application stage.
     */
    @Test
    public void testStageTitleIsSetCorrectly() {
        FxAssert.verifyThat(window("Calendar"), WindowMatchers.isShowing());
    }

    /**
     * Verifies that the initial scene displayed upon application launch is the
     * login scene.
     * <p>
     * This test checks that the login button is visible, indicating that the
     * correct
     * initial UI scene has been loaded and displayed.
     */
    @Test
    public void testInitialSceneIsLoginScene() {
        FxAssert.verifyThat("#loginButton", NodeMatchers.isVisible());
    }
}
