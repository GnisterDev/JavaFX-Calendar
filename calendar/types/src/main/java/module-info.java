module calendar.types {
    opens calendar.types to com.fasterxml.jackson.databind;

    requires transitive javafx.graphics;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    exports calendar.types;
}
