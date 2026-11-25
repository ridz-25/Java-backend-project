
package src.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/gigconnect";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "your_password";

    // Private constructor prevents object creation
    private DBConnection() {
    }

    // Establish database connection
    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
            URL,
            USERNAME,
            PASSWORD
        );
    }

    // Test database connection
    public static boolean testConnection() {

        try (Connection con = getConnection()) {

            return con != null && !con.isClosed();

        } catch (SQLException e) {

            System.out.println(
                "Database connection failed: " + e.getMessage()
            );

            return false;
        }
    }
}
