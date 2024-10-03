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

    @Test
    public void testDeserializeValidColor() throws IOException, JsonProcessingException {
        // Mock JsonParser and DeserializationContext (assuming a mocking framework is used)
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
