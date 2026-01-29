package org.example.gui;

import org.example.control.Admin;
import org.example.control.Login;
import org.example.DAO.AeroportoDAO;
import org.example.model.UtenteGenerico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Rappresenta la finestra di autenticazione principale dell'applicazione.
 * <p>
 * La classe gestisce l'inserimento delle credenziali, la verifica tramite il DAO
 * e il reindirizzamento dinamico dell'utente in base al suo ruolo (Admin o Utente Generico).
 * Funge inoltre da punto di accesso per la procedura di registrazione nuovi utenti.
 * </p>
 * * @see org.example.DAO.AeroportoDAO
 * @see org.example.control.Login
 */
public class LoginInterface extends JFrame {
    private JPanel mainPanel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton confermaButton;
    private JLabel Username;
    private JLabel Password;
    private JButton registratiButton;

    private AeroportoDAO userDAO = new AeroportoDAO();

    public LoginInterface() {
        super("Login Interface");

        mainPanel = new JPanel();
        mainPanel.setLayout(null);

        Username = new JLabel("Username");
        Username.setBounds(100, 50, 100, 30);
        mainPanel.add(Username);

        textField1 = new JTextField();
        textField1.setBounds(200, 50, 200, 25);
        mainPanel.add(textField1);

        Password = new JLabel("Password");
        Password.setBounds(100, 100, 100, 30);
        mainPanel.add(Password);

        passwordField1 = new JPasswordField();
        passwordField1.setBounds(200, 100, 200, 25);
        mainPanel.add(passwordField1);

        confermaButton = new JButton("Conferma");
        confermaButton.setBounds(300, 200, 100, 40);
        mainPanel.add(confermaButton);

        registratiButton = new JButton("Registrati");
        registratiButton.setBounds(100, 200, 140, 40);
        mainPanel.add(registratiButton);

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
/**
 * Gestore dell'evento click sul pulsante "Conferma".
 * 1. Recupera l'input dall'utente.
 * 2. Interroga il DB tramite {@link AeroportoDAO#findByUsernameAndPassword(String, String)}.
 * 3. Se l'autenticazione ha successo, esegue il casting dell'oggetto {@link Login}
 * per aprire l'interfaccia corretta.
 * 4. Altrimenti, mostra un messaggio di errore.
 */
        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = textField1.getText();
                String pass = new String(passwordField1.getPassword());

                Login utenteTrovato = userDAO.findByUsernameAndPassword(user, pass);

                if (utenteTrovato != null) {
                    if (utenteTrovato instanceof Admin) {

                        Admin adminObj = (Admin) utenteTrovato;
                        new AdminInterface(adminObj).setVisible(true);

                        dispose();
                    } else if (utenteTrovato instanceof UtenteGenerico) {

                        UtenteGenerico utenteGen = (UtenteGenerico) utenteTrovato;
                        new UserInterface(utenteGen).setVisible(true);

                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Username o Password errati", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        setVisible(true);
        /**
         * Chiude la finestra corrente e apre {@link Registrazione}.
         */
        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Registrazione().setVisible(true);
                dispose();
            }
        });
    }


}