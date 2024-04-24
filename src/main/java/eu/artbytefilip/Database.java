package eu.artbytefilip;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public Connection connection;

    private int port;
    private String host, database, username, password;

    public Database(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public void connectToDatabase() {
        try {
            // Načítanie JDBC ovládača
            Class.forName("com.mysql.jdbc.Driver");

            // Pripojenie k databáze
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + "/" + database,
                    username, password);

            System.out.println("Pripojenie k databáze bolo úspešné.");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Nepodarilo sa pripojiť k databáze: " + e.getMessage());
        }
    }

    public void disconnectFromDatabase() {
        try {
            // Odpojenie od databázy
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Odpojenie od databázy bolo úspešné.");
            }
        } catch (SQLException e) {
            System.out.println("Nepodarilo sa odpojiť od databázy: " + e.getMessage());
        }
    }
}
