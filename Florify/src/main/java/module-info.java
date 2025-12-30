module com.example.florify {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    requires java.sql;
    requires jdk.xml.dom;

    // Jackson
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    // Fixes for your errors
    requires org.json;           // JSON library
    requires java.net.http;      // HTTP client
    requires java.desktop;
    requires annotations;
    requires pmml.evaluator;
    requires pmml.model;
    requires org.tensorflow;
    requires jdk.compiler;       // AWT / java.datatransfer

    exports com.example.florify;
    opens com.example.florify to javafx.fxml, com.fasterxml.jackson.databind;

    exports com.example.florify.ui.login;
    opens com.example.florify.ui.login to javafx.fxml;

    exports com.example.florify.ui.feed;
    opens com.example.florify.ui.feed to javafx.fxml;

    exports com.example.florify.ui.navigation;
    opens com.example.florify.ui.navigation to javafx.fxml;

    opens com.example.florify.ui.main to javafx.graphics;
    exports com.example.florify.machineLearningModels;
    opens com.example.florify.machineLearningModels to com.fasterxml.jackson.databind, javafx.fxml;
}
