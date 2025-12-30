package com.example.florify;

import com.example.florify.ui.login.LoginController;
import com.example.florify.ui.login.LoginView;
import com.example.florify.ui.navigation.SceneManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FlorifyApp extends Application {

    private static Stage currentStage;

    @Override
    public void start(Stage stage)
    {
        stage.setTitle("Florify");
        // first initialize it
        SceneManager.init(stage);

        // load scene from SceneManager and save it in loginView
        LoginView loginView = SceneManager.loadLoginScene();

        SceneManager.switchScene(loginView.getScene());
    }

    public static void setScene(Scene scene)
    {
        currentStage.setScene(scene);
    }

    public static void main(String[] args) { launch(args); }
}
