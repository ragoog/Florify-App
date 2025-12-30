package com.example.florify.ui.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.security.PrivateKey;

public class PlantSurveyView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        Label title = new Label("Enter Your Plant Data");
        title.setStyle("-fx-text-fill: white ");

        TextField textField = createTextField("Soil Moisture:");

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(title, textField);

        Scene scene = new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    // creates a customized Text field
    private TextField createTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-color: rgba(255, 255, 255, 0.9); " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: #C8D9C8; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-width: 2; " +
                        "-fx-padding: 12;"
        );
        field.setPrefHeight(45);

        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(
                        "-fx-font-family: 'Verdant'; " +
                                "-fx-font-size: 14px; " +
                                "-fx-background-color: rgba(255, 255, 255, 0.95); " +
                                "-fx-background-radius: 10; " +
                                "-fx-border-color: #6B8E4E; " +
                                "-fx-border-radius: 10; " +
                                "-fx-border-width: 2; " +
                                "-fx-padding: 12;"
                );
            } else {
                field.setStyle(
                        "-fx-font-family: 'Verdant'; " +
                                "-fx-font-size: 14px; " +
                                "-fx-background-color: rgba(255, 255, 255, 0.9); " +
                                "-fx-background-radius: 10; " +
                                "-fx-border-color: #C8D9C8; " +
                                "-fx-border-radius: 10; " +
                                "-fx-border-width: 2; " +
                                "-fx-padding: 12;"
                );
            }
        });

        return field;
    }

    private Button createButton(String prompt)
    {
        Button button = new Button(prompt);
        button.setStyle("");
        return  button;
    }

}
