package calendar.persistence.internal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import javafx.scene.paint.Color;

import java.io.IOException;

/**
 * A custom serializer for the {@link Color} class, enabling serialization of a
 * {@link Color} instance to a JSON object representing RGB color values.
 * <p>
 * This serializer outputs a JSON object with "red", "green", and "blue" fields,
 * each representing the intensity of a color channel as a double between 0.0
 * and 1.0.
 * </p>
 */
public class ColorSerializer extends JsonSerializer<Color> {

    /**
     * Serializes a {@link Color} object into JSON format.
     * <p>
     * The resulting JSON object contains "red", "green", and "blue" fields,
     * each corresponding to the RGB color channel values of the {@code Color}
     * instance.
     * </p>
     *
     * @param  color       the {@link Color} instance to serialize
     * @param  jsonGen     the JSON generator used to write JSON content
     * @param  provider    the serializer provider
     * @throws IOException if an error occurs during writing to the JSON
     *                     generator
     */
    @Override
    public void serialize(final Color color,
            final JsonGenerator jsonGen,
            final SerializerProvider provider) throws IOException {
        jsonGen.writeStartObject();
        jsonGen.writeNumberField("red", color.getRed());
        jsonGen.writeNumberField("green", color.getGreen());
        jsonGen.writeNumberField("blue", color.getBlue());
        jsonGen.writeEndObject();
    }
}
