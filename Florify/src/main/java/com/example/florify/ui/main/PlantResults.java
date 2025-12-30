package com.example.florify.ui.main;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class PlantResults extends Application {

    private TextField searchField;
    private ScrollPane resultPane;
    private VBox resultContainer;
    private static final String API_KEY = "usr-sTYTzukSx9XeysmJJaSOrmvNA5iqest9CRD-jPG_pjs";

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f2f5;");

        // Top Navigation
        HBox topNav = createStyledTopBar();
        root.setTop(topNav);

        // Search Section
        VBox searchSection = createSearchSection();
        root.setTop(new VBox(topNav, searchSection));

        // Results Area
        resultPane = createResultsArea();
        root.setCenter(resultPane);

        Scene scene = new Scene(root, 900, 800);
        stage.setScene(scene);
        stage.setTitle("Florify - Plant Explorer");
        stage.show();
    }

    private HBox createStyledTopBar() {
        HBox topBar = new HBox(10);
        topBar.setPrefHeight(50);
        topBar.setPadding(new Insets(10, 20, 10, 20));

        // Icon
        String imgLink = "/com/example/florify/Petite_plante_sur_le_sol_dans_un_style_pixel_art___Vecteur_Premium-removebg-preview.png";
        try {
            Image iconImage = new Image(Objects.requireNonNull(getClass().getResource(imgLink)).toExternalForm());
            ImageView iconView = new ImageView(iconImage);
            iconView.setFitHeight(40);
            iconView.setFitWidth(40);
            topBar.getChildren().add(iconView);
        } catch (Exception e) {
            // Icon not found, continue without it
        }

        // Title
        Label title = new Label("Florify Plant Explorer");
        title.setStyle("-fx-font-size: 24px; " +
                "-fx-font-family: Verdant; " +
                "-fx-text-fill: #3C5148;" +
                "-fx-font-weight: bold;"
        );

        topBar.getChildren().add(title);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.9);" +
                        "-fx-background-radius: 8;"
        );

        return topBar;
    }

    private VBox createSearchSection() {
        VBox searchBox = new VBox(15);
        searchBox.setPadding(new Insets(20));
        searchBox.setStyle("-fx-background-color: white; -fx-background-radius: 0 0 8 8;");

        // Search title
        Label searchLabel = new Label("🌿 Discover Plants");
        searchLabel.setStyle("-fx-font-size: 20px; -fx-font-family: Verdant; " +
                "-fx-text-fill: #3C5148; -fx-font-weight: bold;");

        // Search input
        HBox searchInputBox = new HBox(10);
        searchInputBox.setAlignment(Pos.CENTER);

        searchField = new TextField();
        searchField.setPromptText("Enter plant name (e.g., Spinach, Rose, Bamboo)...");
        searchField.setPrefWidth(500);
        searchField.setStyle("""
                -fx-font-size: 16px;
                -fx-font-family: Verdant;
                -fx-padding: 12;
                -fx-background-radius: 8;
                -fx-border-color: #dcdcdc;
                -fx-border-radius: 8;
                -fx-border-width: 2;
                -fx-focus-color: #6B8E4E;
                -fx-faint-focus-color: transparent;
                """);

        // Search button
        Button searchBtn = new Button("Search");
        searchBtn.setStyle("""
                -fx-background-color: linear-gradient(to right, #6B8E4E, #A0C48C);
                -fx-background-radius: 8;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-font-size: 16px;
                -fx-font-family: Verdant;
                -fx-cursor: hand;
                -fx-padding: 12 30 12 30;
                """);

        searchBtn.setOnMouseEntered(e -> searchBtn.setEffect(addGlowEffect()));
        searchBtn.setOnMouseExited(e -> searchBtn.setEffect(null));
        searchBtn.setOnAction(e -> searchPlant());

        // Allow Enter key to search
        searchField.setOnAction(e -> searchPlant());

        searchInputBox.getChildren().addAll(searchField, searchBtn);

        searchBox.getChildren().addAll(searchLabel, searchInputBox);
        searchBox.setAlignment(Pos.CENTER);

        return searchBox;
    }

    private ScrollPane createResultsArea() {
        resultContainer = new VBox(20);
        resultContainer.setPadding(new Insets(30));
        resultContainer.setStyle("-fx-background-color: #f0f2f5;");
        resultContainer.setAlignment(Pos.TOP_CENTER);

        // Welcome message
        VBox welcomeBox = new VBox(15);
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.setPadding(new Insets(50));
        welcomeBox.setMaxWidth(600);

        Label welcomeTitle = new Label("🌱 Welcome to Plant Explorer");
        welcomeTitle.setStyle("-fx-font-size: 28px; -fx-font-family: Verdant; " +
                "-fx-text-fill: #3C5148; -fx-font-weight: bold;");

        Label welcomeText = new Label("Search for any plant to discover detailed information about " +
                "its growth requirements, characteristics, and care tips!");
        welcomeText.setWrapText(true);
        welcomeText.setStyle("-fx-font-size: 16px; -fx-font-family: Verdant; " +
                "-fx-text-fill: #666666; -fx-text-alignment: center;");
        welcomeText.setAlignment(Pos.CENTER);

        welcomeBox.getChildren().addAll(welcomeTitle, welcomeText);
        resultContainer.getChildren().add(welcomeBox);

        ScrollPane scrollPane = new ScrollPane(resultContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f0f2f5; -fx-background-color: #f0f2f5;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        return scrollPane;
    }

    private void searchPlant() {
        String plantName = searchField.getText().trim();
        if (plantName.isEmpty()) {
            showError("Please enter a plant name!");
            return;
        }

        // Clear previous results and show loading
        resultContainer.getChildren().clear();
        VBox loadingBox = createLoadingIndicator();
        resultContainer.getChildren().add(loadingBox);

        // Fetch plant info in background thread
        new Thread(() -> {
            String plantInfo = getPlantInfo(plantName);
            javafx.application.Platform.runLater(() -> displayResult(plantName, plantInfo));
        }).start();
    }

    private VBox createLoadingIndicator() {
        VBox loadingBox = new VBox(15);
        loadingBox.setAlignment(Pos.CENTER);
        loadingBox.setPadding(new Insets(100));

        ProgressIndicator progress = new ProgressIndicator();
        progress.setStyle("-fx-progress-color: #6B8E4E;");

        Label loadingText = new Label("Searching for plant information...");
        loadingText.setStyle("-fx-font-size: 16px; -fx-font-family: Verdant; " +
                "-fx-text-fill: #666666;");

        loadingBox.getChildren().addAll(progress, loadingText);
        return loadingBox;
    }

    private void displayResult(String plantName, String info) {
        resultContainer.getChildren().clear();

        VBox resultCard = new VBox(20);
        resultCard.setPadding(new Insets(30));
        resultCard.setMaxWidth(800);
        resultCard.setStyle("-fx-background-color: white; " +
                "-fx-background-radius: 12; " +
                "-fx-border-color: #dcdcdc; " +
                "-fx-border-radius: 12; " +
                "-fx-border-width: 1; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // Plant title with icon
        HBox titleBox = new HBox(15);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Label plantIcon = new Label("🌿");
        plantIcon.setStyle("-fx-font-size: 48px;");

        VBox titleTextBox = new VBox(5);
        Label plantTitle = new Label(capitalizeWords(plantName));
        plantTitle.setStyle("-fx-font-size: 32px; -fx-font-family: Verdant; " +
                "-fx-text-fill: #2C4A3E; -fx-font-weight: bold;");

        Label subtitle = new Label("Plant Information");
        subtitle.setStyle("-fx-font-size: 14px; -fx-font-family: Verdant; " +
                "-fx-text-fill: #6B8E4E; -fx-font-weight: bold;");

        titleTextBox.getChildren().addAll(plantTitle, subtitle);
        titleBox.getChildren().addAll(plantIcon, titleTextBox);

        // Separator
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #e0e0e0;");

        // Plant information
        Label infoLabel = new Label(info);
        infoLabel.setWrapText(true);
        infoLabel.setStyle("-fx-font-size: 14px; -fx-font-family: 'Consolas', 'Monaco', monospace; " +
                "-fx-text-fill: #333333; -fx-line-spacing: 4px;");

        // Search again button
        Button searchAgainBtn = new Button("🔍 Search Another Plant");
        searchAgainBtn.setStyle("""
                -fx-background-color: #6B8E4E;
                -fx-background-radius: 8;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-font-size: 14px;
                -fx-font-family: Verdant;
                -fx-cursor: hand;
                -fx-padding: 10 20 10 20;
                """);
        searchAgainBtn.setOnMouseEntered(e -> searchAgainBtn.setEffect(addGlowEffect()));
        searchAgainBtn.setOnMouseExited(e -> searchAgainBtn.setEffect(null));
        searchAgainBtn.setOnAction(e -> {
            searchField.clear();
            searchField.requestFocus();
        });

        HBox buttonBox = new HBox(searchAgainBtn);
        buttonBox.setAlignment(Pos.CENTER);

        resultCard.getChildren().addAll(titleBox, separator, infoLabel, buttonBox);
        resultContainer.getChildren().add(resultCard);
    }

    private void showError(String message) {
        resultContainer.getChildren().clear();

        VBox errorBox = new VBox(15);
        errorBox.setAlignment(Pos.CENTER);
        errorBox.setPadding(new Insets(50));
        errorBox.setMaxWidth(500);
        errorBox.setStyle("-fx-background-color: #ffe6e6; " +
                "-fx-background-radius: 12; " +
                "-fx-border-color: #ffcccc; " +
                "-fx-border-radius: 12; " +
                "-fx-border-width: 2;");

        Label errorIcon = new Label("⚠️");
        errorIcon.setStyle("-fx-font-size: 48px;");

        Label errorText = new Label(message);
        errorText.setWrapText(true);
        errorText.setStyle("-fx-font-size: 16px; -fx-font-family: Verdant; " +
                "-fx-text-fill: #cc0000; -fx-font-weight: bold;");
        errorText.setAlignment(Pos.CENTER);

        errorBox.getChildren().addAll(errorIcon, errorText);
        resultContainer.getChildren().add(errorBox);
    }

    private String getPlantInfo(String plant) {
        try {
            String url = "https://trefle.io/api/v1/plants/search?token=" + API_KEY +
                    "&q=" + URLEncoder.encode(plant, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject json = new JSONObject(response.body());
                JSONArray data = json.optJSONArray("data");

                if (data != null && data.length() > 0) {
                    JSONObject plantData = data.getJSONObject(0);
                    int id = plantData.optInt("id", -1);

                    if (id != -1) {
                        return getDetailedPlantInfo(id);
                    }
                }
            }
        } catch (Exception e) {
            return "❌ Error connecting to plant database. Please check your internet connection and try again.";
        }
        return "❌ No information available for \"" + plant + "\". Try searching for a different plant name.";
    }

    private String getDetailedPlantInfo(int plantId) {
        try {
            String url = "https://trefle.io/api/v1/plants/" + plantId + "?token=" + API_KEY;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject json = new JSONObject(response.body());
                JSONObject data = json.optJSONObject("data");

                if (data != null) {
                    StringBuilder info = new StringBuilder();

                    // Basic Info
                    String commonName = data.optString("common_name", "Unknown Plant");
                    String scientificName = data.optString("scientific_name", "");
                    String family = data.optString("family_common_name", "");
                    int year = data.optInt("year", 0);
                    String author = data.optString("author", "");

                    // Header
                    info.append("🌿 ").append(commonName.toUpperCase());
                    if (!scientificName.isEmpty()) {
                        info.append("\n📚 Scientific Name: ").append(scientificName);
                        if (year > 0) {
                            info.append(" (").append(year).append(")");
                        }
                        if (!author.isEmpty()) {
                            info.append(" by ").append(author);
                        }
                    }
                    if (!family.isEmpty()) {
                        info.append("\n👪 Family: ").append(family);
                    }

                    // Main Species Details
                    JSONObject mainSpecies = data.optJSONObject("main_species");
                    if (mainSpecies != null) {
                        // Growth Information
                        JSONObject growth = mainSpecies.optJSONObject("growth");
                        if (growth != null) {
                            info.append("\n\n🌱 GROWTH REQUIREMENTS:");

                            int light = growth.optInt("light", -1);
                            if (light != -1) {
                                info.append("\n   ☀️  Light: ");
                                if (light >= 9) info.append("Full sun (").append(light).append("/10)");
                                else if (light >= 7) info.append("Mostly sunny (").append(light).append("/10)");
                                else if (light >= 5) info.append("Partial shade (").append(light).append("/10)");
                                else if (light >= 3) info.append("Mostly shade (").append(light).append("/10)");
                                else info.append("Full shade (").append(light).append("/10)");
                            }

                            int humidity = growth.optInt("atmospheric_humidity", -1);
                            if (humidity != -1) {
                                info.append("\n   💧 Humidity: ").append(humidity).append("/10");
                            }

                            int nutrients = growth.optInt("soil_nutriments", -1);
                            if (nutrients != -1) {
                                info.append("\n   🌍 Soil Nutrients: ").append(nutrients).append("/10");
                            }

                            double phMin = growth.optDouble("ph_minimum", -1);
                            double phMax = growth.optDouble("ph_maximum", -1);
                            if (phMin != -1 && phMax != -1) {
                                info.append("\n   🧪 pH Range: ").append(phMin).append(" - ").append(phMax);
                            }

                            JSONObject minTemp = growth.optJSONObject("minimum_temperature");
                            if (minTemp != null) {
                                int celsius = minTemp.optInt("deg_c", -999);
                                int fahrenheit = minTemp.optInt("deg_f", -999);
                                if (celsius != -999) {
                                    info.append("\n   🌡️  Min Temperature: ").append(celsius).append("°C (").append(fahrenheit).append("°F)");
                                }
                            }

                            JSONObject maxTemp = growth.optJSONObject("maximum_temperature");
                            if (maxTemp != null) {
                                int celsius = maxTemp.optInt("deg_c", -999);
                                int fahrenheit = maxTemp.optInt("deg_f", -999);
                                if (celsius != -999) {
                                    info.append("\n   🌡️  Max Temperature: ").append(celsius).append("°C (").append(fahrenheit).append("°F)");
                                }
                            }
                        }

                        // Specifications
                        JSONObject specs = mainSpecies.optJSONObject("specifications");
                        if (specs != null) {
                            info.append("\n\n📏 PLANT SPECIFICATIONS:");

                            JSONObject avgHeight = specs.optJSONObject("average_height");
                            if (avgHeight != null) {
                                int cm = avgHeight.optInt("cm", -1);
                                if (cm != -1) {
                                    info.append("\n   📐 Average Height: ").append(cm).append(" cm");
                                }
                            }

                            JSONObject maxHeight = specs.optJSONObject("maximum_height");
                            if (maxHeight != null) {
                                int cm = maxHeight.optInt("cm", -1);
                                if (cm != -1) {
                                    info.append("\n   📐 Maximum Height: ").append(cm).append(" cm");
                                }
                            }

                            String growthRate = specs.optString("growth_rate", "");
                            if (!growthRate.isEmpty()) {
                                info.append("\n   ⚡ Growth Rate: ").append(capitalizeWords(growthRate));
                            }

                            String toxicity = specs.optString("toxicity", "");
                            if (!toxicity.isEmpty() && !toxicity.equals("none")) {
                                info.append("\n   ⚠️  Toxicity: ").append(capitalizeWords(toxicity));
                            }
                        }

                        // Edible information
                        boolean edible = mainSpecies.optBoolean("edible", false);
                        String ediblePart = mainSpecies.optString("edible_part", "");
                        if (edible) {
                            info.append("\n\n🍽️  EDIBILITY:");
                            info.append("\n   ✅ This plant is EDIBLE!");
                            if (!ediblePart.isEmpty() && !ediblePart.equals("null")) {
                                info.append("\n   🥗 Edible Parts: ").append(capitalizeWords(ediblePart));
                            }
                        }

                        // Flower information
                        JSONObject flower = mainSpecies.optJSONObject("flower");
                        if (flower != null) {
                            String color = flower.optString("color", "");
                            boolean conspicuous = flower.optBoolean("conspicuous", false);

                            if (!color.isEmpty() || conspicuous) {
                                info.append("\n\n🌸 FLOWERS:");
                                if (!color.isEmpty() && !color.equals("null")) {
                                    info.append("\n   🎨 Color: ").append(capitalizeWords(color));
                                }
                                if (conspicuous) {
                                    info.append("\n   ✨ Has showy, conspicuous flowers");
                                }
                            }
                        }

                        // Foliage information
                        JSONObject foliage = mainSpecies.optJSONObject("foliage");
                        if (foliage != null) {
                            String color = foliage.optString("color", "");
                            String texture = foliage.optString("texture", "");
                            boolean evergreen = foliage.optBoolean("leaf_retention", false);

                            if (!color.isEmpty() || !texture.isEmpty() || evergreen) {
                                info.append("\n\n🍃 FOLIAGE:");
                                if (!color.isEmpty() && !color.equals("null")) {
                                    info.append("\n   🎨 Color: ").append(capitalizeWords(color));
                                }
                                if (!texture.isEmpty() && !texture.equals("null")) {
                                    info.append("\n   ✨ Texture: ").append(capitalizeWords(texture));
                                }
                                info.append("\n   🌿 Type: ").append(evergreen ? "Evergreen" : "Deciduous");
                            }
                        }
                    }

                    // Distribution
                    JSONObject distribution = data.optJSONObject("distribution");
                    if (distribution != null) {
                        JSONArray natives = distribution.optJSONArray("native");
                        if (natives != null && natives.length() > 0) {
                            info.append("\n\n🗺️  NATIVE REGIONS:");
                            info.append("\n   ");
                            for (int i = 0; i < Math.min(natives.length(), 8); i++) {
                                info.append(natives.getString(i));
                                if (i < Math.min(natives.length(), 8) - 1) info.append(", ");
                            }
                            if (natives.length() > 8) {
                                info.append("\n   ...and ").append(natives.length() - 8).append(" more regions");
                            }
                        }
                    }

                    return info.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "❌ Unable to fetch detailed information. Please try again.";
    }

    private DropShadow addGlowEffect() {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#6B8E4E"));
        glow.setRadius(5);
        glow.setSpread(0.2);
        return glow;
    }

    private String capitalizeWords(String str) {
        if (str == null || str.isEmpty()) return str;
        String[] words = str.split("\\s+");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return result.toString().trim();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
