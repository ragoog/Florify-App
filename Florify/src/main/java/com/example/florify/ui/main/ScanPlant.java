package com.example.florify.ui.main;

import com.example.florify.machineLearningModels.TFLoader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class ScanPlant extends Application {

    private ImageView imageView;
    private ProgressIndicator loadingIndicator;
    private Label loadingLabel;
    private StackPane imageContainer;
    private TFLoader modelLoader;

    // Update this path to your model location
    private static final String MODEL_PATH = "C:\\Users\\omaro\\Florify-App\\Florify\\src\\main\\resources\\plant_disease_detector";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Plant Disease Scanner");

        // Initialize the ML model loader (singleton)
        modelLoader = TFLoader.getInstance();

        // Load model in background thread
        new Thread(() -> {
            try {
                modelLoader.loadCNNModel(MODEL_PATH);
            } catch (Exception e) {
                Platform.runLater(() -> showError("Failed to load ML model: " + e.getMessage()));
            }
        }).start();

        imageView = new ImageView();
        imageView.setFitWidth(400);
        imageView.setFitHeight(300);
        imageView.setPreserveRatio(true);


        // HIDE THESE BY DEFAULT AND ONLY SHOW THEM WHEN NEEDED
        // Create loading indicator
        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setMaxSize(60, 60);
        loadingIndicator.setVisible(false);

        loadingLabel = new Label("Analyzing plant...");
        loadingLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #6B8E4E; -fx-font-weight: bold;");
        loadingLabel.setVisible(false);

        // Container for image and loading overlay
        imageContainer = new StackPane();
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.getChildren().addAll(imageView, loadingIndicator);

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
        layout.getChildren().addAll(uploadButton, imageContainer, loadingLabel);

        Scene scene = new Scene(layout, 500, 450);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Close model when app closes
        primaryStage.setOnCloseRequest(e -> modelLoader.close());
    }

    private void uploadImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Plant Image");

        // Set file extension filters
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            // Load and display the image
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);

            // Check if model is loaded
            if (!modelLoader.isModelLoaded()) {
                showError("Model is still loading. Please wait a moment and try again.");
                return;
            }

            // Show loading indicator
            showLoading(true);

            // Run prediction in background thread
            new Thread(() -> {
                try {
                    String result = modelLoader.predictFromImage(selectedFile.getAbsolutePath());

                    // Update UI on JavaFX thread
                    Platform.runLater(() -> {
                        showLoading(false);
                        showResult(result);
                    });

                } catch (Exception e) {
                    Platform.runLater(() -> {
                        showLoading(false);
                        showError("Prediction failed: " + e.getMessage());
                    });
                }
            }).start();
        }
    }

    private void showLoading(boolean show) {
        loadingIndicator.setVisible(show);
        loadingLabel.setVisible(show);
    }

    private void showResult(String result) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Scan Result");
        alert.setHeaderText("Plant Disease Detection");
        alert.setContentText(result);
        alert.setGraphic(null);

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

    private static Label getLabel(String tipOfTheDay) {
        Label tipLabel = new Label(tipOfTheDay);
        tipLabel.setEffect(new DropShadow(5, Color.rgb(0,0,0,0.5)));
        tipLabel.setStyle(
                "-fx-font-family: Verdant;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: linear-gradient(to bottom, #3C5148, #6B8E4E);" +
                        "-fx-fill: #3C5148;" +
                        "-fx-stroke: #1B2727;" +
                        "-fx-stroke-width: 0.5;"
        );

        String fullText = tipLabel.getText();
        tipLabel.setText("");

        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        final int[] idx = {0};           // current character index
        final int[] dir = {1};           // 1 = forward, -1 = backward
        final int[] pauseCounter = {0};
        int pauseDuration = 40;   // number of frames to wait at full/empty 50ms * 40 = 2 seconds

        KeyFrame kf = new KeyFrame(Duration.millis(50), e -> {
            // eli gowa el lamda expression must be 1) final 2) array

            // if it is below 0 keep it at 0 AKA CLAMPING
            if(idx[0] < 0) idx[0] = 0;
            if(dir[0] > fullText.length()) idx[0] = fullText.length();

            // If we are at the end or start, increment pause counter
            if ((idx[0] == fullText.length() && dir[0] == 1) || (idx[0] == 0 && dir[0] == -1)) {
                // delete the first letter too
                if(idx[0] == 0) tipLabel.setText("");

                if (pauseCounter[0] < pauseDuration) {
                    pauseCounter[0]++;
                    return; // do nothing, just wait
                } else {
                    pauseCounter[0] = 0;
                    dir[0] *= -1; // reverse direction
                }
            }

            tipLabel.setText(fullText.substring(0, idx[0]));
            idx[0] += dir[0];
        });

        timeline.getKeyFrames().add(kf);
        timeline.play();

        tipLabel.setWrapText(true); // so the tip wraps if too long
        return tipLabel;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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