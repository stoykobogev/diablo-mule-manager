module org.diablo.manager {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.fasterxml.jackson.databind;

    opens org.diablo.manager to javafx.fxml;
    exports org.diablo.manager;
    exports org.diablo.manager.controllers;
    exports org.diablo.manager.entities;
    opens org.diablo.manager.controllers to javafx.fxml;
}