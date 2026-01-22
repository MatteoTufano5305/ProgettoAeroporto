package org.example.gui;

import org.example.model.UtenteGenerico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterface extends JFrame {
    private JPanel UserInterface;
    private JButton effettuaPrenotazioneButton;
    private JButton ricercaVoloButton;
    private JButton indietroButton;
    private JLabel Testo;


    private UtenteGenerico utenteLoggato;


    public UserInterface(UtenteGenerico utente) {
        super("User Interface");


        this.utenteLoggato = utente;

        UserInterface = new JPanel();
        UserInterface.setLayout(null);

        Testo = new JLabel("Benvenuto " + utente.getNome());
        Testo.setBounds(50, 10, 200, 20);
        UserInterface.add(Testo);

        effettuaPrenotazioneButton = new JButton("Effettua Prenotazione");
        effettuaPrenotazioneButton.setBounds(70, 40, 160, 30);
        UserInterface.add(effettuaPrenotazioneButton);

        ricercaVoloButton = new JButton("Ricerca Volo");
        ricercaVoloButton.setBounds(70, 80, 160, 30);
        UserInterface.add(ricercaVoloButton);

        indietroButton = new JButton("Indietro");
        indietroButton.setBounds(10, 220, 100, 30);
        UserInterface.add(indietroButton);

        setContentPane(UserInterface);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setLocationRelativeTo(null);
        setVisible(true);


        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginInterface(); // Torna al login
                dispose();
            }
        });


        ricercaVoloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RicercaVolo(utenteLoggato).setVisible(true);
                dispose();
            }
        });


        effettuaPrenotazioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SelezioneVolo selezioneVolo = new SelezioneVolo(utenteLoggato, "modifica");
                selezioneVolo.setVisible(true);
                dispose();
            }
        });
    }


    public static void main(String[] args) {
        UtenteGenerico userTest = new UtenteGenerico(1, "testUser", "pass", "Mario Rossi");
        SwingUtilities.invokeLater(() -> new UserInterface(userTest));
    }
}