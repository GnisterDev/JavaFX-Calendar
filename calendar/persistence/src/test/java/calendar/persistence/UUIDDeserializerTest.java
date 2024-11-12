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

/**
 * Test class for {@link UUIDDeserializer} ensuring correct deserialization of
 * UUIDs.
 */
public class UUIDDeserializerTest {

    private ObjectMapper objectMapper;

    /**
     * Sets up the test environment by initializing the {@link ObjectMapper} and
     * registering the {@link UUIDDeserializer}.
     * This method is called before each test to ensure the proper setup of the
     * object mapper.
     */
    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(UUID.class, new UUIDDeserializer());
        objectMapper.registerModule(module);
    }

    /**
     * Tests the deserialization of a valid UUID string into a {@link UUID} object.
     * Verifies that the correct UUID is produced from a valid JSON string.
     * 
     * @throws IOException if the JSON string cannot be properly parsed into a
     *                     {@link UUID},
     *                     or if there is any issue during the deserialization
     *                     process.
     */
    @Test
    public void testValidUUIDDeserialization() throws IOException {
        String json = "\"123e4567-e89b-12d3-a456-426614174000\""; // Valid UUID

        UUID uuid = objectMapper.readValue(json, UUID.class);

        assertEquals(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), uuid);
    }

    /**
     * Tests deserialization of an invalid UUID string.
     * 
     * @throws IllegalArgumentException if an invalid UUID format is encountered.
     */
    @Test
    public void testInvalidUUIDDeserialization() {
        String json = "\"invalid-uuid\""; // Invalid UUID format

        assertThrows(IllegalArgumentException.class, () -> {
            objectMapper.readValue(json, UUID.class);
        });
    }

    /**
     * Tests deserialization of a null UUID string.
     * Verifies that the deserialization of a null value results in a {@code null}
     * {@link UUID}.
     * 
     * @throws JsonProcessingException if the JSON input cannot be processed due to
     *                                 an invalid format or other issues during
     *                                 deserialization, even when the value is null.
     */
    @Test
    public void testNullUUIDDeserialization() throws JsonProcessingException {
        String json = "null"; // Null UUID

        UUID uuid = objectMapper.readValue(json, UUID.class);

        assertNull(uuid); // Should be null
    }

    /**
     * Tests deserialization of an empty string as a UUID.
     * 
     * @throws IllegalArgumentException if an empty string is provided.
     */
    @Test
    public void testEmptyStringUUIDDeserialization() {
        String json = "\"\""; // Empty string

        assertThrows(IllegalArgumentException.class, () -> {
            objectMapper.readValue(json, UUID.class);
        });
    }
}
