package com.example.florify.ui.feed;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.util.Duration;

public class PostCard extends VBox {

    public PostCard(String usernameText, String contentText) {

        // Username label
        Label username = new Label(usernameText);
        username.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1A1A1A; -fx-font-family: Verdant;");


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
            username.setTextFill(gradient);
        }));

        colorAnim.setCycleCount(Animation.INDEFINITE);
        colorAnim.play();

        // Content label
        Label contentLabel = new Label(contentText);
        contentLabel.setWrapText(true);
        contentLabel.setStyle("-fx-text-fill: #333333; -fx-font-size: 15; -fx-font-family: Verdant;");

        // VBox spacing, padding, styling
        setSpacing(8);
        setPadding(new Insets(12));
        setMaxWidth(Double.MAX_VALUE);
        setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 12;
            -fx-border-color: #dcdcdc;
            -fx-border-radius: 12;
            -fx-border-width: 1;
            -fx-opacity: 0.8;
            -fx-padding: 10;
        """);

        // drop shadow when mouse enter
        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setOffsetY(2);
        shadow.setColor(Color.rgb(0, 0, 0, 0.08));
        setEffect(shadow);

        setOnMouseEntered(e -> {
            shadow.setRadius(14);
            shadow.setOffsetY(4);
            shadow.setColor(Color.rgb(0, 0, 0, 0.12));
            setOpacity(1.0);
        });

        setOnMouseExited(e -> {
            shadow.setRadius(10);
            shadow.setOffsetY(2);
            shadow.setColor(Color.rgb(0, 0, 0, 0.08));
            setOpacity(0.92);
        });



        getChildren().addAll(username, contentLabel);
    }
}
