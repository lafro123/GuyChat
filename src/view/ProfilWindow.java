package view;


import controller.LoginController;
import controller.MessageController;
import controller.ProfilWindowController;
import model.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import java.awt.Color;

import javax.swing.JFrame;

import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;
import java.util.Date;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import java.text.SimpleDateFormat;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;


import java.awt.event.*;
import java.util.concurrent.TimeUnit;


public class ProfilWindow extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton logOutButton;
    private JButton returnButton;

    Color bleufonce2 = new Color(31, 97, 141);




    public ProfilWindow(ProfilWindowController profilWindowController ) {
        // Création de la fenêtre principale
        setTitle("Profil");
        setSize(600, 400);
        setLocationRelativeTo(null);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Création du panneau principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());


        returnButton = new JButton("\u2b98 Back");
        returnButton.setBackground(bleufonce2);
        returnButton.setForeground(Color.WHITE);
        leftPanel.add(returnButton, BorderLayout.NORTH);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        //init parameters panel
        JPanel informationPanel = new JPanel();
        informationPanel.setLayout(new GridLayout( 4,1));
        JButton parameterButton = new JButton("Edit profil \u270E");
        JButton statsButton = new JButton("Stats");
        informationPanel.add(parameterButton);
        informationPanel.add(statsButton);
        leftPanel.add(informationPanel,BorderLayout.CENTER);

        logOutButton= new JButton("Log Out \u274c");
        logOutButton.setForeground(Color.WHITE);
        logOutButton.setBackground(new Color(204, 13, 13));
        leftPanel.add(logOutButton, BorderLayout.SOUTH);
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                /*profilController.sendMessage("/testDeconnexion: ");

                //  boolean userIsValid = loginController.getUserIsValid();
                Boolean userIsValid = profilController.getUserIsValidWithTimeout(2, TimeUnit.SECONDS);

                JOptionPane.showMessageDialog(this, "Veuillez saisir un nom d'utilisateur et un mot de passe.");

                    } else if (userIsValid) {
                        loginController.setUsername(username);
                        loginController.setUserPassword(password);
                        JOptionPane.showMessageDialog(this, "Connexion sucess !");
                        MessageController messageController = new MessageController(loginController.getModel());
                        pageAcceuil view2 = new pageAcceuil(messageController);
                        messageController.setView(view2);
                        dispose();
                        view2.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Incorrect user or password.");
                    }*/

                /*dispose();
                profilController.getModel().getUser();

                Client model = new Client(); //creer le client apres la page login sinon entered trop tot

                LoginController loginController = new LoginController(model);
                LoginPage view = new LoginPage(loginController);
                loginController.setView(view);*/
            }
        });


        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        JLabel informationLabel = new JLabel("Edit Information");
        centerPanel.add(informationLabel, BorderLayout.NORTH);

        JPanel displayPanel = new JPanel(new GridLayout(6, 2));
        JScrollPane paramScrollPane = new JScrollPane(displayPanel);
        centerPanel.add(paramScrollPane,BorderLayout.CENTER);

        JLabel containImageLabel = new JLabel();
        containImageLabel.setVisible(true);
        displayPanel.add(containImageLabel);

        JLabel userImageLabel = new JLabel();
        userImageLabel.setVisible(true);
        userImageLabel.setSize(60,60);
        userImageLabel.setForeground(Color.BLACK);
        containImageLabel.add(userImageLabel);
        JButton imageButton = new JButton("Upload Image \ud83d\udce5");
        imageButton.setBackground(bleufonce2);
        imageButton.setForeground(Color.WHITE);
        displayPanel.add(imageButton);

        imageButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                JFileChooser file = new JFileChooser();
                file.setCurrentDirectory(new File(System.getProperty("user.home")));
                //filter the files
                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg","gif","png");
                file.addChoosableFileFilter(filter);
                int result = file.showSaveDialog(null);
                //if the user click on save in Jfilechooser
                if(result == JFileChooser.APPROVE_OPTION){
                    File selectedFile = file.getSelectedFile();
                    String path = selectedFile.getAbsolutePath();
                    userImageLabel.setIcon(ResizeImage(path, userImageLabel));
                }
                //if the user click on save in Jfilechooser


                else if(result == JFileChooser.CANCEL_OPTION){
                    System.out.println("No File Select");
                }
            }
        });

        JLabel userNameLabel = new JLabel("Name : ");
        displayPanel.add(userNameLabel);
        JTextField nameField = new JTextField(profilWindowController.getModel().getUser().getUsername());
        displayPanel.add(nameField);
        nameField.setSize(200, 24);

        JLabel userPWLabel = new JLabel("Password : ");
        userPWLabel.setBackground(Color.BLUE);
        displayPanel.add(userPWLabel);
        JTextField pwField = new JTextField(profilWindowController.getModel().getUser().getPassword());
        displayPanel.add(pwField);
        pwField.setSize(200, 24);

        JLabel typeLabel = new JLabel("User Type : ");
        typeLabel.setBackground(Color.BLUE);
        displayPanel.add(typeLabel);
        JTextField typeField = new JTextField(profilWindowController.getModel().getUser().getUserType().name());
        displayPanel.add(typeField);
        typeField.setSize(200, 24);

        JLabel userStatusLabel = new JLabel("Status : ");
        displayPanel.add(userStatusLabel);
        //JTextField statusField = new JTextField("online");
        //displayPanel.add(statusField);

        JCheckBox onlineCheckbox = new JCheckBox("online");
        displayPanel.add(onlineCheckbox);
        displayPanel.add(new JLabel());
        JCheckBox awayCheckbox = new JCheckBox("away");
        displayPanel.add(awayCheckbox);
        //fonctions pour avoir seulement une box check a la fois
        ButtonGroup checkBoxGroup = new ButtonGroup();
        checkBoxGroup.add(onlineCheckbox);
        checkBoxGroup.add(awayCheckbox);

        //Boutons save et delete
        JPanel southButtonsPanel = new JPanel();
        southButtonsPanel.setLayout(new BorderLayout());

        JButton saveButton= new JButton("Save Changes \u2705");
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(new Color(33, 141, 10));
        JButton deleteButton= new JButton("Delete account \u274c");
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBackground(new Color(204, 13, 13));
        southButtonsPanel.add(saveButton, BorderLayout.CENTER);
        southButtonsPanel.add(deleteButton, BorderLayout.EAST);
        centerPanel.add(southButtonsPanel, BorderLayout.SOUTH);


        //init stats panel
        JLabel statsLabel = new JLabel("Statistics");

        JPanel statsPanel = new JPanel(new GridLayout(2,1));


        //Creation du panel de statistiques sur les Status
        ChartPanel chartPanel = new ChartPanel(chartStatus());
        chartPanel.setPreferredSize(new java.awt.Dimension(150, 150));
        statsPanel.add(chartPanel);

        //Creation du panel de statistiques sur les Types d'utilisateurs
        ChartPanel chartPanel2 = new ChartPanel(chartType());
        chartPanel2.setPreferredSize(new java.awt.Dimension(150, 150));
        statsPanel.add(chartPanel2);

        //Creation du panel de statistiques sur le nombre de messages dans le temps
        ChartPanel chartPanel3 = new ChartPanel(chartMessages());
        chartPanel3.setPreferredSize(new java.awt.Dimension(150, 150));
        statsPanel.add(chartPanel3);

        //Creation du panel de statistiques sur le nombre de connexions dans le temps
        ChartPanel chartPanelCo = new ChartPanel(chartConnections());
        chartPanelCo.setPreferredSize(new java.awt.Dimension(150, 150));
        statsPanel.add(chartPanelCo);

        JScrollPane statsScrollPane= new JScrollPane(statsPanel);

        onlineCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profilWindowController.sendMessage("/ChangeStatus:online");
            }
        });
        awayCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profilWindowController.sendMessage("/ChangeStatus:away");
            }
        });

        statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                centerPanel.remove(paramScrollPane);
                centerPanel.remove(informationLabel);
                centerPanel.remove(southButtonsPanel);

                centerPanel.add(statsLabel, BorderLayout.NORTH);
                centerPanel.add(statsScrollPane,BorderLayout.CENTER);

                setContentPane(mainPanel);


            }
        });
        saveButton.addActionListener(new ActionListener() {//er
            @Override
            public void actionPerformed(ActionEvent e) {
                profilWindowController.sendMessage("/MAJProfil: "+nameField.getText()+" "+pwField.getText());
                profilWindowController.getModel().getUser().setUsername(nameField.getText());
                profilWindowController.getModel().getUser().setPassword(pwField.getText());
            }
        });

        parameterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                centerPanel.remove(statsLabel);
                centerPanel.remove(statsScrollPane);
                centerPanel.add(informationLabel, BorderLayout.NORTH);
                centerPanel.add(southButtonsPanel, BorderLayout.SOUTH);
                centerPanel.add(paramScrollPane, BorderLayout.CENTER);
                setContentPane(mainPanel);


            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profilWindowController.sendMessage("/DeleteAccount");
            }
        });

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);




        // Ajout du panneau principal à la fenêtre
        setContentPane(mainPanel);
    }



    // Methode to resize imageIcon with the same size of a Jlabel
    public ImageIcon ResizeImage(String ImagePath, JLabel label)
    {
        ImageIcon MyImage = new ImageIcon(ImagePath);
        Image img = MyImage.getImage();
        Image newImg = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
    }

    //fonctions pour créer les graphes
    public JFreeChart chartStatus(){
        DefaultPieDataset dataset1 = new DefaultPieDataset();
        dataset1.setValue("Online", 2);
        dataset1.setValue("Offline", 3);
        dataset1.setValue("Away", 5);

        // Création du camembert par statut
        JFreeChart chart1 = ChartFactory.createPieChart(
                "Nombre d'utilisateurs par statut",
                dataset1,
                true,
                true,
                false
        );
        PiePlot plot1 = (PiePlot) chart1.getPlot();
        plot1.setStartAngle(290);
        plot1.setDirection(Rotation.CLOCKWISE);
        plot1.setForegroundAlpha(0.5f);
        plot1.setNoDataMessage("Aucune donnée à afficher");

        return chart1;
    }


    public JFreeChart chartType(){
        // Création du dataset pour le camembert par type
        DefaultPieDataset dataset2 = new DefaultPieDataset();
        dataset2.setValue("Utilisateurs", 2);
        dataset2.setValue("Modérateurs", 3);
        dataset2.setValue("Administrateurs", 3);
        dataset2.setValue("Bannis", 5);

        // Création du camembert par type
        JFreeChart chart2 = ChartFactory.createPieChart(
                "Nombre d'utilisateurs par type",
                dataset2,
                true,
                true,
                false
        );
        PiePlot plot2 = (PiePlot) chart2.getPlot();
        plot2.setStartAngle(290);
        plot2.setDirection(Rotation.CLOCKWISE);
        plot2.setForegroundAlpha(0.5f);
        plot2.setNoDataMessage("Aucune donnée à afficher");
        return chart2;
    }
    public JFreeChart chartMessages(){
        // Données inventées
        int[] messages = {50, 75, 100, 90, 80, 110, 120, 150, 180, 200, 250, 220, 230, 240, 280, 300, 330, 320, 350, 400};
        Date[] dates = {new Date(122, 0, 1, 0, 0), new Date(122, 0, 2, 0, 0), new Date(122, 0, 3, 0, 0), new Date(122, 0, 4, 0, 0), new Date(122, 0, 5, 0, 0), new Date(122, 0, 6, 0, 0), new Date(122, 0, 7, 0, 0), new Date(122, 0, 8, 0, 0), new Date(122, 0, 9, 0, 0), new Date(122, 0, 10, 0, 0), new Date(122, 0, 11, 0, 0), new Date(122, 0, 12, 0, 0), new Date(122, 0, 13, 0, 0), new Date(122, 0, 14, 0, 0), new Date(122, 0, 15, 0, 0), new Date(122, 0, 16, 0, 0), new Date(122, 0, 17, 0, 0), new Date(122, 0, 18, 0, 0), new Date(122, 0, 19, 0, 0), new Date(122, 0, 20, 0, 0)};

        // Création de la série temporelle
        TimeSeries series = new TimeSeries("Nombre de messages envoyés");
        for (int i = 0; i < messages.length; i++) {
            Hour hour = new Hour(dates[i]);
            series.add(hour, messages[i]);
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);

        // Création du graphique
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Nombre de messages envoyés dans le temps",
                "Date",
                "Nombre de messages",
                dataset,
                true,
                true,
                false
        );
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.GRAY);
        //plot.setRangeGridlinePaint(Color

        // Personnalisation de l'axe des abscisses
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("dd/MM/yyyy"));

        // Personnalisation de la ligne du graphique
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.BLUE);
        plot.setRenderer(renderer);
        return chart;
    }

    public JFreeChart chartConnections(){
        // Création de la série temporelle
        TimeSeries series2 = new TimeSeries("Nombre de connexions");
        series2.add(new Millisecond(new Date(2023, 3, 1, 0, 0, 0)), 10);
        series2.add(new Millisecond(new Date(2023, 3, 2, 0, 0, 0)), 15);
        series2.add(new Millisecond(new Date(2023, 3, 3, 0, 0, 0)), 20);
        series2.add(new Millisecond(new Date(2023, 3, 4, 0, 0, 0)), 30);
        series2.add(new Millisecond(new Date(2023, 3, 5, 0, 0, 0)), 25);
        series2.add(new Millisecond(new Date(2023, 3, 6, 0, 0, 0)), 35);
        series2.add(new Millisecond(new Date(2023, 3, 7, 0, 0, 0)), 40);

        // Ajout de la série temporelle à un dataset
        TimeSeriesCollection datasetCo = new TimeSeriesCollection();
        datasetCo.addSeries(series2);

        // Création du graphique
        JFreeChart chartCo = ChartFactory.createTimeSeriesChart(
                "Nombre de connexions dans le temps",
                "Date",
                "Nombre de connexions",
                datasetCo,
                true,
                true,
                false
        );

        // Personnalisation du graphique
        XYPlot plotCo = chartCo.getXYPlot();

        // Personnalisation de l'axe des abscisses
        DateAxis axisCo = (DateAxis) plotCo.getDomainAxis();
        axisCo.setDateFormatOverride(new SimpleDateFormat("dd/MM/yyyy"));

        // Personnalisation de la ligne du graphique
        XYLineAndShapeRenderer rendererCo = new XYLineAndShapeRenderer();
        rendererCo.setSeriesPaint(0, Color.RED);
        plotCo.setRenderer(rendererCo);

        return chartCo;
    }

    /*public static void main(String[] args) {
        ProfilWindow window = new ProfilWindow();
        window.setVisible(true);
    }*/
}





