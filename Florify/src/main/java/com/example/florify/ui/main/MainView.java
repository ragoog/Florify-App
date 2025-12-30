package com.example.florify.ui.main;

import com.example.florify.ui.login.LoginView;
import com.example.florify.ui.navigation.SceneManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;

import javax.print.DocFlavor;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Objects;

public class MainView extends Application {

    public static void main(String[] args) { launch(args); }


    @Override
    public void start(Stage primaryStage){

            HBox upperBox = createStyledTopBar();
            VBox tipPane = createStyledTipBar();

            HBox cardContainer = new HBox(20);
        cardContainer.setStyle("-fx-background-color: transparent;");

        cardContainer.setPadding(new Insets(20, 20, 20, 20));
            cardContainer.setAlignment(Pos.CENTER);
            cardContainer.getChildren().add(createPlantCard("Tomato", "/com/example/florify/tomato.png"));
            cardContainer.getChildren().add(createPlantCard("Potato", "/com/example/florify/potato.png"));
            cardContainer.getChildren().add(createPlantCard("Mint", "/com/example/florify/mint.png"));
            cardContainer.getChildren().add(createPlantCard("Lettuce", "/com/example/florify/lettuce.png"));
            cardContainer.getChildren().add(createPlantCard("Aloe", "/com/example/florify/aloe.png"));
            cardContainer.getChildren().add(createPlantCard("Spinach", "/com/example/florify/spinach.png"));
            cardContainer.getChildren().add(createPlantCard("Basil", "/com/example/florify/basil.png"));

            ScrollPane scrollPane = new ScrollPane(cardContainer);
        VBox.setMargin(scrollPane, new Insets(70, 0, 30, 0));

        scrollPane.setStyle("""
    -fx-background-color: transparent;
    -fx-background-insets: 0;
    -fx-padding: 0;
""");
        Platform.runLater(() -> {
            Node vp = scrollPane.lookup(".viewport");
            if (vp != null) {
                vp.setStyle("-fx-background-color: transparent;");
            }
        });

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setPannable(true);
            scrollPane.setPadding(new Insets(20, 20, 5, 20));
            scrollPane.setFitToHeight(true);
            scrollPane.setOnScrollFinished(e -> snapToClosestCard(scrollPane, cardContainer));

            // Action buttons
            HBox actionButtons = createActionButtons();

            VBox primary = createStyledPrimary();
            primary.getChildren().addAll(upperBox, tipPane,scrollPane, actionButtons);

            Scene scene = new Scene(primary, 1080, 720);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

    private void snapToClosestCard(ScrollPane scrollPane, HBox container) {
        double scrollX = scrollPane.getHvalue() * (container.getWidth() - scrollPane.getWidth());
        double closest = 0;
        double minDist = Double.MAX_VALUE;

        for (int i = 0; i < container.getChildren().size(); i++) {
            double cardCenter = container.getChildren().get(i).getLayoutX() + container.getChildren().get(i).getBoundsInParent().getWidth() / 2.0;
            double dist = Math.abs(cardCenter - scrollX - scrollPane.getWidth() / 2.0);
            if (dist < minDist) {
                minDist = dist;
                closest = cardCenter;
            }
        }

        double targetHValue = (closest - scrollPane.getWidth() / 2.0) / (container.getWidth() - scrollPane.getWidth());
        targetHValue = Math.max(0, Math.min(1, targetHValue));

        TranslateTransition tt = new TranslateTransition(Duration.millis(300));
        tt.setToX(0); // dummy, we animate via hvalue
        tt.play();

        scrollPane.setHvalue(targetHValue);
    }

    private VBox createPlantCard(String plantName, String imagePath)
    {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));
        card.setPrefSize(60, 100);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15;");

