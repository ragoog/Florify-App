package Server;

import Common.Post;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FeedServer {

    public static final int PORT = 6000;
    // i added nth

    // Store all client output streams to be able to broadcast it
    private static final List<ObjectOutputStream> clients = new CopyOnWriteArrayList<>();

    // Keep all posts in server memory (temporary)
    private static final List<Post> allPosts = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        System.out.println("Florify Feed Server started...");

        //opening the server and listening on given port
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {
                //accepting clients
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");

                // Create streams
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                // Add output stream to list for broadcasting
                clients.add(out);

                // Send all previous posts to this new client
                for (Post post : allPosts) {
                    try {
                        out.writeObject(post);
                        out.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Start a thread to handle this client
                new Thread(() -> handleClient(in, out)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(ObjectInputStream in, ObjectOutputStream out) {
        try {
            while (true) {
                Post post = (Post) in.readObject();
                System.out.println("Received post from " + post.getUsername() + ": " + post.getContent());

                // Save post in memory
                allPosts.add(post);

                // Broadcast to all clients
                for (ObjectOutputStream clientOut : clients) {
                    try {
                        synchronized (clientOut) { // ensure thread-safe writing
                            clientOut.writeObject(post);
                            clientOut.flush();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        clients.remove(clientOut);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Client disconnected");
        }
    }
}


