package com.example.florify.ui.feed;

import com.example.florify.common.Post;
import com.example.florify.common.Session;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class FeedPage extends Application {

    private ObjectOutputStream out;
    private ObjectInputStream in;

    private VBox postsContainer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Main pane
        BorderPane root = new BorderPane();
        root.setPrefSize(1080, 720);

        //region Background
        String imagePath = "/com/example/florify/FeedPageBackground.png";
        Image bg = new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, true);
        BackgroundImage backgroundImage = new BackgroundImage(bg, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        root.setBackground(new Background(backgroundImage));
        //endregion

        // region Home button
        VBox homeButton = new VBox();
        String imgPath = "/com/example/florify/home2.png";
        Image img = new Image(Objects.requireNonNull(getClass().getResource(imgPath)).toExternalForm());
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(35);
        imageView.setFitWidth(35);
        Button home = new Button();
        home.setGraphic(imageView);

        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.18);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(1);

        home.setOnMouseEntered(e -> {
            home.setEffect(colorAdjust);
            home.setEffect(dropShadow);
        });
        home.setOnMouseExited(e -> home.setEffect(null));

        Label label = new Label("home");
        label.setStyle("-fx-font-size: 12;-fx-font-weight: bold;-fx-font-family: Verdant;-fx-text-fill: black;");
        homeButton.getChildren().addAll(home, label);
        homeButton.setAlignment(Pos.CENTER);
        home.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

        //endregion

        // region Community button
        VBox comm = new VBox();
        comm.setAlignment(Pos.CENTER);
        Button commButton = new Button();
        String imgPath2 = "/com/example/florify/group.png";
        Image commImg = new Image(Objects.requireNonNull(getClass().getResource(imgPath2)).toExternalForm());
        ImageView imageView2 = new ImageView(commImg);
        imageView2.setFitHeight(35);
        imageView2.setFitWidth(35);
        commButton.setGraphic(imageView2);
        commButton.setStyle("-fx-background-color: transparent; -fx-padding: 0;");
        commButton.setAlignment(Pos.CENTER);
        commButton.setOnMouseEntered(e -> {
            commButton.setEffect(colorAdjust);
            commButton.setEffect(dropShadow);
        });
        commButton.setOnMouseExited(e -> commButton.setEffect(null));

        Label commLabel = new Label("community");
        commLabel.setStyle("-fx-font-size: 12;-fx-font-weight: bold;-fx-font-family: Verdant;-fx-text-fill: black;");
        comm.getChildren().addAll(commButton, commLabel);
        comm.setTranslateY(-1.5);

        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(100);
        buttons.setPadding(new Insets(10));
        buttons.setStyle("-fx-background-color: transparent;-fx-padding: 10;");
        buttons.getChildren().addAll(homeButton, comm);

        //endregion

        // region Post button and post textfield
        VBox v = new VBox();
        v.setPrefWidth(900);
        v.setPrefHeight(900);
        v.setMaxWidth(900);
        v.setMaxHeight(900);

        HBox h = new HBox(1);
        h.setPrefWidth(v.getPrefWidth());
        h.setAlignment(Pos.CENTER);
        Button postButton = new Button("Post");
        v.setAlignment(Pos.CENTER);
        TextField post = new TextField();
        post.setPromptText("What’s on your mind?..");
        post.setPrefHeight(200);
        post.setPrefWidth(200);
        post.setStyle("-fx-control-inner-background:#253528;-fx-border-color:#253528;-fx-background-radius: 8;"
                + "-fx-border-radius: 8;-fx-prompt-text-fill: grey;-fx-focus-color: transparent;"
                + "-fx-faint-focus-color: transparent;-fx-text-fill: grey;-fx-font-size: 18;-fx-font-family: Verdant;");

        postButton.setStyle("-fx-background-color:#253528;-fx-border-color:#253528;-fx-background-radius: 8;"
                + "-fx-border-radius: 8;-fx-prompt-text-fill: grey;-fx-focus-color: transparent;"
                + "-fx-faint-focus-color: transparent;-fx-text-fill: grey;-fx-font-size: 18;-fx-font-family: Verdant;");

        postButton.setOnMouseEntered(e -> postButton.setEffect(addGlowEffect()));
        postButton.setOnMouseExited(e -> postButton.setEffect(null));
        postButton.setOnMousePressed(e -> postButton.setEffect(addGlowEffect()));
        postButton.setOnMouseReleased(e -> postButton.setEffect(null));

        HBox.setHgrow(post, Priority.ALWAYS);
        post.setAlignment(Pos.CENTER);
        h.getChildren().addAll(post, postButton);

        //endregion

        // region Posts container
        postsContainer = new VBox(10);
        postsContainer.setPadding(new Insets(10));
        postsContainer.setFillWidth(true);
        postsContainer.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));

        ScrollPane scrollPane = new ScrollPane(postsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(800);
        scrollPane.setPrefWidth(800);
        scrollPane.setStyle("-fx-background-color: #8BA889;-fx-opacity: 0.3;");

        v.getChildren().addAll(scrollPane, h);

        root.setCenter(v);
        root.setBottom(buttons);
        //endregion

        //region Scene
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        //endregion

        // Initialize server connection
        initializeConnection();

        // Handle post button action
        postButton.setOnAction(e -> sendPost(post));
    }

    private void sendPost(TextField postField) {
        String text = postField.getText().trim();
        if (text.isEmpty()) return;
        try {
            Post newPost = new Post(Session.getUsername(), text);
            out.writeObject(newPost);
            out.flush();
            postField.clear();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initializeConnection() {
        try {
            Socket socket = new Socket("localhost", 6000);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());


            new Thread(() -> {
                try {
                    while (true) {
                        Post post = (Post) in.readObject();
                        Platform.runLater(() -> {
                            PostCard card = new PostCard(post.getUsername(), post.getContent());
                            postsContainer.getChildren().add(0, card);
                        });
                    }
                } catch (Exception e) {
                    System.out.println("Disconnected from server");
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DropShadow addGlowEffect() {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#6B8E4E"));
        glow.setRadius(15);
        glow.setSpread(0.15);
        return glow;
    }
}

