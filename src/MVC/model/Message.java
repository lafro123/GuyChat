package MVC.model;

import java.sql.Timestamp;

// classe pour le DAO mais pas utilisé car pas eu le temps de relier l'historique des messages au graphique
public class Message {
    private Utilisateur userSender;
    private String text;
    private Timestamp time;
}
