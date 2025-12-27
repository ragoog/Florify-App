package com.example.florify;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.w3c.dom.html.HTMLButtonElement;

import java.util.Objects;

public class LoginPage extends Application
{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        // main pane
        BorderPane primary = new BorderPane();
        primary.setPrefSize(1080, 720);

        // content pane
        VBox contentPane = new VBox(10);
        contentPane.setSpacing(50);
        contentPane.setStyle("-fx-background-color: #253528; -fx-opacity : 0.9;");
        contentPane.setPrefWidth(520);

        // top bar
        HBox topBar = new HBox();


        //region Content

        // load font
        Label title = new Label("Login");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 80; -fx-font-family: Verdant; ");

        Label subtitle = new Label("Login with a valid username and password");
        subtitle.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-font-family: Verdant;");


        TextField username = createStyledTextField();
        username.setPromptText("Username");

        TextField password = createStyledTextField();
        password.setPromptText("Password");

        TextField confirmPassword = createStyledTextField();
        confirmPassword.setPromptText("Confirm Password");

        // Login button
        Button loginButton = createStyledButton();

        Button registerLoginButton = createStyledRegisterButton();
        registerLoginButton.setOnAction(e -> {slideAnimation(700, contentPane, registerLoginButton,
                title, subtitle, username, confirmPassword, loginButton);});




        // region Background Image

        // image link here
        String imgLink = "/com/example/florify/image (1).png";
        Image bg = new Image(Objects.requireNonNull(getClass().getResource(imgLink)).toExternalForm());

        // initializing background image
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,true,true,true,true);
        BackgroundImage backgroundImage = new BackgroundImage(
                bg,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize
        );

        // setting the background image
        primary.setBackground(new Background(backgroundImage));
        //endregion


        contentPane.getChildren().addAll(title, subtitle, username, password, loginButton);

        VBox.setMargin(title, new Insets(30,20,10,10));
        VBox.setMargin(subtitle, new Insets(10,20,10,10));
        VBox.setMargin(username, new Insets(5,20,10,10));
        VBox.setMargin(password, new Insets(5,20,10,10));
        VBox.setMargin(confirmPassword, new Insets(5,20,10,10));
        VBox.setMargin(loginButton, new Insets(5,20,10,20));
        contentPane.setAlignment(Pos.BASELINE_LEFT);

        topBar.getChildren().addAll(contentPane, registerLoginButton);
        primary.setLeft(topBar);

        Scene scene = new Scene(primary);  // set main primary pane
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private TextField createStyledTextField() {
        TextField textField = new TextField();
        textField.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: white; " +
                        "-fx-border-color: transparent transparent #444444 transparent; " +
                        "-fx-border-width: 0 0 1 0; " +
                        "-fx-padding: 5 0 5 0; " +
                        "-fx-font-size: 20px;" +
                        "-fx-font-family: Verdant"
        );
        textField.setPrefWidth(300);

        // Focus effect
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                textField.setStyle(
                        "-fx-background-color: transparent; " +
                                "-fx-text-fill: white; " +
                                "-fx-border-color: transparent transparent #6B9E5A transparent; " +
                                "-fx-border-width: 0 0 2 0; " +
                                "-fx-padding: 5 0 5 0; " +
                                "-fx-font-size: 20px;" +
                                "-fx-font-family: Verdant"
                );
            } else {
                textField.setStyle(
                        "-fx-background-color: transparent; " +
                                "-fx-text-fill: white; " +
                                "-fx-border-color: transparent transparent #444444 transparent; " +
                                "-fx-border-width: 0 0 1 0; " +
                                "-fx-padding: 5 0 5 0; " +
                                "-fx-font-size: 20px;" +
                                "-fx-font-family: Verdant"
                );
            }
        });

        return textField;
    }
    private Button createStyledButton() {
        Button loginButton = new Button("LOGIN");
        loginButton.setPrefWidth(200);
        loginButton.setPrefHeight(40);
        loginButton.setStyle(
                "-fx-background-color: #5A7C5F; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 50;" +
                        "-fx-font-family: Verdant;" +
                        "-fx-opacity: 1.0;"
        );

        loginButton.setOnMouseEntered(e -> loginButton.setStyle(
                "-fx-background-color: #8BA889; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 50;" +
                        "-fx-font-family: Verdant;" +
                        "-fx-opacity: 1.0;"
        ));

        loginButton.setOnMouseExited(e -> loginButton.setStyle(
                "-fx-background-color: #5A7C5F; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 50;" +
                        "-fx-font-family: Verdant;" +
                        "-fx-opacity: 1.0;"
        ));
        return loginButton;
    }
    private Button createStyledRegisterButton() {
        Button signUpButton = new Button("REGISTER");
        signUpButton.setPrefWidth(150);
        signUpButton.setPrefHeight(40);
        signUpButton.setStyle(
                "-fx-background-color: #253528; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand; " +
                        "-fx-background-radius: 0;" +
                        "-fx-border-radius: 0;" +
                        "-fx-font-family: Verdant;" +
                        "-fx-opacity: 0.95;"
        );

// Add hover effects for sign up button
        signUpButton.setOnMouseEntered(e -> signUpButton.setStyle(
                "-fx-background-color: #253528; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 0;" +
                        "-fx-border-radius: 0;" +
                        "-fx-cursor: hand; " +
                        "-fx-font-family: Verdant; " +
                        "-fx-opacity: 0.95;"
        ));

        signUpButton.setOnMouseExited(e -> signUpButton.setStyle(
                "-fx-background-color: #253528; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 0;" +
                        "-fx-border-radius: 0;" +
                        "-fx-cursor: hand; " +
                        "-fx-font-family: Verdant; " +
                        "-fx-opacity: 0.95;"
        ));

        signUpButton.setOnMousePressed(e -> signUpButton.setStyle(
                "-fx-background-color: #253528; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 0;" +
                        "-fx-border-radius: 0;" +
                        "-fx-cursor: hand; " +
                        "-fx-font-family: Verdant; " +
                        "-fx-opacity: 0.95;"
        ));
        return  signUpButton;
    }
    private void slideAnimation(int milliseconds, Pane pane, Button registerLoginButton,
                                Label title, Label subtitle, TextField username, TextField confirmPassword, Button loginButton)
    {
        TranslateTransition slidePane = new TranslateTransition(Duration.millis(milliseconds), pane);
        TranslateTransition slideButton = new TranslateTransition(Duration.millis(800), registerLoginButton);


        if(registerLoginButton.getText().equals("REGISTER"))
        {
            // change texts to the login texts
            registerLoginButton.setText("LOGIN");
            title.setText("Sign up");
            subtitle.setText("Enter a valid email and password");
            username.setPromptText("Email");
            loginButton.setText("SIGN UP");

            pane.getChildren().add(4, confirmPassword);

            // Animate pane sliding to the left
            slidePane.setToX(560);
            slidePane.play();

            // Animate the signup button moving to the left
            slideButton.setToX(-110); // Move to the left (opposite direction)
            slideButton.play();
        }
        else if(registerLoginButton.getText().equals("LOGIN"))
        {
            // change texts to the register texts
            registerLoginButton.setText("REGISTER");
            title.setText("Login");
            subtitle.setText("Login with a valid username and password");
            username.setPromptText("Username");
            loginButton.setText("LOGIN");

            // remove the confirm password button
            pane.getChildren().remove(4);

            // animate pane sliding to the right
            slidePane.setToX(0);
            slidePane.play();

            // animate button sliding to the right
            slideButton.setToX(0);
            slideButton.play();
        }
    }
}