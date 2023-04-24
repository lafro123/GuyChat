
package model;
import controller.LoginController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class Client extends Observable {
    private Utilisateur user = new Utilisateur("unknown", "unknown");
//    private static final String SERVER_IP = "192.168.1.37";
    private static final String SERVER_IP =IPAddress.getIpAddress().getHostAddress(); // retourne l'adress ip de ton ordi
    private static final int SERVER_PORT = 9999;

    private PrintWriter out;

    public Client() {
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connexion établie avec le serveur...");

            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(user.getUsername());

            Thread readMessage = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String receivedMessage;

                        while ((receivedMessage = input.readLine()) != null) {
                            receiveMessage(receivedMessage);
                        }
                    } catch (IOException e) {
                        System.err.println("Erreur lors de la réception du message: " + e.getMessage());
                    }
                }
            });

            readMessage.start();
        } catch (UnknownHostException e) {
            System.err.println("Hôte inconnu : " + e.getMessage());
        } catch (IOException e) {
             System.err.println("Erreur d'entrée/sortie : " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        out.println(user.getUsername() + "¤" + message);
    }



    private void receiveMessage(String message) {
        setChanged();
        notifyObservers(message);
    }

    public Utilisateur getUser() {
        return this.user;
    }
    public void addObserver(Observer observer) {
        super.addObserver(observer);
    }
}