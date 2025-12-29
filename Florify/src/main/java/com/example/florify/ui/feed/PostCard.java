package com.example.florify.ui.feed;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class PostCard extends VBox {

    public PostCard(String content) {

        Label username = new Label("Username");
        username.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        setSpacing(8);
        setPadding(new Insets(12));
        setMaxWidth(Double.MAX_VALUE);

        setStyle("""
            -fx-background-color: #253528;
            -fx-background-radius: 12;
        """);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label contentLabel = new Label(content);
        contentLabel.setWrapText(true);
        contentLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");
        getChildren().addAll(username, contentLabel);
    }
}
