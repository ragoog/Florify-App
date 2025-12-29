package com.example.florify.common;

public class Session {

    private static String username;
    public static void setUsername(String u) {
        username = u;
    }

    public static String getUsername() {
        return username;
    }
}
