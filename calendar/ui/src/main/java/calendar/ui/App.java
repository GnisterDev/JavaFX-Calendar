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
    public void stop() {
        try {
            Core.destroy();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        try {
            Core.initialize();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }

        SceneCore.setLoader(url -> this.getClass().getResource(url));
        SceneCore.setStage(primaryStage);
        SceneCore.setResizable(false);
        SceneCore.setStageTitle("Calendar");
        SceneCore.setScene("Login.fxml");
        SceneCore.showStage();
    }
}
