package network;

import Common.Post;
import javafx.application.Platform;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class FeedNetworkClient {

    private ObjectOutputStream out;
    private ObjectInputStream in;

    public FeedNetworkClient(Consumer<Post> onPostReceived) {
        try {
            Socket socket = new Socket("localhost", 6000);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            new Thread(() -> {
                try {
                    while (true) {
                        Post post = (Post) in.readObject();
                        Platform.runLater(() -> onPostReceived.accept(post));
                    }
                } catch (Exception e) {
                    System.out.println("Disconnected from server");
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendPost(Post post) {
        try {
            System.out.println("Sending post: " + post);
            out.writeObject(post);
            out.flush();
            System.out.println("Post sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
