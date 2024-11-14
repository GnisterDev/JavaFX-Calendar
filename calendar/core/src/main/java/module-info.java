module calendar.core {
    requires transitive javafx.fxml;
    requires transitive calendar.types;
    requires calendar.persistence;

    exports calendar.core;
}
