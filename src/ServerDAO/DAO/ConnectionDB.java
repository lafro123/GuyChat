package ServerDAO.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static String url = "jdbc:mysql://localhost:3306/appconv";
    private static String user = "root"; // voir si on se créer pas des profils
    private static String password = "root";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
