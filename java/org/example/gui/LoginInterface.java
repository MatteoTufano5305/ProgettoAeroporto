package org.example.gui;

import org.example.control.Admin;
import org.example.control.Login;
import org.example.DAO.AeroportoDAO;
import org.example.model.UtenteGenerico;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = textField1.getText();
                String pass = new String(passwordField1.getPassword());

                Login utenteTrovato = userDAO.findByUsernameAndPassword(user, pass);

                if (utenteTrovato != null) {
                    if (utenteTrovato instanceof Admin) {
                        JOptionPane.showMessageDialog(null, "Admin logged in");


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
        registratiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Registrazione().setVisible(true);
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginInterface::new);
    }
}