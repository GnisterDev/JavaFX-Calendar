package calendar.persistence;

import java.io.FileOutputStream;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import calendar.types.Event;

public class Persistence {
    public static void write(Event e) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        FileOutputStream fileOutputStream = new FileOutputStream("post.json");
        mapper.writeValue(fileOutputStream, e);
        fileOutputStream.close();
    }

    public static void write(CalendarData c) {

    }
}
