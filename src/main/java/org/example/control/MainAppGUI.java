package org.example.control;

import org.example.gui.LoginInterface;
import org.example.util.DatabaseManager;

import javax.swing.*;

public class MainAppGUI {

    public static void main(String[] args) {

        if (!DatabaseManager.isDatabaseAvailable()) {
            JOptionPane.showMessageDialog(null,
                    "Errore Critico: Impossibile connettersi al Database.\nControlla che PostgreSQL sia acceso.",
                    "Errore DB",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        SwingUtilities.invokeLater(() -> {
            new LoginInterface();
        });
    }
}