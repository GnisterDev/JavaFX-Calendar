package calendar.core;

import java.net.URL;
import java.util.function.Function;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneCore {
    private static Function<String, URL> loader;
    private static Stage stage;

    public static void setLoader(Function<String, URL> loader) {
        SceneCore.loader = loader;
    }

    public static void setStage(Stage stage) {
        SceneCore.stage = stage;
    }

    public static URL getSceneUrl(String name) {
        return loader.apply(name);
    }

    public static Scene getScene(URL location) {
        try {
            return new Scene(FXMLLoader.load(location));
        } catch (Exception e) {
            return null;
        }
    }

    public static void setScene(String name) {
        stage.setScene(getScene(getSceneUrl(name)));
    }

    public static void showStage() {
        stage.show();
    }

    public static void setResizable(boolean resizable) {
        stage.setResizable(resizable);
    }

    public static double getStageWidth() {
        return stage.getWidth();
    }

    public static double getStageHeight() {
        return stage.getHeight();
    }

    public static void setStageTitle(String title) {
        stage.setTitle(title);
    }
}
