package calendar.persistence;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javafx.scene.paint.Color;
import calendar.persistence.internal.ColorDeserializer;
import calendar.persistence.internal.ColorSerializer;
import calendar.persistence.internal.UUIDDeserializer;

public final class Persistence {
    private Persistence() {

    }

    /** The default filepath for the savefile to be saved at. */
    public static final String DEAFULT_FILE_PATH = "./userdata.json";

    /**
     * Serializes the given object to a JSON file at the {@code default} file
     * path.
     *
     * <p>
     * This method uses the Jackson library to serialize the provided object
     * into a JSON format and writes it to the file specified by the filepath.
     * </p>
     *
     * The file will be overwritten if it already exists.
     *
     * @param  <T>         the type of the object to be serialized
     * @param  object      the object to be serialized and written to the file
     * @throws IOException if there is an error writing to the file
     */
    public static <T> void write(final T object) throws IOException {
        Persistence.write(object, Persistence.DEAFULT_FILE_PATH);
    }

    /**
     * Serializes the given object to a JSON file at the {@code given} file
     * path.
     *
     * <p>
     * This method uses the Jackson library to serialize the provided object
     * into a JSON format and writes it to the file specified by the filepath.
     * </p>
     *
     * The file will be overwritten if it already exists.
     *
     * @param  <T>         the type of the object to be serialized
     * @param  object      the object to be serialized and written to the file
     * @param  filepath    the path of the file where the JSON representation of
     *                     the object will be written
     * @throws IOException if there is an error writing to the file
     */
    public static <T> void write(final T object, final String filepath)
            throws IOException {
        Files.writeString(Path.of(filepath), toJSON(object));
    }

    /**
     * Serializes an object to a JSON-formatted string with specific
     * serialization configurations.
     * <p>
     * This method uses the Jackson library to convert an object of type
     * {@code T} to a JSON string. It registers custom serializers, including a
     * {@link ColorSerializer} for {@link Color} objects and a
     * {@link JavaTimeModule} for Java 8+ date/time objects, enabling a more
     * specialized serialization process. The JSON output is also formatted with
     * indentation for readability.
     * </p>
     *
     * @param  <T>         the type of the object to be serialized
     * @param  object      the object to serialize to JSON
     * @return             a JSON-formatted string representing the serialized
     *                     object
     * @throws IOException if an I/O error occurs during serialization
     */
    public static <T> String toJSON(final T object) throws IOException {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Color.class, new ColorSerializer());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(module);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        OutputStream outputStream = new ByteArrayOutputStream();
        mapper.writeValue(outputStream, object);
        return outputStream.toString();
    }

    /**
     * Deserializes a JSON file into an object of the specified type.
     *
     * <p>
     * This method reads a JSON file from the {@code default} file path and
     * deserializes it into an object of the specified class type. It uses the
     * Jackson library to handle the deserialization.
     * </p>
     *
     * <p>
     * The method expects the file to contain a valid JSON representation of the
     * object type being deserialized.
     * </p>
     *
     * @param  <T>         the type of the object to be deserialized
     * @param  objectType  the class of the object to be deserialized from the
     *                     file
     * @return             the deserialized object of type {@code T}
     * @throws IOException if there is an error reading from the file or
     *                     deserializing the JSON content
     */
    public static <T> T read(final Class<T> objectType) throws IOException {
        return Persistence.read(objectType, Persistence.DEAFULT_FILE_PATH);
    }

    /**
     * Deserializes a JSON file into an object of the specified type.
     *
     * <p>
     * This method reads a JSON file from the {@code given} file path and
     * deserializes it into an object of the specified class type. It uses the
     * Jackson library to handle the deserialization.
     * </p>
     *
     * <p>
     * The method expects the file to contain a valid JSON representation of the
     * object type being deserialized.
     * </p>
     *
     * @param  <T>         the type of the object to be deserialized
     * @param  objectType  the class of the object to be deserialized from the
     *                     file
     * @param  filepath    the path of the JSON file to be read and deserialized
     * @return             the deserialized object of type {@code T}
     * @throws IOException if there is an error reading from the file or
     *                     deserializing the JSON content
     */
    public static <T> T read(final Class<T> objectType, final String filepath)
            throws IOException {
        return fromJSON(objectType, Files.readString(Path.of(filepath)));
    }

    /**
     * Deserializes a JSON-formatted string into an object of the specified
     * type.
     * <p>
     * This method uses the Jackson library to convert a JSON string into an
     * instance of the given class type {@code T}. It registers custom
     * deserializers, including a {@link ColorDeserializer} for {@link Color}
     * objects and a {@link UUIDDeserializer} for {@link UUID} objects, to
     * handle specialized deserialization requirements. Additionally, a
     * {@link JavaTimeModule} is registered to support Java 8+ date/time
     * classes.
     * </p>
     *
     * @param  <T>         the type of the object to be deserialized
     * @param  objectType  the {@link Class} of the object to deserialize to
     * @param  json        the JSON string to deserialize
     * @return             an instance of {@code T} populated with data from the
     *                     JSON string
     * @throws IOException if an I/O error occurs during deserialization or if
     *                     the JSON is invalid
     */
    public static <T> T fromJSON(final Class<T> objectType, final String json)
            throws IOException {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Color.class, new ColorDeserializer());
        module.addDeserializer(UUID.class, new UUIDDeserializer());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.registerModule(module);

        T object = mapper.readValue(json, objectType);

        return object;
    }
}
