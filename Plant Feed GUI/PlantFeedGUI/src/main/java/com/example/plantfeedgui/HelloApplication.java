package com.example.plantfeedgui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.Optional;

public class HelloApplication extends Application {

    private String username; // store the dynamic username

    @Override
    public void start(Stage stage) throws IOException {


        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Username");
        dialog.setHeaderText("Welcome to Florify!");
        dialog.setContentText("Username:");
        dialog.setGraphic(null);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            username = result.get().trim();
            if (username.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username cannot be empty!");
                alert.showAndWait();
                alert.setGraphic(null);
                Platform.exit();
                return;
            }
        } else {
            Platform.exit();
            return;
        }


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);


        FeedPageController controller = fxmlLoader.getController();
        controller.setUsername(username);

        stage.setTitle("Feed - " + username);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}


