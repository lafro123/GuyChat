package MVC.view;

import MVC.model.Utilisateur;
import MVC.controller.MessageController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class parametter extends JFrame implements ActionListener {
    JButton ban, unban,viewBannedUsers, user_type, setClassicButton, setAdminButton, setModeratorButton;
    Color bleuclair = new Color(234, 242, 248);
    Color bleufonce1 = new Color(36, 113, 163);
    Color bleufonce2 = new Color(31, 97, 141);
    Color bleufonce3 = new Color(23, 32, 42);
    JPanel userTypeButtonsPanel, cards;
    private MessageController messageController;

    private String nom;

    public parametter(String nom, MessageController messageController) {
        //Création de la fenêtre
        super("Parametter");
        this.messageController = messageController;
        this.nom = nom;
        JPanel parpan = new JPanel(new GridLayout(0, 1)); // Changez le layout en GridLayout avec un nombre variable de lignes
        parpan.setPreferredSize(new Dimension(200, 300));
        //ajout des differents boutons d'action
        ban = new JButton("Ban");
        ban.addActionListener(this);
        ban.setBackground(bleufonce1);//couleur du bouton
        ban.setForeground(bleuclair);//couleur du texte du bouton

        unban = new JButton("UnBan");
        unban.addActionListener(this);
        unban.setBackground(bleufonce1);
        unban.setForeground(bleuclair);

        viewBannedUsers = new JButton("viewBannedUsers");
        viewBannedUsers.addActionListener(this);
        viewBannedUsers.setBackground(bleufonce1);
        viewBannedUsers.setForeground(bleuclair);

        user_type = new JButton("user-type");
        user_type.addActionListener(this);
        user_type.setBackground(bleufonce1);
        user_type.setForeground(bleuclair);

        parpan.add(ban);
        parpan.add(unban);
        parpan.add(viewBannedUsers);
        parpan.add(user_type);

        // On créé un nouveau JPanel pour les boutons de type d'utilisateur
        userTypeButtonsPanel = new JPanel(new GridLayout(3, 1));

        setAdminButton = new JButton("Set Admin");
        setAdminButton.addActionListener(this);
        userTypeButtonsPanel.add(setAdminButton);
        setAdminButton.setBackground(bleufonce2);
        setAdminButton.setForeground(bleuclair);

        setModeratorButton = new JButton("Set Moderator");
        setModeratorButton.addActionListener(this);
        userTypeButtonsPanel.add(setModeratorButton);
        setModeratorButton.setBackground(bleufonce2);
        setModeratorButton.setForeground(bleuclair);

        setClassicButton = new JButton("Set Classic");
        setClassicButton.addActionListener(this);
        userTypeButtonsPanel.add(setClassicButton);
        setClassicButton.setBackground(bleufonce2);
        setClassicButton.setForeground(bleuclair);

        // Créez un JPanel avec un CardLayout pour gérer l'affichage des boutons de type d'utilisateur
        cards = new JPanel(new CardLayout());
        cards.add(new JPanel(), "empty");
        cards.add(userTypeButtonsPanel, "userTypeButtons");
       // cards.setSize(200, 100);
        parpan.add(cards); // Ajoutez le JPanel cards à parpan

        // Définir la couleur d'arrière-plan de la fenêtre et du JPanel
        parpan.setBackground(Color.white);
        getContentPane().add(parpan, BorderLayout.CENTER);
        getContentPane().setBackground(Color.blue);

        // Définir les propriétés de la fenêtre JFrame
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int userType;

        //on stock le type d" l'utilisateur dans une variable afin de verifier si il peut realiser certaines actions
       if(messageController.getModel().getUser().getUserType()== Utilisateur.UserType.ADMINISTRATOR){
           userType =1;
       }else if(messageController.getModel().getUser().getUserType()== Utilisateur.UserType.MODERATOR){
           userType=2;
       }else userType=3;

        if (e.getSource() == ban) { //possibilité de bannir que si utilisateur est admin ou moderateur

            if (userType == 3) {
                JOptionPane.showMessageDialog(this, "YOU CANNOT!");
                dispose();
            } else {
                messageController.sendMessage("/ban:" + nom);
                dispose();
            }
        }

        if (e.getSource() == unban) {//possibilité de débannir que si utilisateur est admin ou moderateur
            if (userType == 3) {
                JOptionPane.showMessageDialog(this, "YOU CANNOT!");
                dispose();
            } else {
                messageController.sendMessage("/unBan:" + nom);
                dispose();
            }
        }
        if (e.getSource() == viewBannedUsers) { //possibilité de voir la liste des bannis que si utilisateur est admin ou moderateur
            if (userType == 3) {
                JOptionPane.showMessageDialog(this, "YOU CANNOT!");
                dispose();
            } else {
                messageController.sendMessage("/bannedUsers:");
                dispose();
            }
        }

        if (e.getSource() == user_type) { // possibilité d'afficher la liste des boutons de changement de type que si utilisateur est admin ou moderateur
            if (userType == 1 ||userType == 2 ) {
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.next(cards);
            } else {
                JOptionPane.showMessageDialog(this, "YOU CANNOT!");
            }
        }

        if (e.getSource() == setClassicButton) {

            if (userType == 1 ||userType == 2 ) {
            messageController.sendMessage("/setUserType: " + nom + " classic");
            dispose();
            } else {
                JOptionPane.showMessageDialog(this, "YOU CANNOT!");
            }
        }

        if (e.getSource() == setAdminButton) {
            if (userType == 1 ) {
            messageController.sendMessage("/setUserType: " + nom + " admin");
            dispose();
            } else {
                JOptionPane.showMessageDialog(this, "YOU CANNOT!");
            }
        }

        if (e.getSource() == setModeratorButton) {
            if (userType == 1  ||userType == 2) {
            messageController.sendMessage("/setUserType: " + nom + " moderator");
            dispose();
            } else {
                JOptionPane.showMessageDialog(this, "YOU CANNOT!");
            }
        }
    }
}