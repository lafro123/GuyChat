package controller;

import model.Client;
import view.ProfilWindow;
import view.SignUP;
import view.pageAcceuil;

import java.util.Observable;

public class ProfilWindowController extends BaseController {

    private ProfilWindow view;
    private pageAcceuil viewPageAcceuil;
    private int nbBan, nbAdmin, nbMod, nbClassic, nbOFF, nbON, nbAway, nbCo;

    public ProfilWindowController(Client model) {
        super(model);
    }
    public void setView(ProfilWindow view, pageAcceuil viewAcceuil) {
        this.view = view;
        this.viewPageAcceuil = viewAcceuil;
    }

    public void closeChatWindow(pageAcceuil view) {
        this.viewPageAcceuil.setVisible(false);
    }

    public void setNbBan(int nbBan) {
        this.nbBan = nbBan;
    }

    public int getNbBan() {
        return nbBan;
    }

    public void setNbAdmin(int nbAdmin) {
        this.nbAdmin = nbAdmin;
    }

    public int getNbAdmin() {
        return nbAdmin;
    }

    public void setNbMod(int nbMod) {
        this.nbMod = nbMod;
    }

    public int getNbMod() {
        return nbMod;
    }

    public void setNbClassic(int nbClassic) {
        this.nbClassic = nbClassic;
    }

    public int getNbClassic() {
        return nbClassic;
    }

    public void setNbON(int nbON) {
        this.nbON = nbON;
    }

    public int getNbON() {
        return nbON;
    }

    public void setNbOFF(int nbOFF) {
        this.nbOFF = nbOFF;
    }

    public int getNbOFF() {
        return nbOFF;
    }

    public void setNbAway(int nbAway) {
        this.nbAway = nbAway;
    }

    public int getNbAway() {
        return nbAway;
    }

    public int getNbCo() {
        return nbCo;
    }

    @Override
    public void update(Observable o, Object arg) {
        String message = (String) arg;

        if(message.startsWith("/nbDiag:")) {
            // separer la chaine de caractere en fonction des espaces
            String[] parts = message.split("\\s+");
            // ranger dans des chaines de caracteres chaque variable correspondante
            String nbBan = parts[1];
            String nbOffline = parts[2];
            String nbOnline = parts[3];
            String nbAway = parts[4];
            String nbAdmin = parts[5];
            String nbMod = parts[6];
            String nbClassic = parts[7];

            // mettre a jour les valeurs de la classe
            this.setNbBan(Integer.parseInt(nbBan));
            this.setNbON(Integer.parseInt(nbOnline));
            this.setNbOFF(Integer.parseInt(nbOffline));
            this.setNbAway(Integer.parseInt(nbAway));
            this.setNbAdmin(Integer.parseInt(nbAdmin));
            this.setNbMod(Integer.parseInt(nbMod));
            this.setNbClassic(Integer.parseInt(nbClassic));

            // appelle des fonctions de diagrammes de la page Stat avec les nouvelles valeurs de la classe
            view.chartType(this.nbBan, this.nbClassic, this.nbAdmin, this.nbMod);
            view.chartStatus(this.nbON, this.nbOFF, this.nbAway);
        } else if (message.startsWith("The user has an account")) {
            this.nbCo ++;
            System.out.println(getNbCo());
        }
    }
}
