package com.example.florify.ui.navigation;
import com.almasb.fxgl.app.scene.StartupScene;
import com.example.florify.ui.feed.FeedPage;
import com.example.florify.ui.login.LoginController;
import com.example.florify.ui.login.LoginView;
import javafx.scene.Scene;
import javafx.stage.Stage;
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
}
