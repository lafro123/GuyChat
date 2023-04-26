package controller;

import model.Client;

import java.util.Observable;
import java.util.Observer;

public abstract class BaseController implements Observer { // class abstraite, tous les controlleurs heritent de cette classe
    protected Client model;
    public void sendMessage(String message) { // fait le llien entre les views et le server
        model.sendMessage(message);
    }

    public BaseController(Client model) {
        this.model = model;
        model.addObserver(this);
    }
    public  Client getModel() {
        return model;
    } // recupere le client actuel

    public void update(Observable o, Object arg) {}


}