package com.example.florify.common;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Post implements Serializable {
    private String username;
    private String content;

    public Post(String username, String content) {
        this.username = username;
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }
}

