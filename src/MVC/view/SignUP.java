package MVC.view;

import MVC.controller.LoginController;
import MVC.controller.SignUpController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

public class SignUP extends JFrame implements ActionListener, Observer {

    private SignUpController signUpController;
    // Variables pour les champs de texte et le bouton
    JTextField usernameField;
    JPasswordField passwordField, passwordField2;
    JButton registerButton, loginButton;

    public SignUP(SignUpController signUpController) {

        // Créer une nouvelle fenêtre JFrame avec un titre
        super("SIGN UP");
        this.signUpController = signUpController;
        // Initialiser les champs de texte et le bouton
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Sign UP");
        registerButton.setBackground(Color.black);
        loginButton.setBackground(Color.black);
        // Ajouter des écouteurs d'événements pour le bouton d'inscription
        registerButton.addActionListener(this);
        loginButton.addActionListener(this);
        // Créer un nouveau JPanel pour contenir les champs de texte et le bouton
        JPanel registerPanel = new JPanel(new GridLayout(3, 1));
        registerPanel.add(new JLabel("Choose user name :"));
        registerPanel.add(usernameField);
        registerPanel.add(new JLabel("Choose password :"));
        registerPanel.add(passwordField);
        registerPanel.add(loginButton);
        registerPanel.add(registerButton);


        // Définir la couleur d'arrière-plan de la fenêtre et du JPanel
        registerPanel.setBackground(Color.white);
        getContentPane().add(registerPanel, BorderLayout.CENTER);
        getContentPane().setBackground(Color.red);

        // Définir les propriétés de la fenêtre JFrame
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {

        // Vérifier si le bouton d'inscription a été cliqué
        if (e.getSource() == registerButton) {
            // Récupérer le nom d'utilisateur et le mot de passe entrés
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            signUpController.sendMessage("/testInscription: " + username + " " + password);

            Boolean userIsCorrect = signUpController.getUserIsCorrect(3, TimeUnit.SECONDS);

            // Vérifier si les informations d'inscription sont valides
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir un nom d'utilisateur et un mot de passe.");
            } else {
                if (userIsCorrect) {
                    JOptionPane.showMessageDialog(this, "Inscription réussie !");

                    LoginController loginController = new LoginController(signUpController.getModel());
                    LoginPage view = new LoginPage(loginController);
                    loginController.setView(view);
                }
                else {
                    JOptionPane.showMessageDialog(this, "Ce nom d'utilisateur ou ce mot de passe est déjà utilisé...");
                }
            }
        }
        if (e.getSource() == loginButton){

            LoginController loginController = new LoginController(signUpController.getModel());
            LoginPage view = new LoginPage(loginController);
            loginController.setView(view);

            view.setVisible(true);
            dispose();
        }

    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
