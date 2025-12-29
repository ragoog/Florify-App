package com.example.florify.db;

import java.sql.*;

public class UserDAO
{
    // Register a new user

    public static boolean registerUser(String username, String email, String password)
    {
        String sqlQuery = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

        try(Connection connection = Database.getConnection();       // equivalent to closing the connections at the end
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery))
        {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);

            preparedStatement.execute();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println("Error registering user: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Maybe account already exists?");
            return false;
        }
    }

    public static boolean loginUser(String username, String password)
    {
        String sqlQuery = "SELECT * FROM users WHERE username = ? AND password = ?";

        try(Connection connection = Database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery))
        {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            // will add true or false according to if it was done correctly
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        }
        catch (SQLException e)
        {
            System.out.println("Error login user: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Username or password is incorrect.");
            return false;
        }
    }

    public static void dropUsersTable() {
        String sqlQuery = "DROP TABLE IF EXISTS users";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlQuery);
            System.out.println("Users table dropped successfully.");
        } catch (SQLException e) {
            System.err.println("Error dropping users table: " + e.getMessage());
        }
    }
}