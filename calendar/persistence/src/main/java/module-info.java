module calendar.persistence {
    requires transitive calendar.types;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    exports calendar.persistence;
}
