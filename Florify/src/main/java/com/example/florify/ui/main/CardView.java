package com.example.florify.ui.main;

import com.almasb.fxgl.net.Client;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.util.JSONPObject;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class CardView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        String imgLink = "/com/example/florify/WhatsApp Image 2025-12-29 at 6.37.34 PM.jpeg";
        VBox upperHalf = createStyledUpperHalf(imgLink, "Spinach");

        Scene scene = new Scene(upperHalf);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public VBox createStyledUpperHalf(String imgLink, String title) {
        HBox upperHalf = new HBox();

        Image plantImg = new Image(Objects.requireNonNull(getClass().getResource(imgLink)).toExternalForm());
        ImageView plantImgView = new ImageView(plantImg);
        plantImgView.setFitHeight(80);
        plantImgView.setFitWidth(60);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 80; -fx-font-family: Verdant; ");

        Label info = new Label(getPlantInfo(title));

        upperHalf.getChildren().addAll(titleLabel, plantImgView);
        VBox upperHalf2 = new VBox();
        upperHalf2.getChildren().addAll(upperHalf, info);

        return upperHalf2;
    }

    // gets a brief plant info
    public String getPlantInfo(String plant) {
        try {
            String apiKey = "usr-sTYTzukSx9XeysmJJaSOrmvNA5iqest9CRD-jPG_pjs"; // Get from https://trefle.io
            String url = "https://trefle.io/api/v1/plants/search?token=" + apiKey +
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
                        return getDetailedPlantInfo(id, apiKey);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "No information available for " + plant;
    }
    private String getDetailedPlantInfo(int plantId, String apiKey) {
        try {
            String url = "https://trefle.io/api/v1/plants/" + plantId + "?token=" + apiKey;

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
                    String familyScientific = data.optString("family", "");
                    String genus = data.optString("genus", "");
                    int year = data.optInt("year", 0);
                    String author = data.optString("author", "");

                    // Header
                    info.append("🌿 ").append(commonName);
                    if (!scientificName.isEmpty()) {
                        info.append("\n📚 Scientific Name: ").append(scientificName);
                        if (year > 0) {
                            info.append(" (").append(year).append(")");
                        }
                        if (!author.isEmpty()) {
                            info.append(" by ").append(author);
                        }
                    }

                    // Main Species Details
                    JSONObject mainSpecies = data.optJSONObject("main_species");
                    if (mainSpecies != null) {
                        info.append("\n");

                        // Growth Information
                        JSONObject growth = mainSpecies.optJSONObject("growth");
                        if (growth != null) {
                            info.append("\n🌱 GROWTH INFORMATION:");

                            // Light requirements
                            int light = growth.optInt("light", -1);
                            if (light != -1) {
                                info.append("\n   ☀️ Light: ");
                                if (light >= 9) info.append("Full sun (9-10/10)");
                                else if (light >= 7) info.append("Mostly sunny (7-8/10)");
                                else if (light >= 5) info.append("Partial shade (5-6/10)");
                                else if (light >= 3) info.append("Mostly shade (3-4/10)");
                                else info.append("Full shade (0-2/10)");
                            }

                            // Humidity
                            int humidity = growth.optInt("atmospheric_humidity", -1);
                            if (humidity != -1) {
                                info.append("\n   💧 Humidity: ").append(humidity).append("/10");
                            }

                            // Soil nutriments
                            int nutrients = growth.optInt("soil_nutriments", -1);
                            if (nutrients != -1) {
                                info.append("\n   🌍 Soil Nutrients: ").append(nutrients).append("/10");
                            }

                            // Soil salinity
                            int salinity = growth.optInt("soil_salinity", -1);
                            if (salinity != -1) {
                                info.append("\n   🧂 Soil Salinity Tolerance: ").append(salinity).append("/10");
                            }

                            // pH
                            double phMin = growth.optDouble("ph_minimum", -1);
                            double phMax = growth.optDouble("ph_maximum", -1);
                            if (phMin != -1 && phMax != -1) {
                                info.append("\n   🧪 pH Range: ").append(phMin).append(" - ").append(phMax);
                            }

                            // Minimum temperatures
                            JSONObject minTemp = growth.optJSONObject("minimum_temperature");
                            if (minTemp != null) {
                                int celsius = minTemp.optInt("deg_c", -999);
                                int fahrenheit = minTemp.optInt("deg_f", -999);
                                if (celsius != -999) {
                                    info.append("\n   🌡️ Min Temperature: ").append(celsius).append("°C");
                                    if (fahrenheit != -999) {
                                        info.append(" (").append(fahrenheit).append("°F)");
                                    }
                                }
                            }

                            // Maximum temperatures
                            JSONObject maxTemp = growth.optJSONObject("maximum_temperature");
                            if (maxTemp != null) {
                                int celsius = maxTemp.optInt("deg_c", -999);
                                int fahrenheit = maxTemp.optInt("deg_f", -999);
                                if (celsius != -999) {
                                    info.append("\n   🌡️ Max Temperature: ").append(celsius).append("°C");
                                    if (fahrenheit != -999) {
                                        info.append(" (").append(fahrenheit).append("°F)");
                                    }
                                }
                            }

                            // Minimum precipitation
                            int minPrecip = growth.optInt("minimum_precipitation", -1);
                            if (minPrecip != -1) {
                                info.append("\n   🌧️ Min Precipitation: ").append(minPrecip).append(" mm");
                            }

                            // Maximum precipitation
                            int maxPrecip = growth.optInt("maximum_precipitation", -1);
                            if (maxPrecip != -1) {
                                info.append("\n   🌧️ Max Precipitation: ").append(maxPrecip).append(" mm");
                            }
                        }

                        // Specifications
                        JSONObject specs = mainSpecies.optJSONObject("specifications");
                        if (specs != null) {
                            info.append("\n\n📏 SPECIFICATIONS:");

                            // Average height
                            JSONObject avgHeight = specs.optJSONObject("average_height");
                            if (avgHeight != null) {
                                int cm = avgHeight.optInt("cm", -1);
                                if (cm != -1) {
                                    info.append("\n   📐 Average Height: ").append(cm).append(" cm");
                                }
                            }

                            // Maximum height
                            JSONObject maxHeight = specs.optJSONObject("maximum_height");
                            if (maxHeight != null) {
                                int cm = maxHeight.optInt("cm", -1);
                                if (cm != -1) {
                                    info.append("\n   📐 Maximum Height: ").append(cm).append(" cm");
                                }
                            }

                            // Toxicity
                            String toxicity = specs.optString("toxicity", "");
                            if (!toxicity.isEmpty() && !toxicity.equals("none")) {
                                info.append("\n   ⚠️ Toxicity: ").append(toxicity);
                            }

                            // Growth habit
                            String growthHabit = specs.optString("growth_habit", "");
                            if (!growthHabit.isEmpty()) {
                                info.append("\n   🌿 Growth Habit: ").append(growthHabit);
                            }

                            // Growth form
                            String growthForm = specs.optString("growth_form", "");
                            if (!growthForm.isEmpty()) {
                                info.append("\n   🌳 Growth Form: ").append(growthForm);
                            }

                            // Growth rate
                            String growthRate = specs.optString("growth_rate", "");
                            if (!growthRate.isEmpty()) {
                                info.append("\n   ⚡ Growth Rate: ").append(growthRate);
                            }
                        }

                        // Edible information
                        boolean edible = mainSpecies.optBoolean("edible", false);
                        String ediblePart = mainSpecies.optString("edible_part", "");
                        if (edible) {
                            info.append("\n\n🍽️ EDIBILITY:");
                            info.append("\n   ✅ This plant is edible!");
                            if (!ediblePart.isEmpty() && !ediblePart.equals("null")) {
                                info.append("\n   🥗 Edible Parts: ").append(ediblePart);
                            }
                        }

                        // Flower information
                        JSONObject flower = mainSpecies.optJSONObject("flower");
                        if (flower != null) {
                            info.append("\n\n🌸 FLOWER:");
                            String color = flower.optString("color", "");
                            boolean conspicuous = flower.optBoolean("conspicuous", false);

                            if (!color.isEmpty() && !color.equals("null")) {
                                info.append("\n   🎨 Color: ").append(color);
                            }
                            if (conspicuous) {
                                info.append("\n   👁️ Has conspicuous flowers");
                            }
                        }

                        // Foliage information
                        JSONObject foliage = mainSpecies.optJSONObject("foliage");
                        if (foliage != null) {
                            info.append("\n\n🍃 FOLIAGE:");
                            String texture = foliage.optString("texture", "");
                            String color = foliage.optString("color", "");
                            String leafRetention = foliage.optBoolean("leaf_retention", false) ? "Evergreen" : "Deciduous";

                            if (!texture.isEmpty() && !texture.equals("null")) {
                                info.append("\n   ✨ Texture: ").append(texture);
                            }
                            if (!color.isEmpty() && !color.equals("null")) {
                                info.append("\n   🎨 Color: ").append(color);
                            }
                            info.append("\n   🌿 Type: ").append(leafRetention);
                        }

                        // Fruit or seed information
                        JSONObject fruitOrSeed = mainSpecies.optJSONObject("fruit_or_seed");
                        if (fruitOrSeed != null) {
                            info.append("\n\n🫐 FRUIT/SEED:");
                            boolean conspicuous = fruitOrSeed.optBoolean("conspicuous", false);
                            String color = fruitOrSeed.optString("color", "");
                            String seedPersistence = fruitOrSeed.optBoolean("seed_persistence", false) ? "Persistent" : "Non-persistent";

                            if (conspicuous) {
                                info.append("\n   👁️ Has conspicuous fruit/seed");
                            }
                            if (!color.isEmpty() && !color.equals("null")) {
                                info.append("\n   🎨 Color: ").append(color);
                            }
                            info.append("\n   🌰 Seed Persistence: ").append(seedPersistence);
                        }
                    }

                    // Distribution
                    JSONObject distribution = data.optJSONObject("distribution");
                    if (distribution != null) {
                        JSONArray natives = distribution.optJSONArray("native");
                        if (natives != null && natives.length() > 0) {
                            info.append("\n\n🗺️ NATIVE TO:");
                            for (int i = 0; i < Math.min(natives.length(), 10); i++) {
                                if (i % 3 == 0) info.append("\n   ");
                                info.append(natives.getString(i));
                                if (i < natives.length() - 1) info.append(", ");
                            }
                            if (natives.length() > 10) {
                                info.append("... and ").append(natives.length() - 10).append(" more");
                            }
                        }
                    }

                    return info.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unable to fetch detailed information.";
    }
}
