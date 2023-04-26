package MVC;

import MVC.controller.LoginController;
import MVC.model.Client;
import MVC.view.LoginPage;

//MVC.model.Main.java
public class Main {
    public static void main(String[] args) {
        //initialisation de la structure MVC

        //creation du MVC.model client
        Client model = new Client();

        //initialise le MVC.model.controller avec le MVC.model client
        LoginController loginController = new LoginController(model);

        //creation la MVC.model.view
        LoginPage view = new LoginPage(loginController);
        loginController.setView(view);
    }
}