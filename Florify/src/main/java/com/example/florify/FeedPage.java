package com.example.florify;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Objects;

public class FeedPage extends Application
{
    public static  void main(String[] args)
    {
        launch(args);
    }

    public void  start(Stage stage)
    {
        //Main pane
    BorderPane root = new BorderPane();
    root.setPrefSize(1080,720);
    root.setStyle("-fx-background-color: #49654E;");
//        region background
//        String imagePath = "/com/example/florify/FeedPageBackground.png";
//        Image bg = new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm());
//        BackgroundSize backgroundSize = new BackgroundSize(100,100,true,true,true, true);
//        BackgroundImage backgroundImage = new BackgroundImage(
//                bg,
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundPosition.CENTER,
//                backgroundSize);
//        root.setBackground(new Background(backgroundImage));
//
//        //endregion

        // region homebutton
        VBox homeButton = new VBox();

        String imgPath = "/com/example/florify/home2.png";
        Image img = new Image(Objects.requireNonNull(getClass().getResource(imgPath)).toExternalForm());
        ImageView imageView = new ImageView(img);
        imageView.setFitHeight(35);
        imageView.setFitWidth(35);

        Button home= new Button();

        home.setGraphic(imageView);
        Label label = new Label("home");
        label.setStyle("-fx-font-size: 12;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: Verdant;"
                    + "-fx-text-fill: black;");


        homeButton.getChildren().addAll(home, label);
        homeButton.setAlignment(Pos.CENTER);
        home.setStyle("-fx-background-color: transparent; -fx-padding: 0;");

      //  root.setCenter(stackPane);
        BorderPane.setAlignment(homeButton, Pos.CENTER);

        //endregion

        //region community button

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
        Label commLabel = new Label("community");
        commLabel.setStyle("-fx-font-size: 12;"
                + "-fx-font-weight: bold;"
                + "-fx-font-family: Verdant;"
                + "-fx-text-fill: black;");

        comm.getChildren().addAll(commButton, commLabel);
        comm.setTranslateY(-1.5);

        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(100);
        buttons.setPadding(new Insets(10));
        buttons.setStyle("-fx-background-color: transparent;");
        buttons.setStyle("-fx-padding: 10;");
        buttons.getChildren().addAll(homeButton, comm);

        //endregion


        root.setBottom(buttons);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}