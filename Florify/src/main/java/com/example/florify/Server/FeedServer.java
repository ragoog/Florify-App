package com.example.florify.Server;

import com.example.florify.common.Post;
import com.example.florify.db.Database;
import com.example.florify.db.PostDAO;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FeedServer {

    public static final int PORT = 6000;

    private static final List<ObjectOutputStream> clients =
            new CopyOnWriteArrayList<>();

    private static final List<Post> allPosts =
            new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        System.out.println("Florify Feed Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            Database.init();

            try {
                // add all the items from database to array list
                allPosts.addAll(PostDAO.loadAllPosts());
                System.out.println("Loaded " + allPosts.size() + " posts from database");
            } catch (SQLException e) {
                e.printStackTrace();
            }


            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");

                ObjectOutputStream out =
                        new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in =
                        new ObjectInputStream(clientSocket.getInputStream());

                clients.add(out);

                // Send previous posts to the new client
                for (Post post : allPosts) {
                    out.writeObject(post);
                    out.flush();
                }

                Thread clientThread = new Thread(() -> handleClient(in, out));
                clientThread.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(ObjectInputStream in,ObjectOutputStream out) {
        try {
            while (true) {
                Post post = (Post) in.readObject();

                System.out.println(
                        "Received post from " +
                                post.getUsername() +
                                ": " +
                                post.getContent()
                );

                PostDAO.savePost(post);     // to save in database

                allPosts.add(post);

                for (ObjectOutputStream clientOut : clients) {
                    try {
                        synchronized (clientOut) {
                            clientOut.writeObject(post);
                            clientOut.flush();
                        }
                    } catch (Exception e) {
                        clients.remove(clientOut);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Client disconnected");
            clients.remove(out);
        }
    }
}
