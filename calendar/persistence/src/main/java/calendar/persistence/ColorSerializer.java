package calendar.persistence;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import javafx.scene.paint.Color;

import java.io.IOException;

public class ColorSerializer extends JsonSerializer<Color> {
    @Override
    public void serialize(Color color, JsonGenerator jsonGen, SerializerProvider provider) throws IOException {
        jsonGen.writeStartObject();
        jsonGen.writeNumberField("red", color.getRed());
        jsonGen.writeNumberField("green", color.getGreen());
        jsonGen.writeNumberField("blue", color.getBlue());
        jsonGen.writeEndObject();
    }
}
