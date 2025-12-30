package com.example.florify.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database
{
    public static final String DB_URL = "jdbc:sqlite:florifyUsers.db";
    public static Connection connection;

    public static void init()
    {
        String sqlQuery = """
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            email TEXT NOT NULL UNIQUE,
            username TEXT NOT NULL,
            password TEXT NOT NULL
            );
            """;

        String sqlQuery2 = """
        CREATE TABLE IF NOT EXISTS posts (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT NOT NULL,
            content TEXT NOT NULL,
            timestamp DATETIME DEFAULT CURRENT_TIMESTAMP
        );
        """;


        try(Connection connection = Database.getConnection();
            Statement statement = connection.createStatement())
        {
            statement.execute(sqlQuery);
            statement.execute(sqlQuery2);
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("Connection to database failed.");
        }
    }

    public static Connection getConnection() throws SQLException
    {
        if(connection==null || connection.isClosed())
            connection = DriverManager.getConnection(DB_URL);

        return connection;
    }
}
