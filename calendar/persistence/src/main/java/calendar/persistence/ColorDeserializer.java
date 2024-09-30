package calendar.persistence;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import javafx.scene.paint.Color;

import java.io.IOException;

public class ColorDeserializer extends JsonDeserializer<Color> {

    @Override
    public Color deserialize(JsonParser parser, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = parser.getCodec().readTree(parser);
        Double r = (double) node.get("red").doubleValue();
        Double g = (double) node.get("green").doubleValue();
        Double b = (double) node.get("blue").doubleValue();

        return Color.color(r, g, b);
    }
}
