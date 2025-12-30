package com.example.florify.ui.main;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ScanPlant extends Application {

    private ImageView imageView;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Image Uploader");

        imageView = new ImageView();
        imageView.setFitWidth(400);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);

        // Create Upload Button
        Button uploadButton = new Button("Upload Image");
        uploadButton.setStyle("""
    -fx-background-color: linear-gradient(to right, #6B8E4E, #A0C48C);
    -fx-background-radius: 12;
    -fx-border-radius: 12;
    -fx-text-fill: white;
    -fx-font-weight: bold;
    -fx-font-size: 16;
    -fx-font-family: Verdant;
    -fx-cursor: hand;
""");
        uploadButton.setOnAction(e -> uploadImage(primaryStage));

        // Layout
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(uploadButton, imageView);

        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void uploadImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");

        // Set file extension filters
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            // Load and display the image
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);

            // Show alert message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setGraphic(null);

            //image here
            alert.setContentText("");

            // Style the OK button
            alert.getDialogPane().lookupButton(ButtonType.OK).setStyle("""
                -fx-background-color: linear-gradient(to right, #6B8E4E, #A0C48C);
                -fx-background-radius: 12;
                -fx-border-radius: 12;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-font-size: 16;
                -fx-font-family: Verdant;
                -fx-cursor: hand;
            """);

            alert.showAndWait();
        }
    }

    private DropShadow addGlowEffect() {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#6B8E4E"));
        glow.setRadius(3.5);
        glow.setSpread(0.15);
        return glow;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
