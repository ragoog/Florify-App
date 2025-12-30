package com.example.florify.ui.main;

import com.example.florify.machineLearningModels.TFLoader;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.Objects;

public class ScanPlant extends Application {

    private ImageView imageView;
    private ProgressIndicator loadingIndicator;
    private Label loadingLabel;
    private StackPane imageContainer;
    private TFLoader modelLoader;
    private VBox uploadSection;
    private Label statusLabel;

    private static final String MODEL_PATH = "C:\\Users\\omaro\\Florify-App\\Florify\\src\\main\\resources\\plant_disease_detector";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Florify - Plant Disease Scanner");

        // Initialize the ML model loader
        modelLoader = TFLoader.getInstance();

        // Load model in background
        new Thread(() -> {
            try {
                modelLoader.loadCNNModel(MODEL_PATH);
                Platform.runLater(() -> updateStatus("✓ CNN Model Ready", "#6B8E4E"));
            } catch (Exception e) {
                Platform.runLater(() -> {
                    updateStatus("⚠ Model Loading Failed", "#cc0000");
                    showError("Failed to load ML model: " + e.getMessage());
                });
            }
        }).start();

        // Main container
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #f8faf9, #e8f5e9);");

        // Top bar
        HBox topBar = createStyledTopBar();
        root.setTop(topBar);

        // Center content
        VBox centerContent = createCenterContent();
        root.setCenter(centerContent);

