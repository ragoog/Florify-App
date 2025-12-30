package com.example.florify.db;

import com.example.florify.common.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO
{

    public static void savePost(Post post) throws SQLException {
        String sql = "INSERT INTO posts (username, content) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, post.getUsername());
            ps.setString(2, post.getContent());
            ps.executeUpdate();
        }
    }

    public static List<Post> loadAllPosts() throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT username, content FROM posts ORDER BY id ASC";

        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                posts.add(new Post(
                        rs.getString("username"),
                        rs.getString("content")
                ));
            }
        }
        return posts;
    }
}
