package com.example.florify;

import javafx.application.Application;
import javafx.scene.Scene;
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


        //region background
        String imagePath = "/com/example/florify/FeedPageBackground.png";
        Image bg = new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(100,100,true,true,true, true);
        BackgroundImage backgroundImage = new BackgroundImage(
                bg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);
        root.setBackground(new Background(backgroundImage));

        //endregion

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}