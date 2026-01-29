package org.example.gui;

import org.example.DAO.AeroportoDAO;
import org.example.model.Volo;
import org.example.control.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Interfaccia grafica dedicata alla gestione dell'assegnazione dei Gate aeroportuali.
 * <p>
 * Questa classe è accessibile esclusivamente agli utenti con privilegi di {@link Admin}.
 * Permette di:
 * <ul>
 * <li>Visualizzare l'elenco completo dei voli e il loro gate attuale.</li>
 * <li>Selezionare un volo e assegnargli un nuovo gate tramite un menu a tendina.</li>
 * <li><b>Verificare automaticamente i conflitti:</b> Il sistema impedisce di assegnare un gate
 * se questo è già occupato da un altro volo nello stesso orario (gestito tramite {@link AeroportoDAO#isGateOccupato}).</li>
 * </ul>
 * </p>
 */
public class AssegnazioneGate extends JFrame {
    private JTable table1;
    private JComboBox<String> comboBox1;
    private JButton btnSalva;
    private JButton indietroButton;
    private final DefaultTableModel tableModel;
    private final AeroportoDAO voloDAO = new AeroportoDAO();
    /**
     * Memorizza l'ID (Codice) del volo attualmente selezionato nella tabella.
     * Se null, nessun volo è selezionato.
     */
    private String voloSelezionatoId = null;

    private Admin adminLoggato;

    /**
     * Costruisce l'interfaccia di assegnazione gate.
     * <p>
     * Inizializza la tabella, i componenti di input e definisce i Listener per gli eventi.
     * In particolare, gestisce la logica del pulsante "Assegna Gate" che:
     * <ol>
     * <li>Recupera data e ora del volo selezionato.</li>
     * <li>Controlla tramite DAO se il nuovo gate è libero.</li>
     * <li>Se libero, aggiorna il database e ricarica la tabella.</li>
     * <li>Se occupato, mostra un messaggio di errore bloccante.</li>
     * </ol>
     * </p>
     *
     * @param admin L'amministratore che ha effettuato l'accesso. Necessario per la navigazione "Indietro".
     */
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
    /**
     * Recupera la lista aggiornata dei voli dal Database e popola la JTable.
     * <p>
     * Gestisce eventuali eccezioni SQL mostrando un messaggio di errore all'utente.
     * Se un volo non ha un gate assegnato, visualizza "Non assegnato".
     * </p>
     */
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

            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Errore durante il caricamento dei voli: " + e.getMessage(),
                    "Errore Database",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}