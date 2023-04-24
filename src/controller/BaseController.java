package controller;

import model.Client;

import java.util.Observable;
import java.util.Observer;

public abstract class BaseController implements Observer {
    protected Client model;
    public void sendMessage(String message) {
        model.sendMessage(message);
    }

    public BaseController(Client model) {
        this.model = model;
        model.addObserver(this);
    }
    public  Client getModel() {
        return model;
    }

    public void update(Observable o, Object arg) {}


}