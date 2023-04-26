package MVC.controller;

import MVC.model.Client;
import MVC.model.Utilisateur;
import MVC.view.pageAcceuil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MessageController extends MVC.controller.BaseController {

    private pageAcceuil view;
    private Set<String> allNames = new HashSet<>(Arrays.asList(model.getUser().getUsername()));
    private Set<String> coNamesSet = new HashSet<>();
    private Set<String> decoNamesSet = new HashSet<>();
    private Set<String> awayNamesSet= new HashSet<>();

    // getters et setters pour recuperer et mettre a jours les Set des noms en fonction de leur status
    public pageAcceuil getView() {
        return view;
    }

    public Set<String> getCoNamesSet() {
        return coNamesSet;
    }

    public Set<String> getDecoNamesSet() {
        return decoNamesSet;
    }

    public Set<String> getAwayNamesSet() {
        return awayNamesSet;
    }

    public void setCoNamesSet(Set<String> coNamesSet) {
        this.coNamesSet = coNamesSet;
    }

    public void setDecoNamesSet(Set<String> decoNamesSet) {
        this.decoNamesSet = decoNamesSet;
    }

    public void setAwayNamesSet(Set<String> awayNamesSet) {
        this.awayNamesSet = awayNamesSet;
    }

    public void setAllNames(Set<String> allNames) {
        this.allNames = allNames;
    }
    public MessageController(Client model) { // contructeur basé sur la classe mere
        super(model);
        model.addObserver(this);
    }
    public void setView(pageAcceuil view) {
        // association de la PageAcceuil au messageController
        this.view = view;
    }
    public Set<String> getAllNames() {
        // recupere tous les noms des utilisateurs de la bdd
        return allNames;
    }


    @Override
    public void update(Observable o, Object arg) { // declencher a chaque message du serveur
        String message = (String) arg;

        if (message.startsWith("/co:")) {
            System.out.println(message);
            // Extraire les noms après chaque préfixe
            Pattern coPattern = Pattern.compile("/co: (.*?) usernameDeco:");
            Matcher coMatcher = coPattern.matcher(message);
            String coNames = coMatcher.find() ? coMatcher.group(1) : "";

            Pattern decoPattern = Pattern.compile("usernameDeco: (.*?) usernameAway:");
            Matcher decoMatcher = decoPattern.matcher(message);
            String decoNames = decoMatcher.find() ? decoMatcher.group(1) : "";

            Pattern awayPattern = Pattern.compile("usernameAway: (.*)");
            Matcher awayMatcher = awayPattern.matcher(message);
            String awayNames = awayMatcher.find() ? awayMatcher.group(1) : "";

            // Créer la chaîne "AllUsers"
            String allUsers = coNames + " " + decoNames + " " + awayNames;

            System.out.println("AllUsers: " + allUsers.trim());
            System.out.println("/co: " + coNames);
            System.out.println("usernameDeco: " + decoNames);
            System.out.println("usernameAway: " + awayNames);

            Set<String> allUsersS = new HashSet<>(Arrays.asList(allUsers.trim().split("\\s+")));
            Set<String> coNamesS = new HashSet<>(Arrays.asList(coNames.trim().split("\\s+")));
            Set<String> decoNamesS = new HashSet<>(Arrays.asList(decoNames.trim().split("\\s+")));
            Set<String> awayNamesS = new HashSet<>(Arrays.asList(awayNames.trim().split("\\s+")));

            setAllNames(allUsersS);
            setCoNamesSet(coNamesS);
            setDecoNamesSet(decoNamesS);
            setAwayNamesSet(awayNamesS);

            view.setAllNames(allNames);
            view.updateUserButtons(view.getUserButtonsPanel(),allNames,coNamesSet,decoNamesSet,awayNamesSet ); // Appelez la méthode updateUserButtons ici

        } else if (message.equals("The user has an account")) { // ne rien faire
        } else if (message.equals("The user has no account")) { // ne rien faire
        } else if (message.startsWith("/GIF:")) {
            String[] parts = message.split("\\s+"); // Divise la chaîne en fonction des espaces
            String url = parts[1];
            String userSender = parts[2];
            //System.out.println("Test if arrivé gif Client/MessageControler " + url + " " + userSender);
            if (userSender.equals(getModel().getUser().getUsername())){ // affichage des GIF a gauche sur PageAcceuil
                view.addGif(url, false);
                System.out.println("test addGifLeft()");
            }else  { // affichage des GIF a droite sur PageAcceuil
                view.sendMessageLeft(userSender);
                view.addGif(url, true);
                System.out.println("test addGifRight()");
            }
        } else if (message.startsWith("/changeType:")) { // mettre a jour le type de l'utilisateur car le type a ete changer manuellement sur la page
            String[] parts = message.split(" ", 2);
            String type = parts[0].substring("/changeType:".length());
            getModel().getUser().setUserType(Enum.valueOf(Utilisateur.UserType.class, type));

            view.repaint(); // repeindre la page
            view.updateUserTypeLabel(); // Ajoutez cette ligne pour mettre à jour le label
        } else if (message.startsWith("/nbDiag:")) { }// ne rien faire
        else {
            if (message.startsWith(model.getUser().getUsername())) { // affichage des messages a gauche sur PageAcceuil
                view.sendMessageRight(message);
            } else if (message.startsWith("*")) { // ecrire le message au centre en italique car previend d'une action dans le serveur (left the chat, banned, etc.)
                view.sendMessageCenter(message);
            } else { // affichage des messages a gauche sur PageAcceuil
                view.sendMessageLeft(message);
            }
        }
    }
}
