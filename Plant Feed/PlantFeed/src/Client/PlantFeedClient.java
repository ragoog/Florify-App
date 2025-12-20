package Client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import Common.Post;
public class PlantFeedClient {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = s.nextLine();


        try {
            //System.out.println("Connecting to server on localhost:6000...");
            Socket socket = new Socket("localhost", 6000);
            System.out.println("Connected to server!");

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            //System.out.println("Streams created successfully.");

            while (true) {
                System.out.print("Write post and type 'exit' to quit: ");
                String content = s.nextLine();

                if (content.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting client...");
                    socket.close();
                    break;
                }

                Post post = new Post(username, content);
                //System.out.println("Sending post: " + post);

                out.writeObject(post);
                out.flush();

                //System.out.println("Waiting for updated community feed...");
                List<Post> posts = (List<Post>) in.readObject();

                System.out.println("\n--- Community Feed ---");
                for (Post p : posts) {
                    System.out.println(p);
                }
                System.out.println("----------------------\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

