package com.example.florify.ui.main;

import com.example.florify.machineLearningModels.PMMLLoader;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.jpmml.evaluator.Evaluator;

public class PlantInfoFormView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F5F5F5;");


        VBox formContainer = new VBox(25);
        formContainer.setPadding(new Insets(40));
        formContainer.setAlignment(Pos.TOP_CENTER);
        formContainer.setMaxWidth(900);

        // Title with animated gradient
        Label title = new Label("Add New Plant");
        title.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 32px; " +
                        "-fx-font-weight: bold;"
        );
        title.setAlignment(Pos.CENTER);

        // Animate gradient
        javafx.animation.Timeline colorAnim = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(50), e -> {
                    double t = (System.currentTimeMillis() % 3000) / 3000.0;
                    javafx.scene.paint.LinearGradient gradient = new javafx.scene.paint.LinearGradient(
                            t, 0, t + 1, 0,
                            true, javafx.scene.paint.CycleMethod.REPEAT,
                            new javafx.scene.paint.Stop(0, javafx.scene.paint.Color.web("#000000")),
                            new javafx.scene.paint.Stop(0.5, javafx.scene.paint.Color.web("#A8E27B")),
                            new javafx.scene.paint.Stop(1, javafx.scene.paint.Color.web("#000000"))
                    );
                    title.setTextFill(gradient);
                })
        );

        colorAnim.setCycleCount(javafx.animation.Animation.INDEFINITE);
        colorAnim.play();
        title.setAlignment(Pos.CENTER);

        VBox nameSection = new VBox(10);
        Label nameLabel = new Label("Plant Name");
        nameLabel.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #253528;"
        );

        TextField nameField = new TextField();
        nameField.setPromptText("e.g., Monstera Deliciosa");
        nameField.setPrefHeight(50);
        nameField.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 15px; " +
                        "-fx-background-color: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: #C8D9C8; " +
                        "-fx-border-radius: 10; " +
                        "-fx-border-width: 2; " +
                        "-fx-padding: 12; " +
                        "-fx-focus-color: transparent; " +
                        "-fx-faint-focus-color: transparent;"
        );

        nameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                nameField.setStyle(
                        "-fx-font-family: 'Verdant'; " +
                                "-fx-font-size: 15px; " +
                                "-fx-background-color: white; " +
                                "-fx-background-radius: 10; " +
                                "-fx-border-color: #6B8E4E; " +
                                "-fx-border-radius: 10; " +
                                "-fx-border-width: 2.5; " +
                                "-fx-padding: 12; " +
                                "-fx-focus-color: transparent; " +
                                "-fx-faint-focus-color: transparent;"
                );
            } else {
                nameField.setStyle(
                        "-fx-font-family: 'Verdant'; " +
                                "-fx-font-size: 15px; " +
                                "-fx-background-color: white; " +
                                "-fx-background-radius: 10; " +
                                "-fx-border-color: #C8D9C8; " +
                                "-fx-border-radius: 10; " +
                                "-fx-border-width: 2; " +
                                "-fx-padding: 12; " +
                                "-fx-focus-color: transparent; " +
                                "-fx-faint-focus-color: transparent;"
                );
            }
        });

        nameSection.getChildren().addAll(nameLabel, nameField);

        VBox envSection = new VBox(15);
        Label envTitle = new Label("Environmental Data");
        envTitle.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #253528;"
        );

        HBox metricsRow = new HBox(15);
        metricsRow.setAlignment(Pos.CENTER);

        VBox moistureCard = new VBox(10);
        moistureCard.setPrefWidth(200);
        moistureCard.setPadding(new Insets(15));
        moistureCard.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-border-color: #C8D9C8; " +
                        "-fx-border-radius: 12; " +
                        "-fx-border-width: 2;"
        );

        Label moistureLabel = new Label("Soil Moisture");
        moistureLabel.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #253528;"
        );

        HBox moistureInputBox = new HBox(5);
        moistureInputBox.setAlignment(Pos.CENTER_LEFT);

        TextField moistureField = new TextField();
        moistureField.setPromptText("Value");
        moistureField.setPrefWidth(90);
        moistureField.setPrefHeight(35);
        moistureField.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 13px; " +
                        "-fx-background-color: #FAFAFA; " +
                        "-fx-background-radius: 6; " +
                        "-fx-border-color: #E0E0E0; " +
                        "-fx-border-radius: 6; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-padding: 8; " +
                        "-fx-focus-color: transparent; " +
                        "-fx-faint-focus-color: transparent;"
        );


        moistureInputBox.getChildren().addAll(moistureField);
        moistureCard.getChildren().addAll(moistureLabel, moistureInputBox);

        VBox tempCard = new VBox(10);
        tempCard.setPrefWidth(200);
        tempCard.setPadding(new Insets(15));
        tempCard.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-border-color: #C8D9C8; " +
                        "-fx-border-radius: 12; " +
                        "-fx-border-width: 2;"
        );

        Label tempLabel = new Label("Temperature");
        tempLabel.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #253528;"
        );

        HBox tempInputBox = new HBox(5);
        tempInputBox.setAlignment(Pos.CENTER_LEFT);

        TextField tempField = new TextField();
        tempField.setPromptText("Value");
        tempField.setPrefWidth(90);
        tempField.setPrefHeight(35);
        tempField.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 13px; " +
                        "-fx-background-color: #FAFAFA; " +
                        "-fx-background-radius: 6; " +
                        "-fx-border-color: #E0E0E0; " +
                        "-fx-border-radius: 6; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-padding: 8; " +
                        "-fx-focus-color: transparent; " +
                        "-fx-faint-focus-color: transparent;"
        );

        tempInputBox.getChildren().addAll(tempField);
        tempCard.getChildren().addAll(tempLabel, tempInputBox);

        // Humidity Card
        VBox humidityCard = new VBox(10);
        humidityCard.setPrefWidth(200);
        humidityCard.setPadding(new Insets(15));
        humidityCard.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-border-color: #C8D9C8; " +
                        "-fx-border-radius: 12; " +
                        "-fx-border-width: 2;"
        );

        Label humidityLabel = new Label("Humidity");
        humidityLabel.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #253528;"
        );

        HBox humidityInputBox = new HBox(5);
        humidityInputBox.setAlignment(Pos.CENTER_LEFT);

        TextField humidityField = new TextField();
        humidityField.setPromptText("Value");
        humidityField.setPrefWidth(90);
        humidityField.setPrefHeight(35);
        humidityField.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 13px; " +
                        "-fx-background-color: #FAFAFA; " +
                        "-fx-background-radius: 6; " +
                        "-fx-border-color: #E0E0E0; " +
                        "-fx-border-radius: 6; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-padding: 8; " +
                        "-fx-focus-color: transparent; " +
                        "-fx-faint-focus-color: transparent;"
        );

        humidityInputBox.getChildren().addAll(humidityField);
        humidityCard.getChildren().addAll(humidityLabel, humidityInputBox);

        // Light Hours Card
        VBox lightCard = new VBox(10);
        lightCard.setPrefWidth(200);
        lightCard.setPadding(new Insets(15));
        lightCard.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-border-color: #C8D9C8; " +
                        "-fx-border-radius: 12; " +
                        "-fx-border-width: 2;"
        );

        Label lightLabel = new Label("Light Hours");
        lightLabel.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 13px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #253528;"
        );

        HBox lightInputBox = new HBox(5);
        lightInputBox.setAlignment(Pos.CENTER_LEFT);

        TextField lightField = new TextField();
        lightField.setPromptText("Value");
        lightField.setPrefWidth(90);
        lightField.setPrefHeight(35);
        lightField.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 13px; " +
                        "-fx-background-color: #FAFAFA; " +
                        "-fx-background-radius: 6; " +
                        "-fx-border-color: #E0E0E0; " +
                        "-fx-border-radius: 6; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-padding: 8; " +
                        "-fx-focus-color: transparent; " +
                        "-fx-faint-focus-color: transparent;"
        );

        lightInputBox.getChildren().addAll(lightField);
        lightCard.getChildren().addAll(lightLabel, lightInputBox);

        metricsRow.getChildren().addAll(moistureCard, tempCard, humidityCard, lightCard);
        envSection.getChildren().addAll(envTitle, metricsRow);

        // Care Schedule Section
        VBox careSection = new VBox(15);
        Label careTitle = new Label("Care Schedule");
        careTitle.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #253528;"
        );

        VBox careCard = new VBox(12);
        careCard.setPrefWidth(300);
        careCard.setPadding(new Insets(18));
        careCard.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 12; " +
                        "-fx-border-color: #C8D9C8; " +
                        "-fx-border-radius: 12; " +
                        "-fx-border-width: 2;"
        );

        Label careLabel = new Label("Days Since Watering");
        careLabel.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #253528;"
        );

        HBox careInputBox = new HBox(10);
        careInputBox.setAlignment(Pos.CENTER_LEFT);

        TextField careField = new TextField();
        careField.setPromptText("0");
        careField.setPrefWidth(120);
        careField.setPrefHeight(38);
        careField.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 14px; " +
                        "-fx-background-color: #FAFAFA; " +
                        "-fx-background-radius: 8; " +
                        "-fx-border-color: #E0E0E0; " +
                        "-fx-border-radius: 8; " +
                        "-fx-border-width: 1.5; " +
                        "-fx-padding: 10; " +
                        "-fx-focus-color: transparent; " +
                        "-fx-faint-focus-color: transparent;"
        );

        Label careUnit = new Label("days");
        careUnit.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 13px; " +
                        "-fx-text-fill: #666666; " +
                        "-fx-font-weight: bold;"
        );

        careInputBox.getChildren().addAll(careField, careUnit);
        careCard.getChildren().addAll(careLabel, careInputBox);
        careSection.getChildren().addAll(careTitle, careCard);

        // Save Button
        Button saveBtn = new Button("Save Plant");
        saveBtn.setPrefSize(200, 50);
        saveBtn.setStyle("""
    -fx-background-color: linear-gradient(to right, #6B8E4E, #A0C48C);
    -fx-background-radius: 12;
    -fx-border-radius: 12;
    -fx-text-fill: white;
    -fx-font-weight: bold;
    -fx-font-size: 16;
    -fx-font-family: Verdant;
    -fx-cursor: hand;
""");

        Label predictionLabel = new Label();
        predictionLabel.setStyle(
                "-fx-font-family: 'Verdant'; " +
                        "-fx-font-size: 16px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #253528;"
        );
        formContainer.getChildren().add(predictionLabel); // Add it under the Save button


        saveBtn.setOnMouseEntered(e ->{saveBtn.setEffect(addGlowEffect());});
        saveBtn.setOnMouseExited(e ->{saveBtn.setEffect(null);});
        saveBtn.setOnMousePressed(e -> {
            try {
                // Parse inputs
                Double soilMoisture = Double.parseDouble(moistureField.getText());
                Double temperature = Double.parseDouble(tempField.getText());
                Double humidity = Double.parseDouble(humidityField.getText());
                Double lightHours = Double.parseDouble(lightField.getText());
                Double daysSinceWatering = Double.parseDouble(careField.getText());

                // Load the model
                Evaluator evaluator = PMMLLoader.loadRfModel("/Watering_Predictor.pmml");
                // Get prediction from PMML
                String result = PMMLLoader.rfModelPrediction(
                        evaluator,
                        soilMoisture,
                        temperature,
                        humidity,
                        lightHours,
                        daysSinceWatering
                );

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Watering Recommendation");
                alert.setHeaderText(null);
                alert.setContentText(result);
                // Style the dialog pane
                alert.getDialogPane().setStyle(
                        "-fx-background-color: linear-gradient(to right, #6B8E4E, #A0C48C);" +
                                "-fx-font-family: 'Verdant';" +
                                "-fx-font-size: 16px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: white;" // Note: may not affect content text
                );
                alert.setGraphic(null);

                // Optional: customize buttons
                Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setStyle(
                        "-fx-background-color: #A0C48C;" +
                                "-fx-text-fill: white;" +
                                "-fx-font-weight: bold;"
                );
                alert.showAndWait();

            } catch (NumberFormatException ex) {
                predictionLabel.setText("Please enter valid numeric values!");
            } catch (Exception ex) {
                predictionLabel.setText("Error predicting: " + ex.getMessage());
                ex.printStackTrace();
            }

        });
        saveBtn.disableProperty().bind(         // if any field is empty disable the button
                moistureField.textProperty().isEmpty()
                .or(tempField.textProperty().isEmpty())
                .or(humidityField.textProperty().isEmpty())
                .or(lightField.textProperty().isEmpty())
                .or(careField.textProperty().isEmpty())
        );

        formContainer.getChildren().addAll(title, nameSection, envSection, careSection, saveBtn);

        root.setCenter(formContainer);

        Scene scene = new Scene(root, 950, 700);
        primaryStage.setTitle("Add New Plant");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private DropShadow addGlowEffect() {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#6B8E4E"));
        glow.setRadius(3.5);
        glow.setSpread(0.15);
        return glow;
    }
}