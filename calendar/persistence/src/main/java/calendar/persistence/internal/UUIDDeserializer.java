package calendar.persistence.internal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.UUID;

/**
 * A custom deserializer for the {@link UUID} class, enabling the
 * deserialization of a JSON string into a {@link UUID} instance.
 * <p>
 * This deserializer expects a JSON string that represents a valid UUID format.
 * </p>
 * This will deserialize into a {@code UUID} instance with the specified value.
 */
public class UUIDDeserializer extends JsonDeserializer<UUID> {

    /**
     * Deserializes JSON content into a {@link UUID} object.
     *
     * @param  p           the JSON parser used to read JSON content
     * @param  ctxt        the deserialization context
     * @return             a {@link UUID} instance created from the JSON string
     * @throws IOException if an error occurs during parsing
     */
    @Override
    public UUID deserialize(final JsonParser p,
            final DeserializationContext ctxt) throws IOException {
        String uuidString = p.getText();
        return UUID.fromString(uuidString);
    }
}
