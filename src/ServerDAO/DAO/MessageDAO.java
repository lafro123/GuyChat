package DAO;

import java.sql.*;
import java.util.List;

public class MessageDAO<Message> {
    private Connection conn;

    public MessageDAO(Connection connection){
        this.conn = connection;
    }

    public String getAllMessages() {
        // recupere tous les messages de la base de données avec leur contenu (text) et leur heure (time)
        String msgContent = null;

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM message");

            while (rs.next()) {
                msgContent = rs.getString("text");
                Timestamp time = rs.getTimestamp("time");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return msgContent;
    }

    public void addMsg(String msg) {
        // ajouter le message a la bdd des qu'il est envoye sur le serveur
        if(msg.startsWith("*")) {

        } else {
            String[] parts1 = msg.split(":");
            String userSender = parts1[0];
            String onlyTheMessage = parts1[1];

            try {
                Statement stmt = this.conn.createStatement();
                String queryMsg = "INSERT INTO message(userSender, text) VALUES('" + userSender + "','" + onlyTheMessage + "')";
                stmt.executeUpdate(queryMsg);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void deleteMsg(String msg) {
        // supprimer une message dans la table message (pas utiliser dans le code car manque de temps)
        try {
            Statement stmt = this.conn.createStatement();
            String queryMsg = "DELETE FROM message(text) WHERE text = '" + msg + "'";
            stmt.executeUpdate(queryMsg);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
