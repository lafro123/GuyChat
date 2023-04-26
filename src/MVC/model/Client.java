
package MVC.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class Client extends Observable {

    //initialise le client a unknown en attendant les infos de la base de données
    private Utilisateur user = new Utilisateur("unknown", "unknown");
    // retourne l'adress ip de ton ordi
    private static final String SERVER_IP ="172.20.10.3";// IPAddress.getIpAddress().getHostAddress();
    private static final int SERVER_PORT = 9999;

    private PrintWriter out;

    public Client() {
        try {
            //initialisation du Socket vers le Serveur
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connexion établie avec le serveur...");

            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(user.getUsername());

            //creation du thread qui attend de recevoir un message, previent les observeur en cas de reception

            Thread readMessage = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //reception du message depuis le socket dans un BufferedReader
                        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String receivedMessage;

                        while ((receivedMessage = input.readLine()) != null) {
                            //reception d'un message, previent les observeur
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
        //previent les observeur d'un nouveau message
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