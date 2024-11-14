package calendar.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import calendar.persistence.internal.ColorSerializer;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link ColorSerializer}, which serializes JavaFX {@link Color}
 * objects
 * to JSON format with RGB values. This class validates the JSON output for
 * specific colors
 * to ensure correct serialization by the custom {@link ColorSerializer}.
 */
public class ColorSerializerTest {

    private ObjectMapper objectMapper;

    /**
     * Sets up the {@link ObjectMapper} with a {@link SimpleModule} registering
     * {@link ColorSerializer} for {@link Color} objects before each test.
     */
    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Color.class, new ColorSerializer());
        objectMapper.registerModule(module);
    }

    /**
     * Tests serialization of a basic gray color with RGB values (0.5, 0.5, 0.5).
     * 
     * @throws JsonProcessingException if an error occurs during JSON serialization.
     */
    @Test
    public void testSerializeBasicColor() throws JsonProcessingException {
        Color color = Color.color(0.5, 0.5, 0.5); // Gray
        String json = objectMapper.writeValueAsString(color);

        assertEquals("{\"red\":0.5,\"green\":0.5,\"blue\":0.5}", json);
    }

    /**
     * Tests serialization of a pure green color with RGB values (0.0, 1.0, 0.0).
     * 
     * @throws JsonProcessingException if an error occurs during JSON serialization.
     */
    @Test
    public void testSerializeExtremeColorValues() throws JsonProcessingException {
        Color color = Color.color(0.0, 1.0, 0.0); // Pure green
        String json = objectMapper.writeValueAsString(color);

        assertEquals("{\"red\":0.0,\"green\":1.0,\"blue\":0.0}", json);
    }

    /**
     * Tests serialization of a white color with RGB values (1.0, 1.0, 1.0).
     * 
     * @throws JsonProcessingException if an error occurs during JSON serialization.
     */
    @Test
    public void testSerializeWhiteColor() throws JsonProcessingException {
        Color color = Color.color(1.0, 1.0, 1.0); // White
        String json = objectMapper.writeValueAsString(color);

        assertEquals("{\"red\":1.0,\"green\":1.0,\"blue\":1.0}", json);
    }

    /**
     * Tests serialization of a black color with RGB values (0.0, 0.0, 0.0).
     * 
     * @throws JsonProcessingException if an error occurs during JSON serialization.
     */
    @Test
    public void testSerializeBlackColor() throws JsonProcessingException {
        Color color = Color.color(0.0, 0.0, 0.0); // Black
        String json = objectMapper.writeValueAsString(color);

        assertEquals("{\"red\":0.0,\"green\":0.0,\"blue\":0.0}", json);
    }
}
