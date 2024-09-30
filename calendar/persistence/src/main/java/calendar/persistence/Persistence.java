package calendar.persistence;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import calendar.types.User;
import calendar.types.UserStore;
import javafx.scene.paint.Color;
import calendar.persistence.internal.ColorDeserializer;
import calendar.persistence.internal.ColorSerializer;
import calendar.persistence.internal.UUIDDeserializer;
import calendar.types.Calendar;
import calendar.types.Event;

public class Persistence {
    public static void write(UserStore object) throws IOException {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Color.class, new ColorSerializer());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        FileOutputStream fileOutputStream = new FileOutputStream("post.json");
        mapper.writeValue(fileOutputStream, object);
        fileOutputStream.close();
    }

    public static <T> T read(Class<T> topClass) throws IOException {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Color.class, new ColorDeserializer());
        module.addDeserializer(UUID.class, new UUIDDeserializer());

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.registerModule(module);

        InputStream fileInputStream = new FileInputStream("post.json");
        T object = mapper.readValue(fileInputStream, topClass);

        return object;
    }

    public static void main(String[] args) {

        ArrayList<Event> e = new ArrayList<>();
        e.add(new Event("title1", "description1", new Date(), new Date()));
        e.add(new Event("title2", "description2", new Date(), new Date()));
        ArrayList<Calendar> cl = new ArrayList<>();
        cl.add(new Calendar(e));
        cl.add(new Calendar(e));

        UUID id = UUID.randomUUID();
        User u = new User(id, "A", "1", cl);

        HashMap<String, UUID> StU = new HashMap<>();
        StU.put("A", id);
        StU.put("B", UUID.randomUUID());
        StU.put("C", UUID.randomUUID());
        StU.put("D", UUID.randomUUID());

        HashMap<UUID, User> UtU = new HashMap<>();
        UtU.put(id, u);
        UtU.put(UUID.randomUUID(), new User("B", "2"));
        UtU.put(UUID.randomUUID(), new User("C", "3"));
        UtU.put(UUID.randomUUID(), new User("D", "4"));

        UserStore userStore = new UserStore(StU, UtU);
        try {
            write(userStore);
            System.out.println(
                    read(UserStore.class)
                            .getUser(id)
                            .get()
                            .getCalendar(1)
                            .getEvent(0)
                            .getStartTime());
        } catch (IOException err) {
            System.out.println(err);
        }
    }
}
