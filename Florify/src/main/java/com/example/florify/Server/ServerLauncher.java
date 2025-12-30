package com.example.florify.Server;

public class ServerLauncher
{

    private static boolean started = false;

    public static synchronized void startFeedServer() {
        if (!started) {
            started = true;
            new Thread(() -> FeedServer.main(null)).start();
        }
    }
}