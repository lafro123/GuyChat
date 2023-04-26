package ServerDAO.server;

import ServerDAO.server.ClientHandler;
import ServerDAO.DAO.MessageDAO;
import ServerDAO.DAO.UserDAO;
import ServerDAO.DAO.ConnectionDB;
import MVC.model.Utilisateur;

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

import static MVC.model.Utilisateur.UserType.*;

public class Server {

    private static final int SERVER_PORT = 9999;
    // retourne l'adress ip de ton ordi
        private static final String SERVER_IP ="172.20.10.3"; //IPAddress.getIpAddress().getHostAddress();


    static Set<String> bannedUser = new HashSet<>();

    //set de client connectés
    static Set<ClientHandler> clientHandlers = new HashSet<>();

    public static void main(String[] args) {
        // Affiche le démarrage du serveur
        System.out.println("Démarrage du serveur...");
        // Affiche l'IP du serveur
        System.out.println("( L'IP du ServerDAO.server est: " + SERVER_IP + " )");

        // Tente de créer un ServerSocket avec l'IP et le port spécifiés
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT, 50, InetAddress.getByName(SERVER_IP))) {
            // Boucle infinie pour accepter les connexions des clients
            while (true) {
                // Attend qu'un client se connecte et accepte la connexion
                Socket clientSocket = serverSocket.accept();
                System.out.println("MVC.model.Client connecté : " + clientSocket.getInetAddress());

                // Crée un PrintWriter pour envoyer des messages au client
                PrintWriter clientWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                // Crée un BufferedReader pour lire les messages du client
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // Lit la première ligne envoyée par le client (username)
                String username = input.readLine();
                // Crée un objet ClientHandler pour gérer la communication avec le client
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientWriter, username);

                // Ajoute le clientHandler à la liste des clients connectés
                clientHandlers.add(clientHandler);

                // Crée un nouveau thread pour gérer ce client et le démarre
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (IOException e) {
            // Affiche un message d'erreur si la création du ServerSocket échoue
            System.err.println("Erreur lors de l'ouverture du socket serveur : " + e.getMessage());
        }
    }


        public static void sendMessageTo(String username, String message, String userSender) { //envoyer un message privé


        //addapte le message en fonction de l'envoyeur et du receveur
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

    public static void broadcastMessage(String message) { //envoyer message a tout le monde
        try {
            Connection conn = ConnectionDB.getConnection();
            MessageDAO msgDAO = new MessageDAO(conn);
            msgDAO.addMsg(message);

            for (ClientHandler handler : clientHandlers) { //parcours tous les client connecté
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

    public static void addBanned(String username) { //ajouter un bannis
        try {
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDAO = new UserDAO(conn);
            //pour ajouter le banissement dans la base en passant par le DOA
            userDAO.setBan(username, true);
            bannedUser.add(username);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void unBan(String username) { //debanir des client
        try {
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDAO = new UserDAO(conn);
            //pour ajouter le banissement dans la base en passant par le DOA
            userDAO.setBan(username, false);
            bannedUser.remove(username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Boolean IsBanned(String username) { //pour savoir si un client est bannis
        try {
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDAO = new UserDAO(conn);
            return userDAO.getBan(username);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendMessageTBanned(String username) { //si un utilisateur bannis essaye de parler
        for (ClientHandler handler : clientHandlers) {
            if (handler.getUsername().equalsIgnoreCase(username)) {
                handler.getWriter().println("pas de message pour toi, t'es banni");
            }
        }
    }

    public static void afficherQuiEstCo() { //permet de mettre a jour les boutons sur le coté du chat
        String userStatus;
        try {
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDao = new UserDAO(conn);
            //retourne tous les utilisateurs avec leur status, pour adapter la couleur des boutons
            userStatus = userDao.getListUserCo();


            for (ClientHandler handler : clientHandlers) {
                handler.getWriter().println(userStatus);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void afficherQuiEstCo2(String userSender) { //Permet d'afficher la liste des gens connecté

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

    public static void isValidUser(String username, String password) { //verification du nom et du mdp lors de la connection
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

    public static void inscription(String username, String password) { //inscription d'un nouvel utilisateur
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

    public static void changeType(String targetUsername, String type, String userSender) { //changement de type d'un utilisateur par un admin
        if (Objects.equals(type, "classic")) {
            type = String.valueOf(CLASSIC);
        }
        if (Objects.equals(type, "admin")) {
            type = String.valueOf(ADMINISTRATOR);
        }
        if (Objects.equals(type, "moderator")) {
            type = String.valueOf(MODERATOR);
        }
        try {
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDao = new UserDAO(conn);
            //applique le changement dans la base de données
            userDao.updateUserTypeByUsername(targetUsername, type);
            //previens tous les clients du changement dans le chat
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

    public static void displayGif(String gif, String userSender) { //envoi du gif aux client
        broadcastMessage("/GIF: " + gif + " " + userSender);

    }

    public static void returnBannedUsers(String userSender) { //retourne la liste des gens bannis
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

    public static void changeProfil(String newUsername, String newPassword, String userSender) { //changement de nom ou de mdp
        try {
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDao = new UserDAO(conn);
            //applique le changement dans la base de données
            userDao.updateUser(newUsername, newPassword, userSender);

            broadcastMessage("* " + userSender + " has changed his/her name to " + newUsername + " *");

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

    public static void setStatus(String name, String Status) { //changement du status du user
        try {
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDao = new UserDAO(conn);
            //applique le changement dans la base
            userDao.setStatus(name, Status);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void majStatProfil() {
        String stats;
        try {
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

    public static void deleteUser(String name) { //suppression d'un user dans la base
        try {
            Connection conn = ConnectionDB.getConnection();
            UserDAO userDao = new UserDAO(conn);
            //applique le changement dans la base
            userDao.deleteUser(name);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}