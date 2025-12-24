package Server;

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import Common.Post;

public class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            System.out.println("ClientHandler started for: " + clientSocket.getInetAddress());
            SendFeed(out);

                while (true) {
                    Post receivedPost = (Post) in.readObject();
                    System.out.println("Received post: " + receivedPost);

                    PlantFeedServer.posts.add(receivedPost);

                    out.reset(); // important
                    SendFeed(out);
                    System.out.println("Feed sent back to client");
                }


        } catch (EOFException e) {
            System.out.println("Client disconnected: " + clientSocket.getInetAddress());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { clientSocket.close(); } catch (Exception e) {}
        }
    }

    private void SendFeed(ObjectOutputStream out) {
        try {
            out.writeObject(PlantFeedServer.posts);
            out.flush();
        }
         catch (Exception e) {}
    }
}

