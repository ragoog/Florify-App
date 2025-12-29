package com.example.florify.ui.login;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;
import java.util.Random;

public class LoginView
{
    private BorderPane primary;
    private Scene scene;

    private TextField username;
    private PasswordField password;
    private PasswordField confirmPassword;
    private Button loginButton;
    private Button registerLoginButton;
    private Label title;
    private Label subtitle;
    private VBox contentPane;

    // create an enum for the RegisterLoginButton state
    enum ButtonState { LOGIN, REGISTER }
    private ButtonState currentButtonState = ButtonState.REGISTER;


    // constructor
    public LoginView()
    {
        InitializeUI();
    }

    // this method initializes the ui
    private void InitializeUI()
    {
        // initialize primary pane
        primary = new BorderPane();
        primary.setPrefSize(1080, 720);

        // initialize content pane
        contentPane = new VBox(10);
        contentPane.setSpacing(50);
        contentPane.setStyle("-fx-background-color: #253528; -fx-opacity : 0.9;");
        contentPane.setPrefWidth(520);
        contentPane.setAlignment(Pos.BASELINE_LEFT);

        // initialize labels
        title = new Label("Login");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 80; -fx-font-family: Verdant; ");

        subtitle = new Label("Login with a valid username and password");
        subtitle.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-font-family: Verdant;");

        // initialize textFields
        username = createStyledTextField();
        username.setPromptText("Username");

        password = createStyledPasswordField();
        password.setPromptText("Password");

        confirmPassword = createStyledPasswordField();
        confirmPassword.setPromptText("Confirm Password");

        // initialize buttons
        loginButton = createStyledButton();
        registerLoginButton = createStyledRegisterButton();

        // align everything and add them to the contentPane
        contentPane.getChildren().addAll(title, subtitle, username, password, loginButton);
        VBox.setMargin(title, new Insets(30,20,10,10));
        VBox.setMargin(subtitle, new Insets(10,20,10,10));
        VBox.setMargin(username, new Insets(5,20,10,10));
        VBox.setMargin(password, new Insets(5,20,10,10));
        VBox.setMargin(confirmPassword, new Insets(5,20,10,10));
        VBox.setMargin(loginButton, new Insets(5,20,10,20));

        // add a Hbox to align the sidepanel with button
        HBox topBar = new HBox();
        topBar.getChildren().addAll(contentPane, registerLoginButton);
        primary.setLeft(topBar);


        // change primaryPane background
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

        scene = new Scene(primary, 1080, 720);
    }

    //region Getters
    //  for everything to reference from LoginController and FlorifyApp
    public Scene getScene() { return scene; }
    public VBox getContentPane() { return contentPane; }
    public Button getRegisterButton() { return registerLoginButton; }
    public Button getLoginButton() { return loginButton; }
    public TextField getUsernameField() { return username; }
    public PasswordField getPasswordField() { return password; }
    public PasswordField getConfirmPasswordField() { return confirmPassword; }
    public Label getTitleLabel() { return title; }
    public Label getSubtitleLabel() { return subtitle; }
    public ButtonState getCurrentButtonState() { return currentButtonState; }
    //endregion

