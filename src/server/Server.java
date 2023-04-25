package server;

import DAO.ConnectionDB;
import DAO.MessageDAO;
import DAO.UserDAO;
import model.IPAddress;
import model.Utilisateur;
import view.pageAcceuil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static model.Utilisateur.Status.OFFLINE;
import static model.Utilisateur.UserType.*;

public class Server {

    private static final int SERVER_PORT = 9999;
    //private static final String SERVER_IP ="172.20.10.3";
    private static final String SERVER_IP = IPAddress.getIpAddress().getHostAddress();//"192.168.1.37";
    // retourne l'adress ip de ton ordi

    static Set<String> bannedUser = new HashSet<>();

    static Set<ClientHandler> clientHandlers = new HashSet<>();
    //static DriverManager DriverManager;

    public static void main(String[] args) {
        System.out.println("Démarrage du serveur...");
        System.out.println("( L'IP du server est: " + SERVER_IP + " )");

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT, 50, InetAddress.getByName(SERVER_IP))) {
            while (true) {

                Socket clientSocket = serverSocket.accept();
                System.out.println("model.Client connecté : " + clientSocket.getInetAddress());

                PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String username = input.readLine();
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientWriter, username);
                //broadcastMessage("* " + username + " has entered the chat *");
                //Server.afficherQuiEstCo();

                clientHandlers.add(clientHandler);

                Thread clientThread = new Thread(clientHandler);
                clientThread.start();

            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'ouverture du socket serveur : " + e.getMessage());
        }
    }

    public static void sendMessageTo(String username, String message, String userSender) {

        for (ClientHandler handler : clientHandlers) {
            if (handler.getUsername().equalsIgnoreCase(username)) {
                handler.getWriter().println("(private message from " + userSender + ") " + message);

                break;
            }
        }
        for (ClientHandler handler : clientHandlers) {
            if (handler.getUsername().equalsIgnoreCase(userSender)) {
                handler.getWriter().println("(private message to " + username + ") " + message);
                break;
            }
        }
    }

    public static void broadcastMessage(String message) {
        try {
            Connection conn = ConnectionDB.getConnection();
            MessageDAO msgDAO = new MessageDAO(conn);
            msgDAO.addMsg(message);

            for (ClientHandler handler : clientHandlers) {
                if (bannedUser.contains(handler.getUsername())) {
                    handler.getWriter().println("pas de message pour toi, t'es banni!!!!!");
                } else {
                    handler.getWriter().println(message);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addBanned(String username) {
        try {
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDAO = new UserDAO(conn);

            userDAO.setBan(username, true);

            bannedUser.add(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void unBan(String username) {
        try {
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDAO = new UserDAO(conn);

            userDAO.setBan(username, false);

            bannedUser.remove(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean IsBanned(String username) {
        try {
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDAO = new UserDAO(conn);

            return userDAO.getBan(username);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendMessageTBanned(String username) {
        for (ClientHandler handler : clientHandlers) {
            if (handler.getUsername().equalsIgnoreCase(username)) {
                handler.getWriter().println("pas de message pour toi, t'es banni");
            }
        }
    }

    public static void afficherQuiEstCo() { //sans affichage , que pour les boutons
        String userStatus;
        try {
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDao = new UserDAO(conn);

            userStatus = userDao.getListUserCo();

            for (ClientHandler handler : clientHandlers) {
                handler.getWriter().println(userStatus);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void afficherQuiEstCo2(String userSender) { //qui affiche dans la ocnv qui est co

        String noms = "";
        int i = 0;
        for (ClientHandler handler : clientHandlers) { //pour eviter l espace au debut
            if (i == 0) {
                noms = noms + handler.getUsername();
            } else noms = noms + " " + handler.getUsername();
            i++;
        }
        System.out.println(noms);
        System.out.println(userSender);
        for (ClientHandler handler : clientHandlers) {
            if (handler.getUsername().equalsIgnoreCase(userSender)) {
                handler.getWriter().println("les utilisateurs co sont :" + noms);
            }
        }
    }

    public static void isValidUser(String username, String password) {
        boolean isValid = false;
        Utilisateur.UserType type = null;
        String msg = null;

        try {
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDao = new UserDAO(conn);
            //MessageDAO msgDAO = new MessageDAO(conn);
            isValid = userDao.isValidUser(username, password);
            type = userDao.getUserTypeByUsername(username);
            //msg = msgDAO.getAllMessages();

            if (isValid) {
                for (ClientHandler handler : clientHandlers) {
                    if (handler.getUsername().equalsIgnoreCase("unknown")) {
                        handler.getWriter().println("The user has an account" + type);
                        handler.setUsername(username);
                        broadcastMessage("* " + username + " has entered the chat *");
                        userDao.setStatus(username, String.valueOf(Utilisateur.Status.ONLINE));
                        break;
                    }
                }

            } else {
                for (ClientHandler handler : clientHandlers) {
                    if (handler.getUsername().equalsIgnoreCase("unknown")) {
                        handler.getWriter().println("The user has no account");
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void inscription(String username, String password) {
        boolean isCorrect = false;
        // utilisation du UserDAO
        try {
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDao = new UserDAO(conn);
            isCorrect = userDao.isCorrectUser(username, password);

            if (isCorrect) {
                for (ClientHandler handler : clientHandlers) {
                    handler.getWriter().println("The user doesn't exist");
                    userDao.addUser(username, password);
                    break;
                }

            } else {
                for (ClientHandler handler : clientHandlers) {
                    handler.getWriter().println("The user already exist");
                    break;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeType(String targetUsername, String type, String userSender) {
        if(Objects.equals(type, "classic")){
            type = String.valueOf(CLASSIC);
        }
        if(Objects.equals(type, "admin")){
            type = String.valueOf(ADMINISTRATOR);
        }
        if(Objects.equals(type, "moderator")){
            type = String.valueOf(MODERATOR);
        }
        try {
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDao = new UserDAO(conn);
            userDao.updateUserTypeByUsername(targetUsername, type);

            for (ClientHandler handler : clientHandlers) {
                if (handler.getUsername().equalsIgnoreCase(targetUsername)) {
                    broadcastMessage("* " + userSender + " set " + targetUsername + " to " + type + " *");
                    handler.getWriter().println("/changeType:" + type);
                    break;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void displayGif(String gif, String userSender) {
        broadcastMessage("/GIF: " + gif + " " + userSender);
        //System.out.println("test broadvast gif");
    }

    public static void returnBannedUsers(String userSender) {
        StringBuilder bannedUserNames = new StringBuilder();

        for (String user : bannedUser) {
            bannedUserNames.append(user);
        }
        for (ClientHandler handler : clientHandlers) {
            if (handler.getUsername().equalsIgnoreCase(userSender)) {
                if (bannedUserNames.isEmpty()) {
                    handler.getWriter().println("* No banned Users yet *");
                } else handler.getWriter().println("* Banned users are : " + bannedUserNames + " *");
                break;
            }
        }
    }

    public static void changeProfil(String newUsername, String newPassword, String userSender) {
        try {
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDao = new UserDAO(conn);
            userDao.updateUser(newUsername, newPassword, userSender);

            broadcastMessage("* " + userSender + " has changed his/her name to " + newUsername+" *");

            for (ClientHandler handler : clientHandlers) {
                if (handler.getUsername().equalsIgnoreCase(userSender)) {
                    handler.setUsername(newUsername);
                    break;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void setStatus(String name,String Status) {
        try{
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDao = new UserDAO(conn);
            userDao.setStatus(name, Status);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void majStatProfil() {
        String stats;
        try{
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDao = new UserDAO(conn);

            stats = userDao.nbStats();

            for (ClientHandler handler : clientHandlers) {
                handler.getWriter().println("/nbDiag: " + stats);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteUser(String name) {
        try{
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDao = new UserDAO(conn);
            userDao.deleteUser(name);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}