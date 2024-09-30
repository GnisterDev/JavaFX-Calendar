package calendar.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import calendar.persistence.internal.ColorSerializer;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ColorSerializerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Color.class, new ColorSerializer());
        objectMapper.registerModule(module);
    }

    @Test
    public void testSerializeBasicColor() throws JsonProcessingException {
        Color color = Color.color(0.5, 0.5, 0.5); // Gray
        String json = objectMapper.writeValueAsString(color);

        assertEquals("{\"red\":0.5,\"green\":0.5,\"blue\":0.5}", json);
    }

    @Test
    public void testSerializeExtremeColorValues() throws JsonProcessingException {
        Color color = Color.color(0.0, 1.0, 0.0); // Pure green
        String json = objectMapper.writeValueAsString(color);

        assertEquals("{\"red\":0.0,\"green\":1.0,\"blue\":0.0}", json);
    }

    @Test
    public void testSerializeWhiteColor() throws JsonProcessingException {
        Color color = Color.color(1.0, 1.0, 1.0); // White
        String json = objectMapper.writeValueAsString(color);

        assertEquals("{\"red\":1.0,\"green\":1.0,\"blue\":1.0}", json);
    }

    @Test
    public void testSerializeBlackColor() throws JsonProcessingException {
        Color color = Color.color(0.0, 0.0, 0.0); // Black
        String json = objectMapper.writeValueAsString(color);

        assertEquals("{\"red\":0.0,\"green\":0.0,\"blue\":0.0}", json);
    }
}
