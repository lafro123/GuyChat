package controller;

import view.SignUP;
import model.Client;

import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SignUpController extends BaseController {
    // Attributs et méthodes spécifiques pour les pages d'authentification
    private SignUP view;
    private BlockingQueue<Boolean> userCorrectQueue = new LinkedBlockingQueue<>();
    public SignUpController(Client model) {
        super(model);
    }

    public void setView(SignUP view) {
        this.view = view;
    }

    @Override
    public void update(Observable o, Object arg) {
        String message = (String) arg;

        if (message.equals("The user already exist")) {
            setUserIsCorrect(false);
        } else if (message.equals("The user doesn't exist")) {
            setUserIsCorrect(true);
        }
    }

    public void setUserIsCorrect(Boolean bool) {
        userCorrectQueue.offer(bool);
    }
    public Boolean getUserIsCorrect(long timeout, TimeUnit unit) {
        try {
            return userCorrectQueue.poll(timeout, unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
