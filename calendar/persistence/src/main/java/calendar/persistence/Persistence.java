package calendar.persistence;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        SimpleModule module = new SimpleModule();
        module.addSerializer(Color.class, new ColorSerializer());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(module);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        FileOutputStream fileOutputStream = new FileOutputStream(filepath);
        mapper.writeValue(fileOutputStream, object);
        fileOutputStream.close();
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
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Color.class, new ColorDeserializer());
        module.addDeserializer(UUID.class, new UUIDDeserializer());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.registerModule(module);

        InputStream fileInputStream = new FileInputStream(filepath);
        T object = mapper.readValue(fileInputStream, objectType);

        return object;
    }
}
