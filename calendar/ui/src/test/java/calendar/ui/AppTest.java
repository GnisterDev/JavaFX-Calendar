package calendar.ui;

import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.matcher.base.NodeMatchers;
import org.testfx.matcher.base.WindowMatchers;

import javafx.stage.Stage;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.framework.junit5.Start;

public class AppTest extends ApplicationTest {

    @Start
    public void start(Stage stage) throws Exception {
        // Manually initialize and start the application
        App app = new App();
        app.start(stage);
    }

    @Test
    public void testStageTitleIsSetCorrectly() {
        FxAssert.verifyThat(window("Calendar"), WindowMatchers.isShowing());
    }

    @Test
    public void testInitialSceneIsLoginScene() {
        FxAssert.verifyThat("#loginButton", NodeMatchers.isVisible());
    }
}
