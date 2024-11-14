
package calendar.ui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import calendar.core.SceneCore;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The {@code App} class serves as the main entry point for the JavaFX
 * application. It extends the {@link Application} class, initializing the core
 * components and setting up the primary stage. The application starts by
 * loading the login scene and performs cleanup when the application is stopped.
 */
public class App extends Application {

    /**
     * The main method that launches the JavaFX application. This method serves
     * as the entry point to the application.
     *
     * @param args command-line arguments (not used in this application)
     */
    public static void main(final String[] args) {
        Application.launch();
    }

    /**
     * This method is called when the JavaFX application is started. It
     * initializes the core logic of the application, sets up the primary stage,
     * and loads the initial scene.
     *
     * @param  primaryStage the primary stage for the JavaFX application
     * @throws IOException  if there is an issue loading resources or scenes
     */
    @Override
    public void start(final Stage primaryStage) throws IOException {
        SceneCore.setLoader(url -> this.getClass().getResource(url));
        SceneCore.setStage(primaryStage);
        SceneCore.setResizable(false);
        SceneCore.setStageTitle("Calendar");
        SceneCore.setScene("Login.fxml");
        SceneCore.showStage();
    }
}
