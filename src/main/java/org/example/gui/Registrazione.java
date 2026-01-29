package org.example.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.example.DAO.AeroportoDAO;

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
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Registrazione::new);
    }
}
