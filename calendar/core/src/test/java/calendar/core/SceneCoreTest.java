package calendar.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneCoreTest {

    @Mock
    private Stage mockStage;

    @Mock
    private Function<String, URL> mockLoader;

    @Mock
    private URL mockUrl;

    @Mock
    private Scene mockScene;

    @BeforeEach
    public void initialize() {
        MockitoAnnotations.openMocks(this);
        SceneCore.setStage(mockStage);
        SceneCore.setLoader(mockLoader);
    }

    @Test
    void testSetStage() {
        SceneCore.setStage(mockStage);
        verifyNoInteractions(mockStage);
    }

    @Test
    void testSetLoader() {
        SceneCore.setLoader(mockLoader);
        verifyNoInteractions(mockLoader); // No interaction expected yet
    }

    @Test
    void testGetSceneUrl() {
        when(mockLoader.apply("Login.fxml")).thenReturn(mockUrl);

        URL sceneUrl = SceneCore.getSceneUrl("Login.fxml");
        assertEquals(mockUrl, sceneUrl);
        verify(mockLoader).apply("Login.fxml");
    }

    @Test
    void testShowStage() {
        SceneCore.showStage();
        verify(mockStage).show();
    }

    @Test
    void testSetResizable() {
        SceneCore.setResizable(true);
        verify(mockStage).setResizable(true);
    }

    @Test
    void testGetStageWidth() {
        when(mockStage.getWidth()).thenReturn(800.0);

        double width = SceneCore.getStageWidth();
        assertEquals(800.0, width);
    }

    @Test
    void testGetStageHeight() {
        when(mockStage.getHeight()).thenReturn(600.0);

        double height = SceneCore.getStageHeight();
        assertEquals(600.0, height);
    }

    @Test
    void testSetStageTitle() {
        SceneCore.setStageTitle("Test Title");
        verify(mockStage).setTitle("Test Title");
    }
}
