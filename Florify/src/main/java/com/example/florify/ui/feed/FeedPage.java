package com.example.florify.ui.feed;

import com.example.florify.common.Post;
import com.example.florify.common.Session;
import com.example.florify.ui.main.MainView;
import com.sun.tools.javac.Main;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class FeedPage extends Application {

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private VBox feedContainer;
    private TextArea postInput;

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f0f2f5;");

        // Top Navigation
        HBox topNav = createStyledTopBar();
        root.setTop(topNav);

        // Feed Area with ScrollPane
        ScrollPane scrollPane = createFeedArea();
        root.setCenter(scrollPane);

        // Create Scene and show Stage
        Scene scene = new Scene(root, 800, 900);
        stage.setScene(scene);
        stage.setTitle("Florify");
        stage.show();

        // Initialize server connection
        try {
            initializeConnection();
        }
        catch (Exception e) {
            System.out.println("Not connected to server!");
        }
    }

    private HBox createStyledTopBar() {
        HBox topBar = new HBox(10);
        topBar.setPrefHeight(50);

        // Start with little icon pic
        String imgLink = "/com/example/florify/Petite_plante_sur_le_sol_dans_un_style_pixel_art___Vecteur_Premium-removebg-preview.png";
        Image iconImage = new Image(Objects.requireNonNull(getClass().getResource(imgLink)).toExternalForm());
        ImageView iconView = new ImageView(iconImage);

        iconView.setFitHeight(48);
        iconView.setFitWidth(48);

        // Create the florify app typography
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
            double t = (System.currentTimeMillis() % 3000) / 3000.0;
            LinearGradient gradient = new LinearGradient(
                    t, 0, t + 1, 0,
                    true, CycleMethod.REPEAT,
                    new Stop(0, javafx.scene.paint.Color.web("#000000")),
                    new Stop(0.5, javafx.scene.paint.Color.web("#A8E27B")),
                    new Stop(1, javafx.scene.paint.Color.web("#000000"))
            );
            title.setTextFill(gradient);
        }));

        colorAnim.setCycleCount(Animation.INDEFINITE);
        colorAnim.play();

        // Buttons
        HBox buttonsPane = new HBox(100);
        Button homeButton = createNavButton("Home");
        Button communityButton = createNavButton("Community");
        Button logoutButton = createNavButton("Logout");
        buttonsPane.getChildren().addAll(homeButton, communityButton, logoutButton);
        buttonsPane.setAlignment(Pos.CENTER);

        topBar.getChildren().addAll(iconView, title, buttonsPane);
        HBox.setMargin(buttonsPane, new Insets(0, 0, 0, 100));

        topBar.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.6);" +
                        "-fx-background-radius: 3;"
        );

        return topBar;
    }

    private Button createNavButton(String text) {
        Button btn = new Button(text);
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

    private ScrollPane createFeedArea() {
        feedContainer = new VBox(15);
        feedContainer.setPadding(new Insets(20));
        feedContainer.setStyle("-fx-background-color: #f0f2f5;");

        // Post Creation Card at the top
        VBox postCreationCard = createPostCreationCard();
        feedContainer.getChildren().add(postCreationCard);

        ScrollPane scrollPane = new ScrollPane(feedContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f0f2f5; -fx-background-color: #f0f2f5;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        return scrollPane;
    }

    private VBox createPostCreationCard() {
        VBox card = new VBox(12);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 8; " +
                "-fx-border-color: #dcdcdc; -fx-border-radius: 8; -fx-border-width: 1;");
        card.setMaxWidth(Double.MAX_VALUE);

        postInput = new TextArea();
        postInput.setPromptText("What's on your mind?");
        postInput.setPrefRowCount(3);

        postInput.setStyle("""
    -fx-font-size: 17;
    -fx-text-fill: black;
    -fx-focus-color: #8BA889;
    -fx-faint-focus-color: transparent;
    -fx-font-family: Verdant;
""");

        // Post button
        Button postBtn = new Button("Post");
        postBtn.setStyle("""
    -fx-background-color: linear-gradient(to right, #6B8E4E, #A0C48C);
    -fx-background-radius: 12;
    -fx-border-radius: 12;
    -fx-text-fill: white;
    -fx-font-weight: bold;
    -fx-font-size: 16;
    -fx-font-family: Verdant;
    -fx-cursor: hand;
""");
        postBtn.setPadding(new Insets(10, 30, 10, 30));

        postBtn.setOnMouseEntered(e->{
            postBtn.setEffect(addGlowEffect());

        });

        postBtn.setOnMouseExited(e->{
            postBtn.setEffect(null);
        });
        // Connect to server logic
        try {
            postBtn.setOnAction(e -> sendPost());
        }
        catch (Exception e) {
            System.out.println("Not connected to server!");
        }

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().add(postBtn);

        card.getChildren().addAll(postInput, buttonBox);

        return card;
    }

    private void sendPost() {
        String text = postInput.getText().trim();
        if (text.isEmpty()) return;

        try {
            Post newPost = new Post(Session.getUsername(), text);
            out.writeObject(newPost);
            out.flush();

            // Clear input correctly
            postInput.clear();

        } catch (Exception ex) {
            //ex.printStackTrace();
            System.out.println("Not connected to server!");
        }
    }


    private void initializeConnection() {
        try {
            Socket socket = new Socket("localhost", 6000);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // Listen for posts from server
            new Thread(() -> {
                try {
                    while (true) {
                        Post post = (Post) in.readObject();
                        Platform.runLater(() -> {
                            PostCard card = new PostCard(post.getUsername(), post.getContent());
                            feedContainer.getChildren().add(1, card);
                        });
                    }
                } catch (Exception e) {
                    System.out.println("Disconnected from server");
                }
            }).start();
        } catch (Exception e) {
            System.out.println("Not connected to server!");
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