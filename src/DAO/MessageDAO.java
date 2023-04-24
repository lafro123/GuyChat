package DAO;

import java.sql.*;
import java.util.List;

public class MessageDAO {
    private Connection conn;

    public MessageDAO(Connection connection){
        this.conn = connection;
    }

    public String getAllMessages() {
        String msgContent = null;

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM message");

            while (rs.next()) {
                msgContent = rs.getString("text");
                Timestamp time = rs.getTimestamp("time");
                //System.out.println("content : " + msgContent + ", at : " + time);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return msgContent;
    }

    public void addMsg(String msg) {

        if(msg.startsWith("*")) {

        } else {
            String[] parts1 = msg.split(":");
            String userSender = parts1[0];
            String onlyTheMessage = parts1[1];

            try {
                Statement stmt = this.conn.createStatement();
                String queryMsg = "INSERT INTO message(text) VALUES('" + onlyTheMessage + "')";
                stmt.executeUpdate(queryMsg);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /*public void deleteMsg(String msg) {
        try {
            Statement stmt = this.conn.createStatement();
            String queryMsg = "DELETE FROM message(text) WHERE text = '" + msg + "'";
            stmt.executeUpdate(queryMsg);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/
}
