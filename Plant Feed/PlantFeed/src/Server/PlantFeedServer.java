package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import Common.Post;

public class PlantFeedServer {

    // Thread-safe list for posts
    public static List<Post> posts = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(6000);
            System.out.println("🌱 PlantFeed Server started on port 6000");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New connection from " + clientSocket.getInetAddress());

                // Start a new thread for each client
                ClientHandler handler = new ClientHandler(clientSocket);
                handler.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

