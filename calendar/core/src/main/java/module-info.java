module calendar.core {
    requires transitive javafx.fxml;
    requires transitive calendar.types;
    requires calendar.persistence;
    requires java.net.http;
    requires transitive no.gorandalum.fluentresult;

    exports calendar.core;
}
