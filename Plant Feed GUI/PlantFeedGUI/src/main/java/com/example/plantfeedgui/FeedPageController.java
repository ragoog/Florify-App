package com.example.plantfeedgui;

import Common.Post;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import network.FeedNetworkClient;

public class FeedPageController {

    @FXML
    private VBox postsContainer;

    @FXML
    private TextField postTextField;

    @FXML
    private Button postButton;

    private FeedNetworkClient client;

    private String username;

    // Setter to be called from the main app after login
    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    public void initialize() {
        client = new FeedNetworkClient(post -> {
            // Callback runs on JavaFX thread via Platform.runLater inside client
            Label label = new Label(post.getUsername() + ": " + post.getContent());
            label.setWrapText(true);
            postsContainer.getChildren().add(label);
        });

        postButton.setOnAction(e -> {
            String text = postTextField.getText().trim();
            if (!text.isEmpty()) {
                Post post = new Post(username, text);
                client.sendPost(post);
                postTextField.clear();
            }
        });
    }
}

