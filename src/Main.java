

import view.LoginPage;
import controller.LoginController;
import model.Client;

//Main.java
public class Main {
    public static void main(String[] args) {
        Client model = new Client(); //creer le client apres la page login sinon entered trop tot

        LoginController loginController = new LoginController(model);
        LoginPage view = new LoginPage(loginController);
        loginController.setView(view);
    }
}