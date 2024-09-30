module calendar.persistence {
    requires transitive calendar.types;
    requires transitive com.fasterxml.jackson.annotation;
    requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.databind;

    exports calendar.persistence;
}
