package calendar.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import calendar.persistence.internal.UUIDDeserializer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UUIDDeserializerTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(UUID.class, new UUIDDeserializer());
        objectMapper.registerModule(module);
    }

    @Test
    public void testValidUUIDDeserialization() throws IOException {
        String json = "\"123e4567-e89b-12d3-a456-426614174000\""; // Valid UUID

        UUID uuid = objectMapper.readValue(json, UUID.class);

        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), uuid);
    }

    @Test
    public void testInvalidUUIDDeserialization() {
        String json = "\"invalid-uuid\""; // Invalid UUID format

        assertThrows(IllegalArgumentException.class, () -> {
            objectMapper.readValue(json, UUID.class);
        });
    }

    @Test
    public void testNullUUIDDeserialization() throws JsonProcessingException {
        String json = "null"; // Null UUID

        UUID uuid = objectMapper.readValue(json, UUID.class);

        assertNull(uuid); // Should be null
    }

    @Test
    public void testEmptyStringUUIDDeserialization() {
        String json = "\"\""; // Empty string

        assertThrows(IllegalArgumentException.class, () -> {
            objectMapper.readValue(json, UUID.class);
        });
    }
}
