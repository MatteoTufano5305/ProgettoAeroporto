package org.example.gui;

import org.example.model.UtenteGenerico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Rappresenta la dashboard (schermata principale) per l'Utente Generico.
 Questa classe estende {@link JFrame} e funge da menu di navigazione principale dopo che un utente ha effettuato il login con successo.
 Da qui l'utente può accedere alle funzionalità principali:
 Effettuare una nuova prenotazione.
 Ricercare voli disponibili.
 Visualizzare lo storico delle proprie prenotazioni.
 Effettuare il logout.

 */
public class UserInterface extends JFrame {
    private JPanel UserInterface;
    private JButton effettuaPrenotazioneButton;
    private JButton ricercaVoloButton;
    private JButton indietroButton;
    private JLabel Testo;
    private JButton mostraPrenotazioniButton;


    private UtenteGenerico utenteLoggato;

    /**
     * Costruisce l'interfaccia utente principale.
     * <p>
     * Inizializza i componenti grafici, imposta il messaggio di benvenuto personalizzato
     * e definisce i listener per la navigazione tra le varie finestre dell'applicazione.
     * </p>
     *
     * @param utente L'oggetto {@link UtenteGenerico} che ha effettuato il login.
     * Non deve essere null, altrimenti l'applicazione potrebbe generare errori
     * nelle finestre successive.
     */
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

        mostraPrenotazioniButton = new JButton("Mostra Prenotazioni");
        mostraPrenotazioniButton.setBounds(70, 120, 160, 30);
        UserInterface.add(mostraPrenotazioniButton);

        indietroButton = new JButton("Indietro");
        indietroButton.setBounds(10, 220, 100, 30);
        UserInterface.add(indietroButton);

        setContentPane(UserInterface);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setLocationRelativeTo(null);
        setVisible(true);
/**
 * Azione per tornare a {@link LoginInterface}
 */

        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginInterface();
                dispose();
            }
        });

        /**
         * Azione per andare alla finestra {@link RicercaVolo}
         */
        ricercaVoloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RicercaVolo(utenteLoggato).setVisible(true);
                dispose();
            }
        });

/**
 * Azione per andare alla finestra di {@link SelezioneVolo} per prenotare
 */
        effettuaPrenotazioneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SelezioneVolo selezioneVolo = new SelezioneVolo(utenteLoggato, "modifica");
                selezioneVolo.setVisible(true);
                dispose();
            }
        });
        /**
         * Azione per mostrare la finestra {@link Prenotazioni}
         */
        mostraPrenotazioniButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                Prenotazioni prenotazioni = new Prenotazioni(utente);
                prenotazioni.setVisible(true);
                dispose();
            }
        });
    }
}