package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private PrintWriter writer;
    private String username;


    public ClientHandler(Socket clientSocket, PrintWriter writer, String username) {
        this.clientSocket = clientSocket;
        this.writer = writer;
        this.username = username;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message;

            clientSocket.getInetAddress();
            Server.afficherQuiEstCo();

            while ((message = input.readLine()) != null) {
                //System.out.println("Message reçu : " + message);

                //déchiffre l'envoyeur et le message
                String[] parts1 = message.split("¤");
                String userSender = parts1[0];
                String onlyTheMessage = parts1[1];

                //si l'utilisateur est ban, ne peut rien faire
                if (Server.IsBanned(userSender)) {
                    Server.sendMessageTBanned(userSender);
                } else {

                    //si message commence par @ --> message privé
                    if (onlyTheMessage.startsWith("@")) {
                        String[] parts = onlyTheMessage.split(" ", 2);
                        String targetUsername = parts[0].substring("@".length());
                        String messageToSend = parts[1];
                        Server.sendMessageTo(targetUsername, messageToSend, userSender);
                    }
                    //si message commence par /ban: --> bannissement d'un utilisateur
                    else if (onlyTheMessage.startsWith("/ban:")) {
                        String[] parts = onlyTheMessage.split(" ", 2);
                        String targetUsername = parts[0].substring("/ban:".length());
                        Server.broadcastMessage("* " + userSender + " has banned " + targetUsername + " *");
                        Server.addBanned(targetUsername);
                    }
                    //si message commence par /unBan: --> débannissement d'un utilisateur
                    else if (onlyTheMessage.startsWith("/unBan:")) {
                        String[] parts = onlyTheMessage.split(" ", 2);
                        String targetUsername = parts[0].substring("/unBan:".length());
                        Server.unBan(targetUsername);
                        Server.broadcastMessage("* " + userSender + " has unbanned " + targetUsername + " *");
                    }
                    //si message commence par /quiCo : --> envoie la liste des client de la base ainsi que leur status (MAJ des boutons)
                    else if (onlyTheMessage.startsWith("/quiCo")) {
                        Server.afficherQuiEstCo();
                    }
                    //si message commence par /co : --> envoie la liste des client dans la conversation
                    else if (onlyTheMessage.startsWith("/co")) {
                        Server.afficherQuiEstCo2(userSender);
                    }
                    //si message commence par /testConnexion: : --> un client essaye de se connecter
                    else if (onlyTheMessage.startsWith("/testConnexion:")) {
                        System.out.println("Test connexion détecté"); // Ajoutez cette ligne
                        String[] parts = onlyTheMessage.split("\\s+"); // Divise la chaîne en fonction des espaces
                        String nom = parts[1];
                        String mdp = parts[2];
                        Server.isValidUser(nom, mdp);
                    }
                    //si message commence par/GIF: : --> envoi d'un GIF
                    else if (onlyTheMessage.startsWith("/GIF:")) {
                        String[] parts = onlyTheMessage.split(" ", 2);
                        String url = parts[0].substring("/GIF:".length());
                        Server.displayGif(url, userSender);
                    }
                    //si message commence par /testInscription:: --> inscription d'un nouveau user
                    else if (onlyTheMessage.startsWith("/testInscription:")) {
                        String[] parts = onlyTheMessage.split("\\s+"); // Divise la chaîne en fonction des espaces
                        String nom = parts[1];
                        String mdp = parts[2];
                        Server.inscription(nom, mdp);
                    }
                    //si message commence par /setUserType:: -->changement de type d'un user
                    else if (onlyTheMessage.startsWith("/setUserType:")) {
                        String[] parts = onlyTheMessage.split("\\s+"); // Divise la chaîne en fonction des espaces
                        String targetUsername = parts[1];
                        String type = parts[2];
                        Server.changeType(targetUsername, type, userSender);
                    }
                    //si message commence par /bannedUsers:-->envoi la liste des users bannis aux utilisateurs
                    else if (onlyTheMessage.startsWith("/bannedUsers:")) {
                        Server.returnBannedUsers(userSender);
                    }
                    //si message commence par /MAJProfil -->enregistrement d'un changement de noms ou de mdp dans la base
                    else if (onlyTheMessage.startsWith("/MAJProfil")) {
                        String[] parts = onlyTheMessage.split("\\s+"); // Divise la chaîne en fonction des espaces
                        String newName = parts[1];
                        String newMdp = parts[2];
                        Server.changeProfil(newName, newMdp, userSender);
                    }
                    //si message commence par /MAJStats -->MAJ des stats
                    else if (onlyTheMessage.startsWith("/MAJStats")) {
                        Server.majStatProfil();
                    }
                    //si message commence par /ChangeStatus:online -->change le status de l'utilisateur dans la base
                    else if (onlyTheMessage.startsWith("/ChangeStatus:online")) {
                        Server.setStatus(username, "ONLINE");
                    }
                    //si message commence par /ChangeStatus:away -->change le status de l'utilisateur dans la base
                    else if (onlyTheMessage.startsWith("/ChangeStatus:away")) {
                        Server.setStatus(username, "AWAY");
                    }
                    //si message commence par /DeleteAccount -->csupprime le compte de l'utilisateur dans la base
                    else if (onlyTheMessage.startsWith("/DeleteAccount")) {
                        Server.deleteUser(username);
                    }
                    //si message commence par /TestDeconnexion -->MAJ status user dans base, avertis les autres user
                    else if (onlyTheMessage.startsWith("/TestDeconnexion")) {
                        Server.broadcastMessage("* " + username + " has left the chat *");
                        Server.setStatus(username, "OFFLINE");
                        Server.clientHandlers.remove(this);
                        Server.afficherQuiEstCo(); //mettre a jour les boutons des autres
                    }
                    //sinon : message normal, tous les users recoivent le message
                    else {
                        Server.broadcastMessage(userSender + ": " + onlyTheMessage);
                    }
                    //dans tous les cas on envoit aussi la liste des users de la base ainsi que leur status pour mettre a jour les boutons de tout le monde
                    Server.afficherQuiEstCo();
                }

            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la réception du message : " + e.getMessage());
        } finally {
            Server.broadcastMessage("* " + username + " has left the chat *");
            Server.setStatus(username, "OFFLINE");
            Server.clientHandlers.remove(this);
            Server.afficherQuiEstCo(); //mettre a jour les boutons des autres

            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Erreur lors de la fermeture du socket client : " + e.getMessage());
            }
        }
    }

}