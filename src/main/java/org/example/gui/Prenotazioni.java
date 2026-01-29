package org.example.gui;

import org.example.DAO.AeroportoDAO;
import org.example.model.Prenotazione;
import org.example.model.UtenteGenerico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Finestra che visualizza lo storico delle prenotazioni e permette di cancellare la prenotazione.
 * Viene aperta dalla UserInterface e riceve l'utente loggato.
 */
public class Prenotazioni extends JFrame {

    private JPanel PanelPrenotazioni;
    private JTable TabellaPrenotazioni;
    private DefaultTableModel tableModel;
    private JButton indietroButton;
    private JButton cancellaPrenotazioneButton;

    private final AeroportoDAO aeroportoDAO = new AeroportoDAO();
    private final UtenteGenerico utente;

    /**
     * Costruttore: Richiede l'utente loggato per caricare i SUOI dati.
     * @param utente L'utente passato dal menu principale.
     */
    public Prenotazioni(UtenteGenerico utente) {
        super("Le mie Prenotazioni");
        this.utente = utente;

        initUI();
        loadData();
        cancellaPrenotazioneButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int rigaSelezionata = TabellaPrenotazioni.getSelectedRow();

                if (rigaSelezionata == -1) {
                    JOptionPane.showMessageDialog(Prenotazioni.this,
                            "Seleziona una prenotazione dalla tabella per eliminarla.",
                            "Nessuna selezione",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String codicePrenotazione = (String) tableModel.getValueAt(rigaSelezionata, 0);


                int conferma = JOptionPane.showConfirmDialog(Prenotazioni.this,
                        "Sei sicuro di voler cancellare la prenotazione " + codicePrenotazione + "?\nL'operazione è irreversibile.",
                        "Conferma Eliminazione",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);


                if (conferma == JOptionPane.YES_OPTION) {

                    boolean esito = aeroportoDAO.deletePrenotazione(codicePrenotazione);

                    if (esito) {

                        tableModel.removeRow(rigaSelezionata);

                        JOptionPane.showMessageDialog(Prenotazioni.this,
                                "Prenotazione eliminata con successo.",
                                "Successo",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(Prenotazioni.this,
                                "Errore durante l'eliminazione nel database.",
                                "Errore",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });
    }

    /**
     * Inizializza l'interfaccia mettendo una tabella e due tasti:uno per cancellare e uno per indietro
     */
    private void initUI() {
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        PanelPrenotazioni = new JPanel(new BorderLayout(10, 10));
        PanelPrenotazioni.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        JLabel titleLabel = new JLabel("Storico Prenotazioni di " + utente.getNome(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        PanelPrenotazioni.add(titleLabel, BorderLayout.NORTH);


        String[] colonne = {
                "Codice Pren.", "Volo", "Partenza", "Destinazione", "Data", "Ora", "Posto", "Stato"
        };

        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        TabellaPrenotazioni = new JTable(tableModel);
        TabellaPrenotazioni.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TabellaPrenotazioni.setRowHeight(30);
        TabellaPrenotazioni.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(TabellaPrenotazioni);
        PanelPrenotazioni.add(scrollPane, BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        indietroButton = new JButton("Indietro");
        indietroButton.setBounds(10, 10, 100, 30);
        bottomPanel.add(indietroButton);

        cancellaPrenotazioneButton = new JButton("Cancella");
        cancellaPrenotazioneButton.setPreferredSize(new Dimension(160, 30));
        cancellaPrenotazioneButton.setForeground(Color.BLACK);
        cancellaPrenotazioneButton.setFont(new Font("Arial", Font.BOLD, 12));
        bottomPanel.add(cancellaPrenotazioneButton);

        PanelPrenotazioni.add(bottomPanel, BorderLayout.SOUTH);


        indietroButton.addActionListener(e -> {
            new UserInterface(utente).setVisible(true);
            dispose();
        });

        setContentPane(PanelPrenotazioni);
    }

    /**
     * Carica i dati dal Database usando l'ID dell'utente.
     */
    private void loadData() {
        tableModel.setRowCount(0);

        List<Prenotazione> listaPrenotazioni = aeroportoDAO.getPrenotazioniByUtente(utente.getIdUtente());

        if (listaPrenotazioni.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Non hai ancora effettuato prenotazioni.");
        } else {
            for (Prenotazione p : listaPrenotazioni) {
                Object[] riga = {
                        p.getCodicePrenotazione(),
                        p.getVolo().getCodice(),
                        p.getVolo().getScaloPartenza(),
                        p.getVolo().getScaloArrivo(),
                        p.getVolo().getData(),
                        p.getVolo().getOra(),
                        p.getPostoAssegnato(),
                        p.getStato()
                };
                tableModel.addRow(riga);
            }
        }
    }
}