package MVC.view;

import MVC.controller.MessageController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GifButtons extends JDialog {
    Color bleuclair = new Color(234, 242, 248);
    Color bleufonce2 = new Color(31, 97, 141);


    public GifButtons(MessageController messageController) {

        //titre de la page
        setTitle("Choose GIF");
        setSize(250, 250);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel gifPannel = new JPanel(new GridLayout(0, 1));
        //initilaise tous les boutons de GIF et les affiche
        JButton happyButton = new JButton("Happy");
        happyButton.setBackground(bleufonce2);
        happyButton.setForeground(bleuclair);
        JButton surprisedButton = new JButton("Surprised");
        surprisedButton.setBackground(bleufonce2);
        surprisedButton.setForeground(bleuclair);
        JButton angryButton = new JButton("Angry");
        angryButton.setBackground(bleufonce2);
        angryButton.setForeground(bleuclair);
        JButton sadButton = new JButton("Sad");
        sadButton.setBackground(bleufonce2);
        sadButton.setForeground(bleuclair);
        JButton sexyButton = new JButton("Sexy");
        sexyButton.setBackground(bleufonce2);
        sexyButton.setForeground(bleuclair);
        JButton disgustedButton = new JButton("Disgusted");
        disgustedButton.setBackground(bleufonce2);
        disgustedButton.setForeground(bleuclair);
        JButton boredButton = new JButton("Bored");
        boredButton.setBackground(bleufonce2);
        boredButton.setForeground(bleuclair);
        JButton funnyButton = new JButton("Funny");
        funnyButton.setBackground(bleufonce2);
        funnyButton.setForeground(bleuclair);
        JButton hypedButton = new JButton("Hyped");
        hypedButton.setBackground(bleufonce2);
        hypedButton.setForeground(bleuclair);
        JButton saussageButton = new JButton("Saussage");
        saussageButton.setBackground(bleufonce2);
        saussageButton.setForeground(bleuclair);

        //on les ajoute au pannel de boutons
        gifPannel.add(happyButton);
        gifPannel.add(surprisedButton);
        gifPannel.add(angryButton);
        gifPannel.add(sadButton);
        gifPannel.add(sexyButton);
        gifPannel.add(disgustedButton);
        gifPannel.add(boredButton);
        gifPannel.add(funnyButton);
        gifPannel.add(hypedButton);
        gifPannel.add(saussageButton);

        getContentPane().add(gifPannel, BorderLayout.CENTER);


//on focntion du boutons cliqué, on envoit un message different au serveur afin qu'il envoit le bon GIF aux autres clients
        happyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageController.sendMessage("/GIF:GIFhappy");
                dispose();
            }
        });

        surprisedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageController.sendMessage("/GIF:GIFsurprised");
                dispose();
            }
        });

        angryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageController.sendMessage("/GIF:GIFangry");
                dispose();
            }
        });

        sadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageController.sendMessage("/GIF:GIFsad");
                dispose();
            }
        });

        sexyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageController.sendMessage("/GIF:GIFsexy");
                dispose();
            }
        });

        disgustedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageController.sendMessage("/GIF:GIFdisgusted");
                dispose();
            }
        });

        boredButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageController.sendMessage("/GIF:GIFbored");
                dispose();
            }
        });

        funnyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageController.sendMessage("/GIF:GIFfunny");
                dispose();
            }
        });

        hypedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageController.sendMessage("/GIF:GIFhyped");
                dispose();
            }
        });

        saussageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageController.sendMessage("/GIF:GIFsaussage");
                dispose();
            }
        });


        setLocationRelativeTo(null);
        setModal(true);
    }
}
