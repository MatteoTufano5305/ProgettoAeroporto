package org.example.gui;

import org.example.DAO.AeroportoDAO;
import org.example.model.Volo;
import org.example.control.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AssegnazioneGate extends JFrame {
    private JTable table1;
    private JComboBox<String> comboBox1;
    private JButton btnSalva;
    private JButton indietroButton;
    private final DefaultTableModel tableModel;
    private final AeroportoDAO voloDAO = new AeroportoDAO();
    private String voloSelezionatoId = null;

    private Admin adminLoggato;


    public AssegnazioneGate(Admin admin) {
        super("Assegna il gate");


        this.adminLoggato = admin;

        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] colonne = {"Codice Volo", "Destinazione", "Data", "Ora", "Gate Attuale"};

        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table1 = new JTable(tableModel);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table1.setRowHeight(25);

        add(new JScrollPane(table1), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        bottomPanel.add(new JLabel("Seleziona Gate:"));

        String[] gates = {"A1", "A2", "B1", "B2", "C1", "C2", "D1"};
        comboBox1 = new JComboBox<>(gates);
        bottomPanel.add(comboBox1);

        btnSalva = new JButton("Assegna Gate");
        btnSalva.setEnabled(false);
        bottomPanel.add(btnSalva);

        indietroButton = new JButton("Indietro");
        bottomPanel.add(indietroButton);

        add(bottomPanel, BorderLayout.SOUTH);

        caricaDatiTabella();


        table1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table1.getSelectedRow() != -1) {
                int selectedRow = table1.getSelectedRow();
                voloSelezionatoId = (String) tableModel.getValueAt(selectedRow, 0);
                String currentGate = (String) tableModel.getValueAt(selectedRow, 4);

                btnSalva.setEnabled(true);

                if (currentGate != null && !currentGate.equals("Non assegnato")) {
                    comboBox1.setSelectedItem(currentGate);
                }
            }
        });

        btnSalva.addActionListener(e -> {
            if (voloSelezionatoId != null) {
                String nuovoGate = (String) comboBox1.getSelectedItem();
                int selectedRow = table1.getSelectedRow();

                // --- PRELIEVO SICURO DEI DATI DALLA TABELLA ---
                Object dataObj = tableModel.getValueAt(selectedRow, 2);
                Object oraObj = tableModel.getValueAt(selectedRow, 3);

                java.time.LocalDate dataVolo;
                java.time.LocalTime oraVolo;


                try {
                    dataVolo = java.time.LocalDate.parse(dataObj.toString());
                    oraVolo = java.time.LocalTime.parse(oraObj.toString());
                } catch (Exception ex) {

                    dataVolo = (java.time.LocalDate) dataObj;
                    oraVolo = (java.time.LocalTime) oraObj;
                }

                System.out.println("Dati letti dalla tabella -> Data: " + dataVolo + " Ora: " + oraVolo);


                boolean isOccupato = voloDAO.isGateOccupato(nuovoGate, dataVolo, oraVolo, voloSelezionatoId);

                if (isOccupato) {

                    JOptionPane.showMessageDialog(this,
                            "ERRORE: Il Gate " + nuovoGate + " è già occupato in questo orario!",
                            "Gate non disponibile",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }


                boolean successo = voloDAO.updateGate(voloSelezionatoId, nuovoGate);

                if (successo) {
                    JOptionPane.showMessageDialog(this, "Gate aggiornato con successo!");
                    caricaDatiTabella();
                    btnSalva.setEnabled(false);
                    voloSelezionatoId = null;
                } else {
                    JOptionPane.showMessageDialog(this, "Errore Database", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        indietroButton.addActionListener(e -> {

            AdminInterface adminInterface = new AdminInterface(adminLoggato);
            adminInterface.setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    private void caricaDatiTabella() {

        tableModel.setRowCount(0);

        try {
            List<Volo> listaVoli = voloDAO.findAllVoli();
            for (Volo v : listaVoli) {
                String gateVisibile = (v.getGateAssegnato() != null) ? v.getGateAssegnato().toString() : "Non assegnato";
                Object[] riga = { v.getCodice(), v.getScaloArrivo(), v.getData(), v.getOra(), gateVisibile };
                tableModel.addRow(riga);
            }

        } catch (java.sql.SQLException e) {

            e.printStackTrace(); // Utile per il debug nella console
            JOptionPane.showMessageDialog(this,
                    "Errore durante il caricamento dei voli: " + e.getMessage(),
                    "Errore Database",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {

        Admin adminFinto = new Admin(1, "test", "test");
        SwingUtilities.invokeLater(() -> new AssegnazioneGate(adminFinto));
    }
}