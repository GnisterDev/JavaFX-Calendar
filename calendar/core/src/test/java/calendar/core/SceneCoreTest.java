package calendar.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Unit test class for the {@link SceneCore} utility class.
 * <p>
 * This class tests various methods in {@link SceneCore} related to setting and
 * interacting with
 * the primary {@link Stage} and loading {@link Scene} resources.
 */
public class SceneCoreTest {

    @Mock
    private Stage mockStage;

    @Mock
    private Function<String, URL> mockLoader;

    @Mock
    private URL mockUrl;

    @Mock
    private Scene mockScene;

    /**
     * Initializes mocks and sets up {@link SceneCore} with mocked dependencies
     * before each test.
     */
    @BeforeEach
    public void initialize() {
        MockitoAnnotations.openMocks(this);
        SceneCore.setStage(mockStage);
        SceneCore.setLoader(mockLoader);
    }

    /**
     * Verifies that {@link SceneCore#setStage(Stage)} can set the stage without any
     * initial interaction.
     */
    @Test
    void testSetStage() {
        SceneCore.setStage(mockStage);
        verifyNoInteractions(mockStage);
    }

    /**
     * Verifies that {@link SceneCore#setLoader(Function)} can set the loader
     * without any initial interaction.
     */
    @Test
    void testSetLoader() {
        SceneCore.setLoader(mockLoader);
        verifyNoInteractions(mockLoader); // No interaction expected yet
    }

    /**
     * Tests {@link SceneCore#getSceneUrl(String)} by verifying that a valid URL is
     * returned when
     * the loader function is applied.
     */
    @Test
    void testGetSceneUrl() {
        when(mockLoader.apply("Login.fxml")).thenReturn(mockUrl);

        URL sceneUrl = SceneCore.getSceneUrl("Login.fxml");
        assertEquals(mockUrl, sceneUrl);
        verify(mockLoader).apply("Login.fxml");
    }

    /**
     * Tests {@link SceneCore#getScene(URL)} with an invalid URL, expecting a null
     * {@link Scene} to be returned.
     */
    @Test
    void testNotValidURL() throws MalformedURLException, URISyntaxException {
        URI uri = new URI("http://example.com/nonValidPath");
        URL notValidURL = uri.toURL();

        Scene notValidScene = SceneCore.getScene(notValidURL);
        assertNull(notValidScene, "Expected null Scene for a non-valid URL.");
    }

    /**
     * Tests {@link SceneCore#showStage()} to verify that the stage's {@code show()}
     * method is called.
     */
    @Test
    void testShowStage() {
        SceneCore.showStage();
        verify(mockStage).show();
    }

    /**
     * Tests {@link SceneCore#setResizable(boolean)} to verify that the resizable
     * property of the stage is set.
     */
    @Test
    void testSetResizable() {
        SceneCore.setResizable(true);
        verify(mockStage).setResizable(true);
    }

    /**
     * Tests {@link SceneCore#getStageWidth()} to verify that the width of the stage
     * is returned accurately.
     */
    @Test
    void testGetStageWidth() {
        when(mockStage.getWidth()).thenReturn(800.0);

        double width = SceneCore.getStageWidth();
        assertEquals(800.0, width);
    }

    /**
     * Tests {@link SceneCore#getStageHeight()} to verify that the height of the
     * stage is returned accurately.
     */
    @Test
    void testGetStageHeight() {
        when(mockStage.getHeight()).thenReturn(600.0);

        double height = SceneCore.getStageHeight();
        assertEquals(600.0, height);
    }

    /**
     * Tests {@link SceneCore#setStageTitle(String)} to verify that the title of the
     * stage is set correctly.
     */
    @Test
    void testSetStageTitle() {
        SceneCore.setStageTitle("Test Title");
        verify(mockStage).setTitle("Test Title");
    }
}
