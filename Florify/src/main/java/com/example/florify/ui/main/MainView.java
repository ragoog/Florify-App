package com.example.florify.ui.main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class MainView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Main container with soft sage background
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #E8EDE7;");

        // Header
        HBox header = createHeader();

        // Status Cards Row
        HBox statusCards = createStatusCards();

        // Tip of the Day
        VBox tipSection = createTipSection();

        // My Garden Section
        VBox gardenSection = createGardenSection();

        // Community Posts
        VBox communitySection = createCommunitySection();

        // Action Buttons
        HBox actionButtons = createActionButtons();

        // Assemble everything in ScrollPane
        VBox content = new VBox(20);
        content.setPadding(new Insets(0, 40, 40, 40));
        content.getChildren().addAll(
                statusCards,
                tipSection,
                gardenSection,
                communitySection,
                actionButtons
        );

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #E8EDE7; -fx-background-color: #E8EDE7;");

        root.getChildren().addAll(header, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Florify - Plant Care App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(20, 40, 20, 40));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: white; -fx-border-color: #E0E0E0; -fx-border-width: 0 0 1 0;");

        Label logo = new Label("🌿 Florify");
        logo.setFont(Font.font("System", FontWeight.BOLD, 28));
        logo.setTextFill(Color.web("#2D5F3F"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button notificationBtn = new Button("🔔");
        notificationBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 20px; -fx-cursor: hand;");

        Button profileBtn = new Button("👤");
        profileBtn.setStyle("-fx-background-color: transparent; -fx-font-size: 20px; -fx-cursor: hand;");

        header.getChildren().addAll(logo, spacer, notificationBtn, profileBtn);
        return header;
    }

    private HBox createStatusCards() {
        HBox cards = new HBox(15);
        cards.setPadding(new Insets(30, 0, 0, 0));
        cards.setAlignment(Pos.CENTER);

        VBox healthyCard = createStatusCard("✓", "Healthy Plants", "#4A7C59", "12 plants thriving");
        VBox waterCard = createStatusCard("💧", "Needs Water", "#E8A847", "3 plants need attention");
        VBox diseaseCard = createStatusCard("⚠", "Disease Alert", "#C85450", "1 plant requires care");

        cards.getChildren().addAll(healthyCard, waterCard, diseaseCard);
        return cards;
    }

    private VBox createStatusCard(String icon, String title, String color, String subtitle) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(25, 35, 25, 35));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        card.setPrefWidth(280);
        card.setMinHeight(120);

        HBox titleBox = new HBox(12);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(28));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web(color));

        titleBox.getChildren().addAll(iconLabel, titleLabel);

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setFont(Font.font(13));
        subtitleLabel.setTextFill(Color.web("#666666"));

        card.getChildren().addAll(titleBox, subtitleLabel);

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 15, 0, 0, 3); -fx-cursor: hand;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);"));

        return card;
    }

    private VBox createTipSection() {
        List<String> tips = List.of(
                "Water your plants early in the morning for best absorption",
                "Rotate your plants weekly for even growth",
                "Check leaves regularly for early signs of pests",
                "Use well-draining soil to prevent root rot",
                "Clean plant leaves gently to improve photosynthesis"
        );

        LocalDate today = LocalDate.now();
        int tipIndex = today.getDayOfYear() % tips.size();
        String tipOfTheDay = tips.get(tipIndex);

        VBox tipBox = new VBox(8);
        tipBox.setPadding(new Insets(20, 30, 20, 30));
        tipBox.setStyle("-fx-background-color: #F5F9F4; -fx-background-radius: 10; -fx-border-color: #C8D9C8; -fx-border-radius: 10; -fx-border-width: 1;");

        Label tipTitle = new Label("💡 Tip of the Day");
        tipTitle.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        tipTitle.setTextFill(Color.web("#2D5F3F"));

        Label tipText = new Label(tipOfTheDay);
        tipText.setFont(Font.font(15));
        tipText.setWrapText(true);
        tipText.setTextFill(Color.web("#3D3D3D"));

        tipBox.getChildren().addAll(tipTitle, tipText);
        return tipBox;
    }

    private VBox createGardenSection() {
        VBox section = new VBox(20);

        Label sectionTitle = new Label("My Garden");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 26));
        sectionTitle.setTextFill(Color.web("#2D5F3F"));

        GridPane plantGrid = new GridPane();
        plantGrid.setHgap(20);
        plantGrid.setVgap(20);
        plantGrid.setAlignment(Pos.CENTER);

        // Sample plants
        String[][] plants = {
                {"🌹", "Rose Garden", "Blooming"},
                {"🌿", "Monstera", "Healthy"},
                {"🪴", "Snake Plant", "Needs Water"},
                {"🌵", "Cactus", "Healthy"}
        };

        for (int i = 0; i < plants.length; i++) {
            VBox plantCard = createPlantCard(plants[i][0], plants[i][1], plants[i][2]);
            plantGrid.add(plantCard, i % 4, i / 4);
        }

        section.getChildren().addAll(sectionTitle, plantGrid);
        return section;
    }

    private VBox createPlantCard(String icon, String name, String status) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");
        card.setPrefSize(200, 180);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(50));

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 16));
        nameLabel.setTextFill(Color.web("#2D2D2D"));

        Label statusLabel = new Label(status);
        statusLabel.setFont(Font.font(13));
        statusLabel.setTextFill(Color.web("#4A7C59"));
        statusLabel.setStyle("-fx-background-color: #E8F5E8; -fx-padding: 4 12; -fx-background-radius: 12;");

        card.getChildren().addAll(iconLabel, nameLabel, statusLabel);

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 3); -fx-cursor: hand;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);"));

        return card;
    }

    private VBox createCommunitySection() {
        VBox section = new VBox(20);

        Label sectionTitle = new Label("Community Posts");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 26));
        sectionTitle.setTextFill(Color.web("#2D5F3F"));

        VBox post1 = createCommunityPost("🌱", "New leaf on my Monstera!", "24 Likes • 8 Comments");
        VBox post2 = createCommunityPost("💛", "Help! My plant's leaves are turning yellow. Any advice?", "15 Replies");

        section.getChildren().addAll(sectionTitle, post1, post2);
        return section;
    }

    private VBox createCommunityPost(String icon, String text, String engagement) {
        VBox post = new VBox(10);
        post.setPadding(new Insets(20, 25, 20, 25));
        post.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #E8E8E8; -fx-border-radius: 12; -fx-border-width: 1;");

        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(24));

        Label postText = new Label(text);
        postText.setFont(Font.font(15));
        postText.setWrapText(true);
        postText.setTextFill(Color.web("#2D2D2D"));

        header.getChildren().addAll(iconLabel, postText);

        Label engagementLabel = new Label(engagement);
        engagementLabel.setFont(Font.font(13));
        engagementLabel.setTextFill(Color.web("#888888"));

        post.getChildren().addAll(header, engagementLabel);
        return post;
    }

    private HBox createActionButtons() {
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(20, 0, 0, 0));

        Button addPlantBtn = createActionButton("+ Add a Plant", "#4A7C59");
        Button scanLeafBtn = createActionButton("🔍 Scan a Leaf", "#3A6B8C");
        Button waterScheduleBtn = createActionButton("💧 Water Schedule", "#E8A847");

        buttons.getChildren().addAll(addPlantBtn, scanLeafBtn, waterScheduleBtn);
        return buttons;
    }

    private Button createActionButton(String text, String color) {
        Button btn = new Button(text);
        btn.setFont(Font.font("System", FontWeight.SEMI_BOLD, 15));
        btn.setPrefSize(220, 50);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand;");

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: derive(" + color + ", -10%); -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-background-radius: 10; -fx-cursor: hand;"));

        return btn;
    }
}