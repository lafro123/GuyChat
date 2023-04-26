package controller;

import model.Utilisateur;
import view.LoginPage;
import model.Client;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public class LoginController extends controller.BaseController implements Observer {

    // Attributs et méthodes spécifiques pour les pages d'authentification
    private LoginPage view;
    private BlockingQueue<Boolean> userValidityQueue = new LinkedBlockingQueue<>();

    public LoginController(Client model) { // constructeur d'apres la classe mere
        super(model);
        model.addObserver(this); // permet d'activer la methode update lors d'un changement
    }

    public void setView(LoginPage view) { // lie le login page au controller
        this.view = view;
    }
    public void setUsername(String username) {
        // met a jour le nom de l'utilisateur lors de la connexion a l'app
        model.getUser().setUsername(username);
    }

    public void setUserPassword(String password) {
        // met a jour le mot de passe lors de la connexion a l'app
        model.getUser().setPassword(password);
    }

    @Override
    public void update(Observable o, Object arg) {
        String message = (String) arg;

        if (message.startsWith("The user has an account")) {
            // si l'utilisateur a deja un compte alors on lui met a jour son type et son status
            Utilisateur.UserType type = Utilisateur.UserType.valueOf(message.substring(23)); // index du fin de la chaine "The user has an account"
            model.getUser().setUserType(type);
            model.getUser().setStatut(Utilisateur.Status.ONLINE);
            setUserIsValid(true);
        } else if (message.equals("The user has no account")) {
            setUserIsValid(false);
        }
    }

    public void setUserIsValid(Boolean bool) {
        userValidityQueue.offer(bool);
    }
    public Boolean getUserIsValidWithTimeout(long timeout, TimeUnit unit) {
        try {
            // utilisation d'un timer pour laisser le temps d'aller verifier dans la bdd
            return userValidityQueue.poll(timeout, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