    // methods related to the style
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
    // methods related to the style
    private PasswordField createStyledPasswordField() {
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle(
                "-fx-background-color: transparent; " +
                        "-fx-text-fill: white; " +
                        "-fx-border-color: transparent transparent #444444 transparent; " +
                        "-fx-border-width: 0 0 1 0; " +
                        "-fx-padding: 5 0 5 0; " +
                        "-fx-font-size: 20px;" +
                        "-fx-font-family: Verdant"
        );
        passwordField.setPrefWidth(300);

        // Focus effect
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                passwordField.setStyle(
                        "-fx-background-color: transparent; " +
                                "-fx-text-fill: white; " +
                                "-fx-border-color: transparent transparent #6B9E5A transparent; " +
                                "-fx-border-width: 0 0 2 0; " +
                                "-fx-padding: 5 0 5 0; " +
                                "-fx-font-size: 20px;" +
                                "-fx-font-family: Verdant"
                );
            } else {
                passwordField.setStyle(
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

        return passwordField;
    }
    private Button createStyledButton() {

        Button btn = new Button("LOGIN");
        btn.setPrefWidth(200);
        btn.setPrefHeight(40);

        // Normal gradient + no glow
        btn.setStyle(
                "-fx-background-color: linear-gradient(to right, #5A7C5F, #6B8E4E);" +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-background-radius: 50;" +
                        "-fx-font-family: Verdant;"
        );

        // Hover effect: gradient + subtle glow
        btn.setOnMouseEntered(e -> {
            btn.setStyle(
                    "-fx-background-color: linear-gradient(to right, #8BA889, #A4C48B);" +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 20px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-background-radius: 50;" +
                            "-fx-font-family: Verdant;"
            );
            btn.setEffect(addGlowEffect()); // add glow
        });

        // return back to normal
        btn.setOnMouseExited(e -> {
            btn.setStyle(
                    "-fx-background-color: linear-gradient(to right, #5A7C5F, #6B8E4E);" +
                            "-fx-text-fill: white; " +
                            "-fx-font-size: 20px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-background-radius: 50;" +
                            "-fx-font-family: Verdant;"
            );
            btn.setEffect(null); // remove glow
        });

        // Optional: click effect (press)
        btn.setOnMousePressed(e -> btn.setEffect(addGlowEffect()));
        btn.setOnMouseReleased(e -> btn.setEffect(null));
        return btn;
    }
    private Button createStyledRegisterButton() {
        Button signUpButton = new Button("register");
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
    private DropShadow addGlowEffect() {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#6B8E4E"));
        glow.setRadius(10);
        glow.setSpread(0.2);

        return glow;
    }

    // methods for button state
    public void slideToLoginAnimation(int milliseconds, Pane pane, Button registerLoginButton,
                              Label title, Label subtitle, TextField username,
                              TextField confirmPassword, Button loginButton)
    {
        TranslateTransition slidePane = new TranslateTransition(Duration.millis(milliseconds), pane);
        TranslateTransition slideButton = new TranslateTransition(Duration.millis(800), registerLoginButton);

        // change texts to the login texts
        registerLoginButton.setText("login");
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

        // change the state back to register
        currentButtonState = ButtonState.LOGIN;
    }
    public void slideToRegisterAnimation(int milliseconds, Pane pane, Button registerLoginButton,
                                 Label title, Label subtitle, TextField username, Button loginButton)
    {
        TranslateTransition slidePane = new TranslateTransition(Duration.millis(milliseconds), pane);
        TranslateTransition slideButton = new TranslateTransition(Duration.millis(800), registerLoginButton);

        // change texts to the register texts
        registerLoginButton.setText("register");
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

        // change it back to log in
        currentButtonState = ButtonState.REGISTER;
    }
    // playLeafAnimation(primary);
}
//region leaf animation
//    private void playLeafAnimation(BorderPane root) {
//        Image leaf = new Image(getClass().getResource("/com/example/florify/9c4e4505-0b82-4eb5-ab24-fe81d8833b01.png").toExternalForm());
//        Random random = new Random();
//
//        for (int i = 0; i < 10; i++) {
//            ImageView leafView = new ImageView(leaf);
//
//            // Random horizontal start
//            double startX = random.nextDouble() * 1080;
//            leafView.setTranslateX(startX);
//
//            // Randomize scale and opacity for natural look
//            double scale = 0.05 + random.nextDouble() * 0.05;
//            leafView.setScaleX(scale);
//            leafView.setScaleY(scale);
//            leafView.setOpacity(0.2 + random.nextDouble() * 0.3);
//
//            root.getChildren().add(leafView);
//
//            // Falling animation
//            TranslateTransition leafTransition = new TranslateTransition(Duration.seconds(10 + random.nextDouble() * 5), leafView);
//            leafTransition.setFromY(-50 - random.nextDouble() * 300); // start above screen
//            leafTransition.setToY(720 + 50); // fall past bottom
//            leafTransition.setCycleCount(TranslateTransition.INDEFINITE);
//            leafTransition.setInterpolator(Interpolator.LINEAR);
//
//            // Random rotation
//            RotateTransition rotate = new RotateTransition(Duration.seconds(3 + random.nextDouble() * 3), leafView);
//            rotate.setByAngle(360);
//            rotate.setCycleCount(Animation.INDEFINITE);
//            rotate.setInterpolator(Interpolator.LINEAR);
//
//            leafTransition.play();
//            rotate.play();
//        }
//    }
//endregion