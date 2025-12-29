package com.example.florify.ui.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class MainView extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage)
    {
        HBox upperTab = new HBox(10);
        upperTab.setStyle("-fx-background-color: green;");
        Label label = new Label("Welcome to Florify");
        upperTab.getChildren().add(label);



        //region List of tips
        List<String> tips = List.of(
                "Water your plants early in the morning.",
                "Rotate your plants weekly for even growth.",
                "Check leaves regularly for pests.",
                "Clean your pots to avoid fungal growth.",
                "Use well-draining soil to prevent root rot.",
                "Fertilize your plants once a month during the growing season.",
                "Avoid overwatering; let the topsoil dry slightly between watering.",
                "Prune dead or yellowing leaves regularly.",
                "Place plants near windows for natural sunlight, but avoid harsh direct sun for delicate plants.",
                "Use a humidity tray for tropical plants.",
                "Clean plant leaves gently to allow better photosynthesis.",
                "Repot plants every 1–2 years to refresh soil and space for growth.",
                "Check stems for firmness to detect early signs of disease.",
                "Rotate plants to ensure even growth and light exposure.",
                "Mulch indoor plants lightly to retain moisture.",
                "Avoid using cold water on sensitive plants.",
                "Introduce beneficial insects for natural pest control.",
                "Use a spray bottle for misting humidity-loving plants.",
                "Remove flowers after blooming to encourage new growth.",
                "Keep an eye on seasonal changes and adjust watering accordingly."
        );
        //endregion

        // get tip of the day
        LocalDate today =  LocalDate.now();
        int tipIndex = today.getDayOfYear() % tips.size();  // to ensure it is within bounds
        String tipOfTheDay = tips.get(tipIndex);

        // tab pane consists of tipTab (what this is about) and label of the tip itself
        TabPane tabPane = new TabPane();
        Tab tipTab = new Tab("Tip of the day");
        Label tipLabel = new Label(tipOfTheDay);
        tipLabel.setWrapText(true); // so the tip wraps if too long
        tipTab.setContent(tipLabel);
        tabPane.getTabs().add(tipTab);

        VBox primary  = new VBox(10);
        primary.getChildren().addAll(upperTab, tabPane);

        Scene scene = new Scene(primary, 1080, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void createStyledTopBar()
    {
        HBox topBar = new HBox(10);

        // start with little icon pic
        String imgLink = "/com/example/florify/Petite_plante_sur_le_sol_dans_un_style_pixel_art___Vecteur_Premium-removebg-preview.png";
        Image iconImage = new Image(Objects.requireNonNull(getClass().getResource(imgLink)).toExternalForm());
        ImageView iconView = new ImageView(iconImage);

        iconView.setFitHeight(48);
        iconView.setFitWidth(48);

        // create the florify app typography
        Label title = new Label("Florify");
        title.setWrapText(true);
        title.setStyle("-fx-font-size: 20px; " +
                "-fx-font-style: Verdana; " +
                "-fx-text-fill: linear-gradient(to right, #3C5148, #6B8E4E);"
        );

        //region buttons button

    }
}

