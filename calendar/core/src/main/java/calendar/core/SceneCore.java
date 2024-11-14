package calendar.core;

import java.net.URL;
import java.util.function.Function;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * SceneCore is a utility class for managing the primary {@link Stage} and
 * switching between {@link Scene} objects in a JavaFX application. It provides
 * static methods for setting a scene loader, configuring the stage, and
 * switching scenes by name.
 */
public final class SceneCore {
    private SceneCore() {

    }

    /** The loader function to load the scene provided. */
    private static Function<String, URL> loader;

    /** The stage of the application. */
    private static Stage stage;

    /**
     * Sets the scene loader function, which converts a scene name into a
     * {@link URL}.
     *
     * @param loader a {@code Function<String, URL>} that returns the
     *               {@link URL} of the scene's FXML file
     */
    public static void setLoader(final Function<String, URL> loader) {
        SceneCore.loader = loader;
    }

    /**
     * Sets the primary {@link Stage} for the application.
     *
     * @param stage the main {@link Stage} for the application
     */
    public static void setStage(final Stage stage) {
        SceneCore.stage = stage;
    }

    /**
     * Retrieves the {@link URL} for the scene based on its name.
     *
     * @param  name the name of the scene
     * @return      the {@link URL} of the FXML file for the scene
     */
    public static URL getSceneUrl(final String name) {
        return loader.apply(name);
    }

    /**
     * Loads a {@link Scene} from the specified {@link URL}.
     *
     * @param  location the {@link URL} of the FXML file
     * @return          the loaded {@link Scene}, or {@code null} if an
     *                  exception occurs.
     */
    public static Scene getScene(final URL location) {
        try {
            return new Scene(FXMLLoader.load(location));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Sets the {@link Scene} of the primary {@link Stage} based on the scene
     * name.
     *
     * @param name the name of the scene to set
     */
    public static void setScene(final String name) {
        stage.setScene(getScene(getSceneUrl(name)));
    }

    public static void showStage() {
        stage.show();
    }

    /**
     * Sets whether the primary {@link Stage} is resizable.
     *
     * @param resizable {@code true} to allow resizing, {@code false} to disable
     *                  resizing.
     */
    public static void setResizable(final boolean resizable) {
        stage.setResizable(resizable);
    }

    public static double getStageWidth() {
        return stage.getWidth();
    }

    public static double getStageHeight() {
        return stage.getHeight();
    }

    /**
     * Sets the title of the primary {@link Stage}.
     *
     * @param title the title to set
     */
    public static void setStageTitle(final String title) {
        stage.setTitle(title);
    }

    /**
     * Get the current x-pos of the window.
     *
     * @return the current x-pos of the window
     */
    public static double getX() {
        return stage.getX();
    }

    /**
     * Get the current y-pos of the window.
     *
     * @return the current y-pos of the window
     */
    public static double getY() {
        return stage.getY();
    }
}
