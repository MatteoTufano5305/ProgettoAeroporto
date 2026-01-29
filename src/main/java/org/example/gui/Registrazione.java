package org.example.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.example.DAO.AeroportoDAO;

/**
 * Interfaccia grafica dedicata alla creazione di nuovi account nel sistema aeroportuale.
 * <p>
 * La classe permette l'inserimento di credenziali (username e password) e
 * la specifica dei privilegi tramite una {@link JCheckBox}.
 * Una volta convalidati i dati, la persistenza viene delegata al metodo statico
 * {@link org.example.DAO.AeroportoDAO#registerUser}.
 * </p>
 * * @author Matteo Tufano,Marino Ometo
 * @version 1.0
 */
public class Registrazione extends JFrame {
    private JPanel panel;
    private JLabel titolo;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JCheckBox adminCheckBox;
    private JButton registraButton;
    private JButton indietroButton;
    private JLabel nomeUtente;
    private JLabel Password;
    /**
     * Costruisce la finestra di registrazione e inizializza i componenti grafici.
     * <p>
     * Vengono definiti i listener per:
     * <ul>
     * <li><b>Ritorno al Login:</b> Chiude la finestra corrente e apre {@link LoginInterface}.</li>
     * <li><b>Processo di Registrazione:</b> Valida l'input, invoca il DAO e gestisce i feedback utente.</li>
     * </ul>
     * </p>
     */
    public Registrazione() {
        super("Registrazione");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        panel = new JPanel();
        panel.setLayout(null);

        titolo = new JLabel("Inserisci le credenziali per registrarti");
        titolo.setBounds(10, 10, 300, 20);
        panel.add(titolo);

        nomeUtente = new JLabel("Nome utente");
        nomeUtente.setBounds(70, 70, 100, 20);
        panel.add(nomeUtente);

        textField1 = new JTextField();
        textField1.setBounds(170, 70, 200, 25);
        panel.add(textField1);

        Password = new JLabel("Password");
        Password.setBounds(70, 120, 100, 20);
        panel.add(Password);

        passwordField1 = new JPasswordField();
        passwordField1.setBounds(170, 120, 200, 25);
        panel.add(passwordField1);

        adminCheckBox = new JCheckBox("Admin");
        adminCheckBox.setBounds(165, 160, 100, 20);
        panel.add(adminCheckBox);

        registraButton = new JButton("Registrati");
        registraButton.setBounds(170, 200, 150, 30);
        panel.add(registraButton);

        indietroButton = new JButton("Indietro");
        indietroButton.setBounds(50, 200, 100, 30);
        panel.add(indietroButton);

        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginInterface().setVisible(true);
                dispose();

            }
        });
        /**
         * Gestore dell'evento click sul pulsante "Registrati".
         * <p>
         * Flusso di esecuzione:
         * <ol>
         * <li>Estrazione dell'input dai campi {@code textField1} e {@code passwordField1}.</li>
         * <li>Controllo di validità: verifica che i campi non siano vuoti.</li>
         * <li>Interazione DAO: tenta l'inserimento nel database tramite {@link AeroportoDAO#registerUser}.</li>
         * <li>Esito: mostra un messaggio di successo o di errore (es. username già esistente).</li>
         * </ol>
         * </p>
         */
        registraButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String usernameInserito = textField1.getText();
                String passwordInserita = new String(passwordField1.getPassword());
                boolean isAdmin = adminCheckBox.isSelected();

                if (usernameInserito.isEmpty() || passwordInserita.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Compila tutti i campi!", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean successo = AeroportoDAO.registerUser(usernameInserito, passwordInserita, usernameInserito, isAdmin);


                if (successo) {
                    JOptionPane.showMessageDialog(null, "Registrazione avvenuta con successo!");

                    textField1.setText("");
                    passwordField1.setText("");
                    adminCheckBox.setSelected(false);


                } else {
                    JOptionPane.showMessageDialog(null, "Errore nella registrazione.\nProbabilmente l'utente esiste già.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
