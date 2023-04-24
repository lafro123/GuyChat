package controller;

import model.Client;
import view.ProfilWindow;
import view.SignUP;

import java.util.Observable;

public class ProfilWindowController extends BaseController {

    private ProfilWindow view;

    public ProfilWindowController(Client model) {
        super(model);
    }
    public void setView(ProfilWindow view) {
        this.view = view;
    }

    @Override
    public void update(Observable o, Object arg) {

    }


}
