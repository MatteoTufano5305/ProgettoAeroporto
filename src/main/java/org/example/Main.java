package org.example.control;

import org.example.gui.LoginInterface;
import javax.swing.*;

/**
 * La classe principale che fa partire tutto il programma
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    LoginInterface loginFrame = new LoginInterface();
                    loginFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Errore nell'avvio della GUI: " + e.getMessage());
                }
            }
        });
    }
}