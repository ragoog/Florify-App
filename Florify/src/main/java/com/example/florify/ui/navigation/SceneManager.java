package com.example.florify.ui.navigation;
import com.example.florify.ui.feed.FeedPage;
import com.example.florify.ui.login.LoginController;
import com.example.florify.ui.login.LoginView;
import com.example.florify.ui.main.MainView;
import com.example.florify.ui.main.PlantInfoFormView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
//import com.example.florify.ui.login.

public class SceneManager
{

    private static Stage primaryStage;

    // create an initialize method that sets stage to whatever stage it is passed to (only the first time)
    public static void init(Stage stage)
    {
        primaryStage = stage;
        stage.setResizable(false);
    }

    //
    public static void switchScene(Scene scene) // switch scenes after this will be used after the first time
    {
        try // to check if primaryStage was initialized or not because it is a static variable so init() has to be called first
        {
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Error in switch scene\nTRY USING init() first\n" + e.getMessage());
        }
    }

    public static LoginView loadLoginScene()
    {
        // for each scene u have to initialize it and its controller to make it functionable
        LoginView loginView = new LoginView();
        new LoginController(loginView);

        return loginView;
    }

    public static Scene loadMainScene()
    {
        try
        {
//            Stage tempStage = new Stage();   // dummy stage
            MainView mainView = new MainView();

            mainView.start(primaryStage);       // MainView sets the scene internally

            return primaryStage.getScene();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Scene loadFeedScene()
    {
        try
        {
            Stage tempStage = new Stage();   // dummy stage
            FeedPage feedPage = new FeedPage();

            feedPage.start(tempStage);       // MainView sets the scene internally

            return tempStage.getScene();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static Scene loadPlantInfoScene()
    {
        try
        {
            PlantInfoFormView plantInfoFormView = new PlantInfoFormView();
            plantInfoFormView.start(primaryStage);       // MainView sets the scene internally
            return primaryStage.getScene();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

}