        Scene scene = new Scene(root, 900, 720);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(e -> modelLoader.close());
    }

    private HBox createStyledTopBar() {
        HBox topBar = new HBox(15);
        topBar.setPrefHeight(70);
        topBar.setPadding(new Insets(15, 30, 15, 30));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);"
        );

        // Icon
        String imgLink = "/com/example/florify/Petite_plante_sur_le_sol_dans_un_style_pixel_art___Vecteur_Premium-removebg-preview.png";
        try {
            Image iconImage = new Image(Objects.requireNonNull(getClass().getResource(imgLink)).toExternalForm());
            ImageView iconView = new ImageView(iconImage);
            iconView.setFitHeight(45);
            iconView.setFitWidth(45);
            topBar.getChildren().add(iconView);
        } catch (Exception ignored) {}

        // Title section
        VBox titleBox = new VBox(2);
        Label title = new Label("Florify Disease Scanner");
        title.setStyle(
                "-fx-font-size: 26px; " +
                        "-fx-font-family: Verdant; " +
                        "-fx-text-fill: #2C4A3E;" +
                        "-fx-font-weight: bold;"
        );

        Label subtitle = new Label("AI-Powered Plant Health Analysis");
        subtitle.setStyle(
                "-fx-font-size: 13px; " +
                        "-fx-font-family: Verdant; " +
                        "-fx-text-fill: #6B8E4E;" +
                        "-fx-font-weight: bold;"
        );

        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Status indicator
        statusLabel = new Label("⟳ Loading Model...");
        statusLabel.setStyle(
                "-fx-font-size: 12px; " +
                        "-fx-font-family: Verdant; " +
                        "-fx-text-fill: #999999;" +
                        "-fx-padding: 8 15 8 15;" +
                        "-fx-background-color: #f5f5f5;" +
                        "-fx-background-radius: 20;"
        );

        topBar.getChildren().addAll(titleBox, spacer, statusLabel);
        return topBar;
    }

    private VBox createCenterContent() {
        VBox content = new VBox(30);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));

        // Welcome card
        VBox welcomeCard = createWelcomeCard();

        // Upload section with image preview
        uploadSection = createUploadSection();

        content.getChildren().addAll(welcomeCard, uploadSection);
        return content;
    }

    private VBox createWelcomeCard() {
        VBox card = new VBox(10);
        card.setMaxWidth(600);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.CENTER);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 16;" +
                        "-fx-effect: dropshadow(gaussian, rgba(107, 142, 78, 0.2), 15, 0, 0, 4);"
        );


        Label title = new Label("Advanced Plant Disease Detection");
        title.setStyle(
                "-fx-font-size: 24px; " +
                        "-fx-font-family: Verdant; " +
                        "-fx-text-fill: #2C4A3E;" +
                        "-fx-font-weight: bold;"
        );

        Label description = new Label(
                "Upload a clear image of your plant's leaves to identify diseases and get treatment recommendations"
        );
        description.setWrapText(true);
        description.setMaxWidth(600);
        description.setAlignment(Pos.CENTER);
        description.setStyle(
                "-fx-font-size: 15px; " +
                        "-fx-font-family: Verdant; " +
                        "-fx-text-fill: #666666;" +
                        "-fx-text-alignment: center;"
        );

        String imgLink = "/com/example/florify/scanner.png";
        Image bg = new Image(Objects.requireNonNull(getClass().getResource(imgLink)).toExternalForm());
        ImageView image = new ImageView(bg);
        image.setPreserveRatio(true);
        image.setFitWidth(60);

        card.getChildren().addAll(image, title, description);
        return card;
    }

    private VBox createUploadSection() {
        VBox section = new VBox(20);
        section.setMaxWidth(700);
        section.setAlignment(Pos.CENTER);

        // Image preview container with rounded corners
        imageContainer = new StackPane();
        imageContainer.setPrefSize(500, 350);
        imageContainer.setMaxSize(500, 350);
        imageContainer.setStyle(
                "-fx-background-color: #fafafa;" +
                        "-fx-border-color: #e0e0e0;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-style: dashed;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;"
        );

        // Image view
        imageView = new ImageView();
        imageView.setFitWidth(480);
        imageView.setFitHeight(330);
        imageView.setPreserveRatio(true);

        Rectangle clip = new Rectangle(480, 330);
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        imageView.setClip(clip);

        // Placeholder
        VBox placeholder = new VBox(15);
        placeholder.setAlignment(Pos.CENTER);
        Label placeholderIcon = new Label("📷");
        placeholderIcon.setStyle("-fx-font-size: 64px;");
        Label placeholderText = new Label("No image selected");
        placeholderText.setStyle(
                "-fx-font-size: 16px; " +
                        "-fx-font-family: Verdant; " +
                        "-fx-text-fill: #999999;"
        );
        placeholder.getChildren().addAll(placeholderIcon, placeholderText);

        // Loading indicator
        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setMaxSize(70, 70);
        loadingIndicator.setStyle("-fx-progress-color: #6B8E4E;");
        loadingIndicator.setVisible(false);

        VBox loadingBox = new VBox(15);
        loadingBox.setAlignment(Pos.CENTER);
        loadingBox.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.95);" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 25;"
        );

        loadingLabel = new Label("Analyzing plant...");
        loadingLabel.setStyle(
                "-fx-font-size: 15px; " +
                        "-fx-text-fill: #2C4A3E; " +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: Verdant;"
        );
        loadingLabel.setVisible(false);

        loadingBox.getChildren().addAll(loadingIndicator, loadingLabel);

        imageContainer.getChildren().addAll(placeholder, imageView, loadingBox);

        // Upload button with enhanced styling
        Button uploadButton = new Button("📁  Select Plant Image");
        uploadButton.setPrefWidth(250);
        uploadButton.setPrefHeight(50);
        uploadButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #6B8E4E, #8BAA6F);" +
                        "-fx-background-radius: 25;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-family: Verdant;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(107, 142, 78, 0.4), 10, 0, 0, 3);"
        );

        uploadButton.setOnMouseEntered(e -> {
            uploadButton.setStyle(
                    "-fx-background-color: linear-gradient(to right, #7BA05A, #9BBB7F);" +
                            "-fx-background-radius: 25;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-family: Verdant;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(107, 142, 78, 0.6), 15, 0, 0, 4);" +
                            "-fx-scale-x: 1.02;" +
                            "-fx-scale-y: 1.02;"
            );
        });

        uploadButton.setOnMouseExited(e -> {
            uploadButton.setStyle(
                    "-fx-background-color: linear-gradient(to right, #6B8E4E, #8BAA6F);" +
                            "-fx-background-radius: 25;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-size: 16px;" +
                            "-fx-font-family: Verdant;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(107, 142, 78, 0.4), 10, 0, 0, 3);"
            );
        });

        uploadButton.setOnAction(e -> uploadImage((Stage) uploadButton.getScene().getWindow()));

        // Tips section
        HBox tipsBox = new HBox(15);
        tipsBox.setAlignment(Pos.CENTER);
        tipsBox.setMaxWidth(650);
        tipsBox.setPadding(new Insets(15));
        tipsBox.setStyle(
                "-fx-background-color: #f0f7ed;" +
                        "-fx-background-radius: 10;"
        );

        Label tipIcon = new Label("💡");
        tipIcon.setStyle("-fx-font-size: 20px;");

        Label tips = new Label("Best Results: Use well-lit photos with clear leaf details");
        tips.setWrapText(true);
        tips.setStyle(
                "-fx-font-size: 13px; " +
                        "-fx-font-family: Verdant; " +
                        "-fx-text-fill: #4A6741;" +
                        "-fx-font-weight: bold;"
        );

        tipsBox.getChildren().addAll(tipIcon, tips);

        section.getChildren().addAll(imageContainer, uploadButton, tipsBox);
        return section;
    }

    private void uploadImage(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Plant Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);

            // Fade in animation
            FadeTransition fade = new FadeTransition(Duration.millis(400), imageView);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();

            if (!modelLoader.isModelLoaded()) {
                showError("Model is still loading. Please wait a moment and try again.");
                return;
            }

            showLoading(true);

            new Thread(() -> {
                try {
                    String result = modelLoader.predictFromImage(selectedFile.getAbsolutePath());
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

        if (show) {
            FadeTransition fade = new FadeTransition(Duration.millis(300), loadingIndicator.getParent());
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
        }
    }

    private void showResult(String result) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Disease Detection Result");
        alert.setHeaderText(null);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 12;"
        );

        // Custom content
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);

        Label icon = new Label("🔬");
        icon.setStyle("-fx-font-size: 48px;");

        Label titleLabel = new Label("Analysis Complete");
        titleLabel.setStyle(
                "-fx-font-size: 20px; " +
                        "-fx-font-family: Verdant; " +
                        "-fx-text-fill: #2C4A3E;" +
                        "-fx-font-weight: bold;"
        );

        Label resultLabel = new Label(result);
        resultLabel.setWrapText(true);
        resultLabel.setMaxWidth(400);
        resultLabel.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-font-family: Verdant; " +
                        "-fx-text-fill: #333333;" +
                        "-fx-text-alignment: center;"
        );
        resultLabel.setAlignment(Pos.CENTER);

        content.getChildren().addAll(icon, titleLabel, resultLabel);
        dialogPane.setContent(content);

        dialogPane.lookupButton(ButtonType.OK).setStyle(
                "-fx-background-color: linear-gradient(to right, #6B8E4E, #8BAA6F);" +
                        "-fx-background-radius: 20;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-family: Verdant;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 10 25 10 25;"
        );

        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.getDialogPane().lookupButton(ButtonType.OK).setStyle(
                "-fx-background-color: #cc0000;" +
                        "-fx-background-radius: 20;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-family: Verdant;" +
                        "-fx-padding: 10 25 10 25;"
        );

        alert.showAndWait();
    }

    private void updateStatus(String text, String color) {
        statusLabel.setText(text);
        statusLabel.setStyle(
                "-fx-font-size: 12px; " +
                        "-fx-font-family: Verdant; " +
                        "-fx-text-fill: " + color + ";" +
                        "-fx-padding: 8 15 8 15;" +
                        "-fx-background-color: " + (color.equals("#6B8E4E") ? "#e8f5e9" : "#ffe6e6") + ";" +
                        "-fx-background-radius: 20;" +
                        "-fx-font-weight: bold;"
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}