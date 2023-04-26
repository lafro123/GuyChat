package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static String url = "jdbc:mysql://localhost:3306/appconv"; // lien vers la bdd
    private static String user = "root"; // identifiant pour la bdd
    private static String password = "root"; // mot de passe pour la bdd

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
        // creer la connection a la base de données au debut de chaque requete sql
    }
}
