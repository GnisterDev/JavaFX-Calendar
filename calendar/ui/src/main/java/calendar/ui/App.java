package calendar.ui;

import java.io.IOException;

import calendar.core.Core;
import calendar.core.SceneCore;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
    public static void main(String[] args) {
        Application.launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Core.initialize();

        SceneCore.setLoader(url -> this.getClass().getResource(url));
        SceneCore.setStage(primaryStage);
        SceneCore.setResizable(false);
        SceneCore.setStageTitle("Calendar");
        SceneCore.setScene("Login.fxml");
        SceneCore.showStage();
    }

    @Override
    public void stop() {
        Core.destroy();
    }
}
