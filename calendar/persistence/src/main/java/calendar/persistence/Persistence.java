package calendar.persistence;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import calendar.types.UserStore;

public class Persistence {
    public static void write(UserStore object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        FileOutputStream fileOutputStream = new FileOutputStream("post.json");
        mapper.writeValue(fileOutputStream, object);
        fileOutputStream.close();
    }

    public static <T> T read(Class<T> topClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        InputStream fileInputStream = new FileInputStream("post.json");
        T object = mapper.readValue(fileInputStream, topClass);

        return object;
    }

    public static void main(String[] args) {
        HashMap<String, UUID> StU = new HashMap<>();
        StU.put("Hello", UUID.randomUUID());

        UserStore userStore = new UserStore(StU, new HashMap<>());
        try {
            write(userStore);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
