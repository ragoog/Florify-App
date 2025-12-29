package com.example.florify.ui.feed;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class PostCard extends VBox {

    public PostCard(String usernameText, String contentText) {

        // Username label
        Label username = new Label(usernameText);
        username.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Content label
        Label contentLabel = new Label(contentText);
        contentLabel.setWrapText(true);
        contentLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14;");

        // VBox spacing, padding, styling
        setSpacing(8);
        setPadding(new Insets(12));
        setMaxWidth(Double.MAX_VALUE);
        setStyle("""
            -fx-background-color: #253528;
            -fx-background-radius: 12;
        """);

        getChildren().addAll(username, contentLabel);
    }
}

