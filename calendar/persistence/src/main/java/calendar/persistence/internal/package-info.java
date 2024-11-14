/**
 * Provides the serializers and deserializes that are used by the persistance
 * class to serialize and deserialize the info contained in the different
 * types-classes correctly.
 *
 * <h2>Contains</h2>
 *
 * <ul>
 * <li>{@code ColorDeserializer} - A utility class used to convert color data
 * from JSON format into a corresponding Java `Color` object during
 * deserialization. This is useful when JSON data includes color information
 * that needs to be mapped to a specific `Color` type in Java.</li>
 * <li>{@code ColorSerializer} - A utility class responsible for serializing a
 * `Color` object into JSON format. This allows a `Color` object to be converted
 * to a JSON-compatible format, such as a hexadecimal color code or an RGB
 * value, when preparing data for output as JSON.</li>
 * <li>{@code UUIDDeserializer} - A utility class used to deserialize a UUID
 * from JSON format into a Java `UUID` object. This helps interpret UUID strings
 * in JSON as `UUID` objects, enabling accurate handling of UUID values within
 * Java.</li>
 * </ul>
 *
 * @since   1.0
 * @version 1.0
 */
package calendar.persistence.internal;
