package MVC.view;

import MVC.controller.LoginController;
import MVC.controller.SignUpController;
import MVC.controller.MessageController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

public class LoginPage extends JFrame implements ActionListener{

    private LoginController loginController;

    Color bleuclair = new Color(234, 242, 248);
    Color bleufonce1 = new Color(36, 113, 163);
    Color bleufonce2 = new Color(31, 97, 141);
    Color bleufonce3 = new Color(23, 32, 42);
    // Variables pour les champs de texte et le bouton
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton, registerButton;

    public LoginPage(LoginController loginController) {

        // Création de la nouvelle fenêtre JFrame avec un titre: LOGIN
        super("LOGIN");
        this.loginController = loginController;

        // On initialise les champs de texte et le bouton
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        loginButton = new JButton("Login");
        loginButton.setBackground(bleufonce2);//Couleur du bouton
        loginButton.setForeground(bleuclair);//Couleur du texte du bouton

        registerButton = new JButton("SignUP");
        registerButton.setBackground(bleufonce2);
        registerButton.setForeground(bleuclair);

        // On ajoute des listener d'événements pour le bouton d'inscription et le bouton de connexion
        registerButton.addActionListener(this);
        loginButton.addActionListener(this);


        // On ajoute un nouveau JPanel pour contenir le logo et les champs de texte et le bouton
        JPanel mainPanel = new JPanel(new BorderLayout());

        // On créé un JLabel pour contenir l'image
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel logoLabel = new JLabel();
        ImageIcon icon = new ImageIcon("images/GuyChat.png");
        setIconImage(icon.getImage());
        Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(img));
        logoPanel.add(logoLabel);

        // On ajoute le JLabel à la partie supérieure du main panel
        mainPanel.add(logoPanel, BorderLayout.NORTH);

        // On ajoute un nouveau JPanel pour contenir les champs de texte et le bouton
        JPanel registerPanel = new JPanel(new GridLayout(3, 1));

        // On ajoute les composants
        // Création des zones de textes username et password
        JLabel cusername = new JLabel("Username :");
        cusername.setForeground(bleufonce3);
        registerPanel.add(cusername);
        registerPanel.add(usernameField);

        JLabel cpsw = new JLabel("Password :");
        cpsw.setForeground(bleufonce3);
        registerPanel.add(cpsw);
        registerPanel.add(passwordField);

        registerPanel.add(registerButton);
        registerPanel.add(loginButton);

        // On ajoute le JPanel des champs de texte et le bouton à la partie centrale du JPanel principal
        mainPanel.add(registerPanel, BorderLayout.CENTER);

        // On définit la couleur d'arrière-plan de la fenêtre et du JPanel principal
        mainPanel.setBackground(bleuclair);
        getContentPane().add(mainPanel);

        pack();
        setVisible(true);//On rend la fenêtre visible
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Permet de fermer le programme quand on ferme la fenêtre
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == loginButton) { // si on clique sur le bouton Login
            // on récupère les informations écrites par l'utilisateur
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            loginController.sendMessage("/testConnexion: " + username + " " + password);

            Boolean userIsValid = loginController.getUserIsValidWithTimeout(2, TimeUnit.SECONDS);
            if (userIsValid == null) {
                //si userIsValid est null = le code na pas pu recevoir les information de la base de données, affichage d'un message d'erreur
                System.out.println("marche pas");

            }
            else {
                if (username.isEmpty() || password.isEmpty()) { // si les champs de text sont vides
                    JOptionPane.showMessageDialog(this, "Veuillez saisir un nom d'utilisateur et un mot de passe."); // popup qui le signale
                } else if (userIsValid) { // si la variable = true (c'est a dire que le mdp et nom n'existe pas encore dans la bdd)
                    loginController.setUsername(username); // envoyer au MVC.model.controller le nouveau nom d'utilisateur
                    loginController.setUserPassword(password); // envoyer au MVC.model.controller le nouveau mot de passe
                    JOptionPane.showMessageDialog(this, "Connexion sucess !"); // popup "connection réussie"
                    MessageController messageController = new MessageController(loginController.getModel());
                    pageAcceuil view2 = new pageAcceuil(messageController);
                    messageController.setView(view2);
                    dispose(); // mettre en place les elements afin qu'il soit tous dans la page
                    view2.setVisible(true); // rendre visible la fenetre de connection
                    setVisible(false); // cacher la fenetre d'inscription
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect user or password.");
                }
            }
        }
        MessageController signup;
        if (e.getSource() == registerButton) { //Si on clique sur le bouton register

            SignUpController SignUpController = new SignUpController(loginController.getModel());
            SignUP view3 = new SignUP(SignUpController);
            SignUpController.setView(view3);
            dispose();
            view3.setVisible(true);
        }

    }

}