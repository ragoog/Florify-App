module com.example.florify {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires jdk.xml.dom;
    requires javafx.graphics;
    requires java.sql;
    requires annotations;
    requires java.desktop;
    requires java.net.http;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires org.json;
//    requires com.example.florify;
    opens com.example.florify.ui.main to javafx.graphics;
    exports com.example.florify;
    opens com.example.florify to javafx.fxml;
//    exports com.example.florify.ui;
//    opens com.example.florify.ui to javafx.fxml;

    exports com.example.florify.ui.login;
    opens com.example.florify.ui.login to javafx.fxml;
    exports com.example.florify.ui.feed;
    opens com.example.florify.ui.feed to javafx.fxml;
    exports com.example.florify.ui.navigation;
    opens com.example.florify.ui.navigation to javafx.fxml;
}