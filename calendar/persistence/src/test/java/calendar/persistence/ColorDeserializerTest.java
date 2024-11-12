package calendar.persistence;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import calendar.persistence.internal.ColorDeserializer;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

/**
 * Unit tests for the {@link ColorDeserializer} class, which is responsible for
 * deserializing
 * JSON objects into {@link Color} instances with specified RGB values. This
 * test class
 * uses the Jackson library to map JSON strings to Java objects and the Mockito
 * framework
 * to mock deserialization components.
 * 
 * <p>
 * The tests in this class ensure the correctness of color deserialization by
 * verifying
 * behavior with various JSON input cases, including valid RGB values, missing
 * fields, invalid
 * values, and boundary values.
 */
public class ColorDeserializerTest {

    private ObjectMapper objectMapper;
    private String json;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Color.class, new ColorDeserializer());
        objectMapper.registerModule(module);
    }

    /**
     * Tests various scenarios for deserialization of {@link Color} objects from
     * JSON data
     * to validate the custom {@code ColorDeserializer} behavior under typical,
     * edge, and error cases.
     *
     * <p>
     * This test covers:
     * <ul>
     * <li>Standard color deserialization: verifies that JSON with valid RGB values
     * produces the expected {@link Color}.</li>
     * <li>Missing fields: ensures a {@link NullPointerException} is thrown when a
     * required color field (e.g., "green") is missing.</li>
     * <li>Invalid field values: verifies that non-numeric values (e.g., "red" as a
     * string) are set to zero.</li>
     * <li>Out-of-bounds values: confirms that illegal RGB values (e.g., greater
     * than 1.0 or less than 0.0) throw an {@link IllegalArgumentException}.</li>
     * <li>Null field values: verifies that {@code null} values default to zero in
     * the deserialized {@link Color}.</li>
     * <li>Negative values: asserts that negative RGB values throw an
     * {@link IllegalArgumentException}.</li>
     * <li>Non-decimal values: validates deserialization of whole number RGB
     * values.</li>
     * <li>Maximum RGB values: confirms correct deserialization for values at the
     * upper bound (1.0).</li>
     * <li>Small decimal values: verifies that very small positive RGB values are
     * correctly deserialized.</li>
     * </ul>
     *
     * @throws Exception if an unexpected error occurs during deserialization.
     */
    @Test
    public void testColorDeserialization() throws Exception {
        // Normal test
        json = "{\"red\": 0.5, \"green\": 0.5, \"blue\": 0.5}";
        Color color1 = objectMapper.readValue(json, Color.class);
        assertEquals(Color.color(0.5, 0.5, 0.5), color1);

        // Test with fild missing
        json = "{\"red\": 0.5, \"blue\": 0.5}"; // green is missing
        assertThrows(NullPointerException.class, () -> {
            objectMapper.readValue(json, Color.class);
        });

        // Test with fild invalid
        json = "{\"red\": \"invalid\", \"green\": 0.5, \"blue\": 0.5}";
        Color color2 = objectMapper.readValue(json, Color.class);
        assertEquals(Color.color(0.0, 0.5, 0.5), color2);

        // Test with illegal values
        json = "{\"red\": 1.5, \"green\": -0.5, \"blue\": 2.0}";
        assertThrows(IllegalArgumentException.class, () -> {
            objectMapper.readValue(json, Color.class);
        });

        // Test with fild null
        json = "{\"red\": null, \"green\": 0.5, \"blue\": 0.5}";
        Color color3 = objectMapper.readValue(json, Color.class);
        assertEquals(Color.color(0.0, 0.5, 0.5), color3);

        // Test with nagative value
        json = "{\"red\": -0.1, \"green\": 0.5, \"blue\": 0.5}";
        assertThrows(IllegalArgumentException.class, () -> {
            objectMapper.readValue(json, Color.class);
        });

        // Test with non decimal value
        json = "{\"red\": 0, \"green\": 0, \"blue\": 0}";
        Color color4 = objectMapper.readValue(json, Color.class);
        assertEquals(Color.color(0, 0, 0), color4);

        // Test with maximum values
        json = "{\"red\": 1.0, \"green\": 1.0, \"blue\": 1.0}";
        Color color5 = objectMapper.readValue(json, Color.class);
        assertEquals(Color.color(1.0, 1.0, 1.0), color5);

        // Test with very small value
        json = "{\"red\": 0.0001, \"green\": 0.0001, \"blue\": 0.0001}";
        Color color6 = objectMapper.readValue(json, Color.class);
        assertEquals(Color.color(0.0001, 0.0001, 0.0001), color6);
    }

    /**
     * Tests the
     * {@link ColorDeserializer#deserialize(JsonParser, DeserializationContext)}
     * method
     * directly with mocked {@link JsonParser} and {@link DeserializationContext}
     * instances.
     * This test verifies the deserializer’s behavior when parsing a valid JSON node
     * into a {@link Color} object.
     * 
     * <p>
     * Mocks the {@link JsonParser} and {@link DeserializationContext}, as well as
     * the
     * {@link JsonNode} values for "red," "green," and "blue" channels, to simulate
     * valid color input.
     * Asserts that the deserialized color matches the expected color values.
     * 
     * @throws IOException             if an I/O error occurs during the
     *                                 deserialization process.
     * @throws JsonProcessingException if an error occurs while processing JSON.
     */
    @Test
    public void testDeserializeValidColor() throws IOException, JsonProcessingException {
        // Mock JsonParser and DeserializationContext (assuming a mocking framework is
        // used)
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        // Create a mock JsonNode with color values
        JsonNode node = mock(JsonNode.class);
        when(node.get("red")).thenReturn(mock(JsonNode.class));
        when(node.get("red").doubleValue()).thenReturn(0.5);
        when(node.get("green")).thenReturn(mock(JsonNode.class));
        when(node.get("green").doubleValue()).thenReturn(1.0);
        when(node.get("blue")).thenReturn(mock(JsonNode.class));
        when(node.get("blue").doubleValue()).thenReturn(0.0);
        when(parser.readValueAsTree()).thenReturn(node);

        // Call the deserialize method
        ColorDeserializer deserializer = new ColorDeserializer();
        Color color = deserializer.deserialize(parser, context);

        // Assert the deserialized color is correct
        assertEquals(Color.color(0.5, 1.0, 0.0), color);
    }
}
