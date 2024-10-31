module calendar.ui {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;

    requires calendar.types;
    requires calendar.core;
    requires javafx.graphics;

    requires org.apache.commons.lang3;
    requires org.controlsfx.controls;

    opens calendar.ui to javafx.graphics, javafx.fxml;
}
