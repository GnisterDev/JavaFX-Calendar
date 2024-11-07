package calendar.persistence.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import javafx.scene.paint.Color;

import java.io.IOException;

/**
 * A custom deserializer for the {@link Color} class, enabling deserialization
 * of a JSON object representing RGB color values into a {@link Color} instance.
 * <p>
 * This deserializer expects a JSON structure with "red", "green", and "blue"
 * fields, each representing a color channel's intensity as a double between 0.0
 * and 1.0.
 * </p>
 *
 * This will deserialize into a {@code Color} instance with the specified RGB
 * values.
 */
public class ColorDeserializer extends JsonDeserializer<Color> {

    /**
     * Deserializes JSON content into a {@link Color} object.
     *
     * @param  parser                  the JSON parser
     * @param  ctxt                    the deserialization context
     * @return                         a {@link Color} instance with RGB values
     *                                 extracted from the JSON input
     * @throws IOException             if an error occurs during parsing
     * @throws JsonProcessingException if the JSON content is malformed or
     *                                 incompatible
     */
    @Override
    public Color deserialize(final JsonParser parser,
            final DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = parser.readValueAsTree();
        Double r = (double) node.get("red").doubleValue();
        Double g = (double) node.get("green").doubleValue();
        Double b = (double) node.get("blue").doubleValue();

        return Color.color(r, g, b);
    }
}
