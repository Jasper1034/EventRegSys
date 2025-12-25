package eventreg2;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/eventregistrationsystem"; 
    private static final String USER = "root";
    private static final String PASSWORD = "Alif##1235";
    
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Success: Database Connected...");
            }
        } catch (Exception e) {
            System.err.println("Failed: Connection Failed!!! " + e.getMessage());
        }
        return connection;
    }
}