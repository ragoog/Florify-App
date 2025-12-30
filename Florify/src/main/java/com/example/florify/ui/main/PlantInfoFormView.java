package com.example.florify.ui.main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class PlantInfoFormView extends Application {

    private String selectedImagePath = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        VBox root = createStyledForm();

        Scene scene = new Scene(root, 900, 750);
        primaryStage.setTitle("Add New Plant");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createStyledForm() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));

        // Background
        root.setStyle("--fxbackground-color: rgba(0, 0, 0, .5);");

        // Main form container with glassmorphism
        StackPane formContainer = createGlassmorphicContainer();

        VBox formContent = new VBox(25);
        formContent.setAlignment(Pos.TOP_CENTER);
        formContent.setStyle("-fx-background-color: transparent;");
        formContent.setPadding(new Insets(40, 50, 40, 50));

        // Title
        Label title = new Label("🌿 Add New Plant");
        title.setStyle("-fx-font-family: 'Verdant'; -fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #1B2727;");
        title.setEffect(new DropShadow(3, Color.rgb(0, 0, 0, 0.3)));

        // Plant Name Section
        VBox nameSection = createInputSection("Plant Name", createTextField("e.g., Monstera Deliciosa"));

        // Image Upload Section
        VBox imageSection = createImageUploadSection();

        // Environmental Data Grid
        GridPane envGrid = createEnvironmentalGrid();

        // Care Information
        VBox careSection = createCareSection();

        // Buttons
        HBox buttonBox = createButtonBox(formContainer);

        formContent.getChildren().addAll(title, nameSection, imageSection, envGrid, careSection, buttonBox);

        // Add form content to glassmorphic container
        formContainer.getChildren().add(formContent);

        ScrollPane scrollPane = new ScrollPane(formContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPannable(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        root.getChildren().add(scrollPane);
        return root;
    }

    private StackPane createGlassmorphicContainer() {
        StackPane container = new StackPane();
        container.setMaxWidth(800);

        // Blurred background
        Pane blurredBg = new Pane();
        blurredBg.setStyle("-fx-background-color: rgba(255, 255, 255, 0.75); -fx-background-radius: 25;");
        blurredBg.setEffect(new GaussianBlur(20));

        // Border
        Pane border = new Pane();
        border.setStyle("-fx-border-color: rgba(200, 217, 200, 0.8); -fx-border-radius: 25; -fx-border-width: 2; -fx-background-color: transparent;");
        border.setMouseTransparent(true);

        blurredBg.prefWidthProperty().bind(container.widthProperty());
        blurredBg.prefHeightProperty().bind(container.heightProperty());
        border.prefWidthProperty().bind(container.widthProperty());
        border.prefHeightProperty().bind(container.heightProperty());

        container.getChildren().addAll(blurredBg, border);
        return container;
    }

    private VBox createInputSection(String labelText, Control inputControl) {
        VBox section = new VBox(8);

        Label label = new Label(labelText);
        label.setStyle("-fx-font-family: 'Verdant'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2D5F3F;");

        section.getChildren().addAll(label, inputControl);
        return section;
    }

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

    private VBox createImageUploadSection() {
        VBox section = new VBox(12);

        Label label = new Label("📸 Plant Photo");
        label.setStyle("-fx-font-family: 'Verdant'; -fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2D5F3F;");

        Button uploadBtn = new Button("Choose Image");
        uploadBtn.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #6B8E4E; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 10 30; " +
                        "-fx-cursor: hand;"
        );

        Label fileLabel = new Label("No image selected");
        fileLabel.setStyle("-fx-font-family: 'Verdant'; -fx-font-size: 13px; -fx-text-fill: #666666;");

        uploadBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Plant Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File file = fileChooser.showOpenDialog(uploadBtn.getScene().getWindow());
            if (file != null) {
                selectedImagePath = file.getAbsolutePath();
                fileLabel.setText("✓ " + file.getName());
                fileLabel.setStyle("-fx-font-family: 'Verdant'; -fx-font-size: 13px; -fx-text-fill: #4A7C59;");
            }
        });

        uploadBtn.setOnMouseEntered(e -> uploadBtn.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #5A7E3E; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 10 30; " +
                        "-fx-cursor: hand;"
        ));

        uploadBtn.setOnMouseExited(e -> uploadBtn.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #6B8E4E; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-padding: 10 30; " +
                        "-fx-cursor: hand;"
        ));

        section.getChildren().addAll(label, uploadBtn, fileLabel);
        return section;
    }

    private GridPane createEnvironmentalGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);

        Label sectionTitle = new Label("🌡️ Environmental Data");
        sectionTitle.setStyle("-fx-font-family: 'Verdant'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2D5F3F;");
        GridPane.setColumnSpan(sectionTitle, 2);
        grid.add(sectionTitle, 0, 0);

        // Soil Moisture
        grid.add(createMetricCard("💧", "Soil Moisture", "%", "0-100"), 0, 1);

        // Temperature
        grid.add(createMetricCard("🌡️", "Temperature", "°C", "15-35"), 1, 1);

        // Humidity
        grid.add(createMetricCard("💨", "Humidity", "%", "40-80"), 0, 2);

        // Light Hours
        grid.add(createMetricCard("☀️", "Light Hours", "hrs/day", "4-16"), 1, 2);

        return grid;
    }

    private VBox createMetricCard(String icon, String metricName, String unit, String range) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(15));
        card.setStyle(
                "-fx-background-color: rgba(245, 249, 244, 0.8); " +
                        "-fx-background-radius: 12; " +
                        "-fx-border-color: #C8D9C8; " +
                        "-fx-border-radius: 12; " +
                        "-fx-border-width: 1.5;"
        );
        card.setPrefWidth(320);

        HBox header = new HBox(8);
        header.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 20px;");

        Label nameLabel = new Label(metricName);
        nameLabel.setStyle("-fx-font-family: 'Verdant'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3C5148;");

        header.getChildren().addAll(iconLabel, nameLabel);

        HBox inputBox = new HBox(8);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        TextField valueField = new TextField();
        valueField.setPromptText("Enter value");
        valueField.setPrefWidth(120);
        valueField.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-color: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-border-color: #C8D9C8; " +
                        "-fx-border-radius: 8; " +
                        "-fx-padding: 8;"
        );

        Label unitLabel = new Label(unit);
        unitLabel.setStyle("-fx-font-family: 'Verdant'; -fx-font-size: 13px; -fx-text-fill: #666666;");

        inputBox.getChildren().addAll(valueField, unitLabel);

        Label rangeLabel = new Label("Range: " + range);
        rangeLabel.setStyle("-fx-font-family: 'Verdant'; -fx-font-size: 11px; -fx-text-fill: #888888;");

        card.getChildren().addAll(header, inputBox, rangeLabel);
        return card;
    }

    private VBox createCareSection() {
        VBox section = new VBox(15);

        Label sectionTitle = new Label("🗓️ Care Schedule");
        sectionTitle.setStyle("-fx-font-family: 'Verdant'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2D5F3F;");

        HBox careGrid = new HBox(20);

        // Days Since Watering
        VBox wateringCard = createCareCard("💧", "Days Since Watering", "days");

        // Days Since Fertilizing
        VBox fertilizingCard = createCareCard("🌱", "Days Since Fertilizing", "days");

        careGrid.getChildren().addAll(wateringCard, fertilizingCard);

        section.getChildren().addAll(sectionTitle, careGrid);
        return section;
    }

    private VBox createCareCard(String icon, String label, String unit) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(15));
        card.setStyle(
                "-fx-background-color: rgba(245, 249, 244, 0.8); " +
                        "-fx-background-radius: 12; " +
                        "-fx-border-color: #C8D9C8; " +
                        "-fx-border-radius: 12; " +
                        "-fx-border-width: 1.5;"
        );
        card.setPrefWidth(320);

        HBox header = new HBox(8);
        header.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 20px;");

        Label nameLabel = new Label(label);
        nameLabel.setStyle("-fx-font-family: 'Verdant'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #3C5148;");
        nameLabel.setWrapText(true);

        header.getChildren().addAll(iconLabel, nameLabel);

        HBox inputBox = new HBox(8);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        TextField valueField = new TextField();
        valueField.setPromptText("0");
        valueField.setPrefWidth(100);
        valueField.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-color: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-border-color: #C8D9C8; " +
                        "-fx-border-radius: 8; " +
                        "-fx-padding: 8;"
        );

        Label unitLabel = new Label(unit);
        unitLabel.setStyle("-fx-font-family: 'Verdant'; -fx-font-size: 13px; -fx-text-fill: #666666;");

        inputBox.getChildren().addAll(valueField, unitLabel);

        card.getChildren().addAll(header, inputBox);
        return card;
    }

    private HBox createButtonBox(StackPane formContainer) {
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setPrefSize(150, 45);
        cancelBtn.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 15px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: rgba(200, 200, 200, 0.8); " +
                        "-fx-text-fill: #333333; " +
                        "-fx-background-radius: 12; " +
                        "-fx-cursor: hand;"
        );

        Button saveBtn = new Button("Save Plant 🌿");
        saveBtn.setPrefSize(150, 45);
        saveBtn.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 15px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #4A7C59; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-cursor: hand;"
        );
        saveBtn.setEffect(new DropShadow(8, Color.rgb(74, 124, 89, 0.4)));

        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 15px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: rgba(180, 180, 180, 0.9); " +
                        "-fx-text-fill: #333333; " +
                        "-fx-background-radius: 12; " +
                        "-fx-cursor: hand;"
        ));

        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 15px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: rgba(200, 200, 200, 0.8); " +
                        "-fx-text-fill: #333333; " +
                        "-fx-background-radius: 12; " +
                        "-fx-cursor: hand;"
        ));

        saveBtn.setOnMouseEntered(e -> saveBtn.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 15px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #3A6C49; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-cursor: hand;"
        ));

        saveBtn.setOnMouseExited(e -> saveBtn.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 15px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-color: #4A7C59; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-cursor: hand;"
        ));

        saveBtn.setOnAction(e -> {
            System.out.println("Plant data saved!");
            // Add your save logic here
        });

        buttonBox.getChildren().addAll(cancelBtn, saveBtn);
        return buttonBox;
    }
}