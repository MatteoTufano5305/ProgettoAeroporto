package org.example.control;

import javax.swing.*;
// ... altre importazioni


public class Main {

    public static void main(String[] args) {
        // Avvia l'interfaccia grafica in modo sicuro nell'Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Questa è l'unica riga che avvia tutto il progetto
                new MainAppGUI();
            }
        });
    }
}