package org.example.gui;

import org.example.control.Admin;
import org.example.gui.LoginInterface;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminInterface extends JFrame {
    private JPanel contentPane;
    private JLabel lblNewLabel;
    private JButton aggiungiVoloButton;
    private JButton eliminaVoloButton;
    private JButton modificaVoloButton;
    private JButton assegnaGateButton;
    private JButton indietroButton;


    private Admin adminLoggato;


    public AdminInterface(Admin admin) {
        super("AdminInterface");


        this.adminLoggato = admin;

        contentPane = new JPanel();
        contentPane.setLayout(null);

        lblNewLabel = new JLabel("Benvenuto " + admin.getUsername() + ", cosa vuole fare?");
        lblNewLabel.setBounds(50,10,300,20);
        contentPane.add(lblNewLabel);

        aggiungiVoloButton = new JButton("Aggiungi Volo");
        aggiungiVoloButton.setBounds(120,50,160,30);
        contentPane.add(aggiungiVoloButton);

        eliminaVoloButton = new JButton("Elimina Volo");
        eliminaVoloButton.setBounds(120,100,160,30);
        contentPane.add(eliminaVoloButton);

        modificaVoloButton = new JButton("Modifica Stato Volo");
        modificaVoloButton.setBounds(120,150,160,30);
        contentPane.add(modificaVoloButton);

        assegnaGateButton = new JButton("Assegna Gate");
        assegnaGateButton.setBounds(120,200,160,30);
        contentPane.add(assegnaGateButton);

        indietroButton = new JButton("Indietro");
        indietroButton.setBounds(10,270,100,30);
        contentPane.add(indietroButton);

        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400,350);
        setLocationRelativeTo(null);



        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginInterface();
                dispose();
            }
        });

        modificaVoloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelezioneVolo selezioneVolo = new SelezioneVolo(adminLoggato,"modifica");
                selezioneVolo.setVisible(true);
                dispose();
            }
        });

        eliminaVoloButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                SelezioneVolo selezioneVolo = new SelezioneVolo(adminLoggato,"elimina");
                selezioneVolo.setVisible(true);
                dispose();
            }
        });

        aggiungiVoloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AggiuntaVolo aggiuntaVolo = new AggiuntaVolo(adminLoggato);
                aggiuntaVolo.setVisible(true);
                dispose();
            }
        });


        assegnaGateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new AssegnazioneGate(adminLoggato).setVisible(true);
                dispose();
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        Admin adminTest = new Admin(1, "admin", "pass");
        SwingUtilities.invokeLater(() -> new AdminInterface(adminTest));
    }
}