package calendar.persistence;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import javafx.scene.paint.Color;
import calendar.persistence.internal.ColorDeserializer;
import calendar.persistence.internal.ColorSerializer;
import calendar.persistence.internal.UUIDDeserializer;

public class Persistence {
    public static String DEAFULT_FILE_PATH = "post.json";

    public static <T> void write(T object) throws IOException {
        Persistence.write(object, Persistence.DEAFULT_FILE_PATH);
    }

    public static <T> void write(T object, String filepath) throws IOException {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Color.class, new ColorSerializer());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        FileOutputStream fileOutputStream = new FileOutputStream(filepath);
        mapper.writeValue(fileOutputStream, object);
        fileOutputStream.close();
    }

    public static <T> T read(Class<T> objectType) throws IOException {
        return Persistence.read(objectType, Persistence.DEAFULT_FILE_PATH);
    }

    public static <T> T read(Class<T> objectType, String filepath) throws IOException {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Color.class, new ColorDeserializer());
        module.addDeserializer(UUID.class, new UUIDDeserializer());

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.registerModule(module);

        InputStream fileInputStream = new FileInputStream(filepath);
        T object = mapper.readValue(fileInputStream, objectType);

        return object;
    }
}
