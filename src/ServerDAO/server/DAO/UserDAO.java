package ServerDAO.server.DAO;

import MVC.model.Utilisateur;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.List;

import static MVC.model.Utilisateur.Status.*;

public class UserDAO implements DAO<Utilisateur> {
    private Connection conn;

    public UserDAO(Connection connection){
        this.conn = connection;
    }
    public Utilisateur.UserType getUserTypeByUsername(String username) {
        Utilisateur.UserType type = null;

        //code pour récupérer un utilisateur en utilisant l'identifiant
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT type FROM user WHERE username = ?");

            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                type = Utilisateur.UserType.valueOf(rs.getString("type"));
            } else {
                System.out.println("problème de lecture du type dans la bdd");
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return type;
    }

    public List<Utilisateur> getAllUsers() {
        return null;
    }

    public void setStatus(String name, String status) {

        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE user SET status = ? WHERE username = ?");
            stmt.setString(1, status);
            stmt.setString(2, name);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getListUserCo() {
        String userCo = "", userDeco = "", userAway = "", listStatusUsers = "", nomCo = "", nomDeco = "", nomAway = "";
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT username, status FROM user");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String status = rs.getString("status");
                String username = rs.getString("username");

                if (status.equals(String.valueOf(ONLINE))) {
                    userCo += username + " ";
                } else if (status.equals(String.valueOf(OFFLINE))) {
                    userDeco += username + " ";
                } else if (status.equals(String.valueOf(AWAY))) {
                    userAway += username + " ";
                }
            }
            rs.close();

            listStatusUsers = "/co: " + userCo + "usernameDeco: " + userDeco + "usernameAway: " + userAway;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(listStatusUsers);
        return listStatusUsers;

    }

    public void updateUserTypeByUsername(String name, String type) {
        //code pour mettre à jour le type de l'utilisateur

        try {
            Statement stmt = conn.createStatement();
            String query = "UPDATE user SET type = '" + Enum.valueOf(Utilisateur.UserType.class, type) + "' WHERE username = '" + name + "'";
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String nbStats() {
        int nbBan = 0, nbOFF = 0, nbON = 0, nbAway = 0, nbAdmin = 0, nbMod = 0, nbClassic = 0;
        String result;
        try {
            Statement stmt = this.conn.createStatement();
            ResultSet rsBan = stmt.executeQuery("SELECT COUNT(*) AS ban FROM user WHERE ban = 1");
            if (rsBan.next()) {
                nbBan = rsBan.getInt("ban");
            }
            rsBan.close(); // Fermer le ResultSet après avoir obtenu les données nécessaires

            ResultSet rsOFF = stmt.executeQuery("SELECT COUNT(*) AS status FROM user WHERE status = 'OFFLINE'");
            if (rsOFF.next()) {
                nbOFF = rsOFF.getInt("status");
            }
            rsOFF.close(); // Fermer le ResultSet après avoir obtenu les données nécessaires

            ResultSet rsON = stmt.executeQuery("SELECT COUNT(*) AS status FROM user WHERE status = 'ONLINE'");
            if (rsON.next()) {
                nbON = rsON.getInt("status");
            }
            rsON.close(); // Fermer le ResultSet après avoir obtenu les données nécessaires

            ResultSet rsAway = stmt.executeQuery("SELECT COUNT(*) AS status FROM user WHERE status = 'AWAY'");
            if (rsAway.next()) {
                nbAway = rsAway.getInt("status");
            }
            rsAway.close(); // Fermer le ResultSet après avoir obtenu les données nécessaires

            ResultSet rsAdmin = stmt.executeQuery("SELECT COUNT(*) AS type FROM user WHERE type = 'ADMINISTRATOR'");
            if (rsAdmin.next()) {
                nbAdmin = rsAdmin.getInt("type");
            }
            rsAdmin.close(); // Fermer le ResultSet après avoir obtenu les données nécessaires

            ResultSet rsMod = stmt.executeQuery("SELECT COUNT(*) AS type FROM user WHERE type = 'MODERATOR'");
            if (rsMod.next()) {
                nbMod = rsMod.getInt("type");
            }
            rsMod.close(); // Fermer le ResultSet après avoir obtenu les données nécessaires

            ResultSet rsClassic = stmt.executeQuery("SELECT COUNT(*) AS type FROM user WHERE type = 'CLASSIC'");
            if (rsClassic.next()) {
                nbClassic = rsClassic.getInt("type");
            }
            rsClassic.close(); // Fermer le ResultSet après avoir obtenu les données nécessaires

            result = nbBan + " " + nbON + " " + nbOFF + " " + nbAway + " " + nbAdmin + " " + nbMod + " " + nbClassic;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;

    }

    public int nbUsers() {// nb personnes par status ADMIN, AWAY
        int nbLignes = 0;
        try {
            Statement stmt = this.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS 'username' FROM user");

            if(rs.next()) {
                nbLignes = rs.getInt("username");
            }
            rs.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return nbLignes;

    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(password.getBytes());
            byte[] byteData = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : byteData) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isCorrectUser(String name, String pwd) {
        try {
            PreparedStatement statement0 = conn.prepareStatement("SELECT username FROM user WHERE username = ?");
            PreparedStatement statement1 = conn.prepareStatement("SELECT pwd FROM user WHERE pwd = ?");

            statement0.setString(1, name);
            statement1.setString(1, hashPassword(pwd));
            ResultSet rs0 = statement0.executeQuery();
            ResultSet rs1 = statement1.executeQuery();
            if (rs0.next() || rs1.next()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUser(String username, String pwd) throws NoSuchAlgorithmException {
        //méthode pour ajouter un utilisateur dans la base de données
        try {
            Statement stmt = this.conn.createStatement();
            String query = "INSERT INTO user(username, pwd) VALUES('" + username + "', '" + hashPassword(pwd) + "')";
            stmt.executeUpdate(query);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUser(String newName, String newPwd, String name) {
        //code pour mettre à jour un utilisateur
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE user SET username = ?, pwd = ? WHERE username = ?");
            stmt.setString(1, newName);
            stmt.setString(2, hashPassword(newPwd));
            stmt.setString(3, name);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValidUser(String username, String password) throws NoSuchAlgorithmException {
        // cryptage du mot de passe écrit par l'utilisateur
        String hashedPwd = hashPassword(password);
        // recherche dans la BDD s'il existe un utilisateur avec le nom et mot de passe entrés
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT pwd FROM user WHERE username = ?");
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();

            if(rs.next()) {
                String correctHashedPwd = rs.getString("pwd");

                if(correctHashedPwd.equals(hashedPwd)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setBan(String name, boolean isBan) {
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE user SET ban = ? WHERE username = ?");
            stmt.setBoolean(1, isBan);
            stmt.setString(2, name);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getBan(String name) {
        Boolean isBan = false;

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT ban FROM user WHERE username = ?");
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                isBan = rs.getBoolean("ban");
            }
            rs.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return isBan;
    }

    public void deleteUser(String name) {
        //code pour supprimer un utilisateur
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM user WHERE username = ?");
            statement.setString(1, name);
            statement.executeUpdate();
            System.out.println("vous avez delete");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Utilisateur get(int id) {
        return null;
    }

    @Override
    public List<Utilisateur> getAll() {
        return null;
    }

    @Override
    public void save(Utilisateur utilisateur) {

    }

    @Override
    public void update(Utilisateur utilisateur) {

    }

    @Override
    public void delete(Utilisateur utilisateur) {

    }
}