        // plant image
        Image img = new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm(), 180, 180, true, true);
        ImageView imgView = new ImageView(img);
        imgView.setFitWidth(180);
        imgView.setFitHeight(180);
        imgView.setPreserveRatio(false);

        // Plant name
        Text plantNameText = new Text(plantName);
        plantNameText.setFont(Font.font("Verdant", 20));
        plantNameText.setFill(Color.web("#3C5148"));

        card.getChildren().addAll(imgView, plantNameText);

        // Hover scale effect
        card.setOnMouseEntered(e -> card.setScaleY(1.05));
        card.setOnMouseExited(e -> card.setScaleY(1.0));

        return card;
    }

    private VBox createStyledTipBar()
    {
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

        // tab pane consists of tip title label (what this is about) and label of the tip itself
        VBox tabPane = new VBox(8);
        tabPane.setPadding(new Insets(20, 30, 20, 30));
        tabPane.setStyle("-fx-background-color: #F5F9F4; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: #C8D9C8;" +
                "-fx-border-radius: 10; " +
                "-fx-border-width: 3;" +
                "-fx-opacity: 0.9");

        Label tipTitle = new Label("\uD83D\uDCA1 Tip Of The Day: ");
        tipTitle.setStyle("-fx-font-family: Verdant; -fx-font-size: 20px; font-weight: bold; -fx-text-fill: #1B2727;");

        Label tipLabel = getLabel(tipOfTheDay);

        tabPane.getChildren().addAll(tipTitle, tipLabel);
        return tabPane;
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
    private HBox createStyledTopBar()
    {
        HBox topBar = new HBox(10);
        topBar.setPrefHeight(50);

        // start with little icon pic
        String imgLink = "/com/example/florify/Petite_plante_sur_le_sol_dans_un_style_pixel_art___Vecteur_Premium-removebg-preview.png";
        Image iconImage = new Image(Objects.requireNonNull(getClass().getResource(imgLink)).toExternalForm());
        ImageView iconView = new ImageView(iconImage);

        iconView.setFitHeight(48);
        iconView.setFitWidth(48);

        // create the florify app typography
        Label title = new Label("Florify");
        title.setPadding(new Insets(8, 0, 0, 0));
        title.setWrapText(true);
        title.setStyle("-fx-font-size: 30px; " +
                "-fx-font-family: Verdant; " +
                "-fx-text-fill: linear-gradient(to right, #3C5148, #6B8E4E);" +
                "-fx-font-weight: bold;"
        );

        // Animate gradient
        Timeline colorAnim = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            double t = (System.currentTimeMillis() % 3000) / 3000.0; // loop over 3s
            LinearGradient gradient = new LinearGradient(
                    t, 0, t + 1, 0, // move gradient along X
                    true, CycleMethod.REPEAT,
                    new Stop(0, javafx.scene.paint.Color.web("#000000")),
                    new Stop(0.5, javafx.scene.paint.Color.web("#A8E27B")),
                    new Stop(1, javafx.scene.paint.Color.web("#000000"))
            );
            title.setTextFill(gradient);
        }));

        colorAnim.setCycleCount(Animation.INDEFINITE);
        colorAnim.play();

        //region buttons button
        HBox buttonsPane  = new HBox(100);
        Button homeButton = createNavButton("Home");

        Button communityButton = createNavButton("Community");
        communityButton.setOnAction(e -> {
            Scene scene = SceneManager.loadFeedScene();
            SceneManager.switchScene(scene);
        });

        Button logoutButton = createNavButton("Logout");
        logoutButton.setOnAction(e -> {
            LoginView scene = SceneManager.loadLoginScene();
            SceneManager.switchScene(scene.getScene());
        });

        buttonsPane.getChildren().addAll(homeButton, communityButton, logoutButton);
        buttonsPane.setAlignment(Pos.CENTER);

        topBar.getChildren().addAll(iconView, title, buttonsPane);
        HBox.setMargin(buttonsPane, new Insets(0, 0, 0, 100));

        topBar.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.6);" + // semi-transparent
                        "-fx-background-radius: 3;"
        );

        return topBar;
    }
    private Button createNavButton(String text) {
        Button btn = new Button(text);
        // base style (THIS is the important one)
        btn.setStyle(
                "-fx-font-family: 'Verdant';" +
                        "-fx-font-size: 18px;" +
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: #1A1A1A;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-alignment: CENTER;" +
                        "-fx-border-color: transparent;" +
                        "-fx-cursor: hand;"
        );

        btn.setPadding(new Insets(15, 10, 8, 10));


        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-font-family: 'Verdant';" +
                        "-fx-font-size: 20px;" +
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: black;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-alignment: CENTER;" +
                        "-fx-border-color: transparent transparent #1B2727 transparent;" +
                        "-fx-border-width: 0 0 2 0;" +
                        "-fx-cursor: hand;"
        ));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-font-family: 'Verdant';" +
                        "-fx-font-size: 18px;" +
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: #1A1A1A;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-alignment: CENTER;" +
                        "-fx-border-color: transparent;" +
                        "-fx-cursor: hand;"
        ));

        return btn;
    }
    private VBox createStyledPrimary()
    {
        VBox primary  = new VBox(10);

        String imgLink = "/com/example/florify/Hailuo_Image_Aesthetic high quality botanic_460955248711925761.jpg";
        Image bgImage = new Image(Objects.requireNonNull(getClass().getResource(imgLink)).toExternalForm());

        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,true,true,true,true);
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize
        );

        primary.setBackground(new Background(backgroundImage));
        return primary;
    }
    private HBox createActionButtons() {
        HBox buttonBox = new HBox(30);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(30, 0, 30, 0));

        // Add Plant Button
        Button addPlantButton = new Button("Add a new plant");
        addPlantButton.setStyle("""
    -fx-background-color: linear-gradient(to right, #6B8E4E, #A0C48C);
    -fx-background-radius: 12;
    -fx-border-radius: 12;
    -fx-text-fill: white;
    -fx-font-weight: bold;
    -fx-font-size: 23;
    -fx-font-family: Verdant;
    -fx-cursor: hand;
""");
        addPlantButton.setOnMouseEntered(e->{
            addPlantButton.setEffect(addGlowEffect());

        });

        addPlantButton.setOnMouseExited(e->{
            addPlantButton.setEffect(null);
        });

        addPlantButton.setOnAction(e -> {
            SceneManager.openPopupScene(new PlantInfoFormView());
        });

        // Scan Plant Button
        Button scanPlantBtn = new Button("Scan a Plant");
        scanPlantBtn.setStyle("""
    -fx-background-color: linear-gradient(to right, #6B8E4E, #A0C48C);
    -fx-background-radius: 12;
    -fx-border-radius: 12;
    -fx-text-fill: white;
    -fx-font-weight: bold;
    -fx-font-size: 24;
    -fx-font-family: Verdant;
    -fx-cursor: hand;
""");

        scanPlantBtn.setOnMouseEntered(e -> {
            scanPlantBtn.setEffect(addGlowEffect());
        });

        scanPlantBtn.setOnMouseExited(e -> {
            scanPlantBtn.setEffect(null);
        });

        scanPlantBtn.setOnAction(e -> {
            SceneManager.openPopupScene(new ScanPlant());
        });

        Button search = new Button("Search Plant");
        search.setStyle("""
    -fx-background-color: linear-gradient(to right, #6B8E4E, #A0C48C);
    -fx-background-radius: 12;
    -fx-border-radius: 12;
    -fx-text-fill: white;
    -fx-font-weight: bold;
    -fx-font-size: 23;
    -fx-font-family: Verdant;
    -fx-cursor: hand;
""");
        search.setOnMouseEntered(e->{
            search.setEffect(addGlowEffect());
        });

        search.setOnMouseExited(e->{
            search.setEffect(null);
        });

        search.setOnAction(e -> {
            SceneManager.openPopupScene(new PlantResults());
        });

        buttonBox.getChildren().addAll(search, addPlantButton, scanPlantBtn);
        return buttonBox;
    }
    private DropShadow addGlowEffect() {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#6B8E4E"));
        glow.setRadius(3.5);
        glow.setSpread(0.15);
        return glow;
    }
}

