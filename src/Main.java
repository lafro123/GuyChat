

import view.LoginPage;
import controller.LoginController;
import model.Client;

//Main.java
public class Main {
    public static void main(String[] args) {
        //initialisation de la structure MVC

        //creation du model client
        Client model = new Client();

        //initialise le controller avec le model client
        LoginController loginController = new LoginController(model);

        //creation la view
        LoginPage view = new LoginPage(loginController);
        loginController.setView(view);
    }
}