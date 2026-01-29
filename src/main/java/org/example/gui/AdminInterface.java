package org.example.gui;

import org.example.control.Admin;
import org.example.gui.LoginInterface;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/** Rappresenta schermata principale per l'Amministratore.
 Questa classe estende {@link JFrame} e funge da menu di navigazione principale dopo che un utente ha effettuato il login con successo.
 Da qui l'utente può accedere alle funzionalità principali:
 Aggiungere,Cancellare,Modificare i voli
 Assegnare il gate
 Effettuare il logout.

 */
public class AdminInterface extends JFrame {
    private JPanel contentPane;
    private JLabel lblNewLabel;
    private JButton aggiungiVoloButton;
    private JButton eliminaVoloButton;
    private JButton modificaVoloButton;
    private JButton assegnaGateButton;
    private JButton indietroButton;

    /**
     * Costruisce l'interfaccia e inizializza i componenti grafici.
     * <p>
     * Il costruttore configura il layout assoluto e associa a ogni pulsante
     * un'azione specifica, iniettando l'oggetto {@code admin} nelle finestre di destinazione.
     * </p>
     * * @param admin L'istanza di {@link Admin} che ha effettuato l'accesso.
     */
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

        /**
         * Gestione della Navigazione:
         * Ogni azione prevede il dispose() della finestra corrente per liberare risorse
         * di memoria prima di aprire la destinazione richiesta.
         */

        /**
         * Apre il modulo di selezione per la MODIFICA dello stato di un volo.
         * Passa la stringa costante "modifica" per istruire la classe di destinazione.
         */

        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginInterface();
                dispose();
            }
        });
/**
 * Azione che indirizza alla modfica del volo,aprendo {@link SelezioneVolo}
 */
        modificaVoloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SelezioneVolo selezioneVolo = new SelezioneVolo(adminLoggato,"modifica");
                selezioneVolo.setVisible(true);
                dispose();
            }
        });
/**
 * Azione che porta all'eliminazione del volo
 * come nella modifica,anche qui apre {@link SelezioneVolo}
 */
        eliminaVoloButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                SelezioneVolo selezioneVolo = new SelezioneVolo(adminLoggato,"elimina");
                selezioneVolo.setVisible(true);
                dispose();
            }
        });
/**
 * Azione per aprire la finestra dell'{@link AggiuntaVolo}
 */
        aggiungiVoloButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AggiuntaVolo aggiuntaVolo = new AggiuntaVolo(adminLoggato);
                aggiuntaVolo.setVisible(true);
                dispose();
            }
        });

/**
 * Azione per aprire la finestra dell'{@link AssegnazioneGate}
 */
        assegnaGateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new AssegnazioneGate(adminLoggato).setVisible(true);
                dispose();
            }
        });

        setVisible(true);
    }

}