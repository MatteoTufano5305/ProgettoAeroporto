package org.example.gui;

import org.example.DAO.AeroportoDAO;
import org.example.model.UtenteGenerico;
import org.example.model.Volo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Interfaccia grafica per la ricerca avanzata dei voli nel sistema.
 * <p>
 * Consente all'utente di filtrare i voli disponibili attraverso due modalità:
 * <ul>
 * <li><b>Ricerca Parametrica:</b> Combinazione di partenza, arrivo, data e orario (abilitabili tramite {@link JCheckBox}).</li>
 * <li><b>Ricerca per ID:</b> Ricerca puntuale tramite codice univoco del volo (esclusiva rispetto agli altri filtri).</li>
 * </ul>
 * I risultati vengono estratti dal database tramite il pattern DAO e visualizzati in una finestra di riepilogo.
 * </p>
 *
 * @author Matteo Tufano,Marino Ometo
 */
public class RicercaVolo extends JFrame {
    private JPanel mainPanel;

    private JCheckBox checkUsaPartenza;
    private JCheckBox checkArrivo;
    private JCheckBox checkUsaData;
    private JCheckBox checkUsaOrario;

    private JTextField textFieldPartenza;
    private JTextField textFieldArrivo;
    private JSpinner spinnerData;
    private JSpinner spinnerOrario;

    private JCheckBox cercaTramiteIDBox;
    private JTextField textFieldID;

    private JButton cercaButton;
    private JButton indietroButton;
    private JButton mostraTuttiButton;


    private UtenteGenerico utenteLoggato;
    /**
     * Costruisce l'interfaccia di ricerca.
     * Inizializza i componenti grafici e imposta i listener per la gestione dinamica dei campi.
     * * @param utente L'oggetto {@link UtenteGenerico} serve per gestire il ritorno alla {@link UserInterface}.
     */

    public RicercaVolo(UtenteGenerico utente) {
        super("Ricerca Volo Avanzata");

        this.utenteLoggato = utente;

        mainPanel = new JPanel();
        mainPanel.setLayout(null);

        checkUsaPartenza = new JCheckBox("Filtra per Partenza");
        checkUsaPartenza.setBounds(30, 40, 150, 20);
        mainPanel.add(checkUsaPartenza);

        textFieldPartenza = new JTextField();
        textFieldPartenza.setBounds(190, 40, 150, 20);
        textFieldPartenza.setEnabled(false);
        mainPanel.add(textFieldPartenza);

        checkArrivo = new JCheckBox("Filtra per Arrivo");
        checkArrivo.setBounds(30, 80, 150, 20);
        mainPanel.add(checkArrivo);

        textFieldArrivo = new JTextField();
        textFieldArrivo.setBounds(190, 80, 150, 20);
        textFieldArrivo.setEnabled(false);
        mainPanel.add(textFieldArrivo);

        checkUsaData = new JCheckBox("Filtra per Data");
        checkUsaData.setBounds(30, 120, 150, 20);
        mainPanel.add(checkUsaData);

        SpinnerDateModel modelData = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        spinnerData = new JSpinner(modelData);
        JSpinner.DateEditor editorData = new JSpinner.DateEditor(spinnerData, "dd/MM/yyyy");
        spinnerData.setEditor(editorData);
        spinnerData.setBounds(190, 120, 150, 25);
        spinnerData.setEnabled(false);
        mainPanel.add(spinnerData);

        checkUsaOrario = new JCheckBox("Filtra per Orario");
        checkUsaOrario.setBounds(30, 160, 150, 20);
        mainPanel.add(checkUsaOrario);

        SpinnerDateModel modelOrario = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
        spinnerOrario = new JSpinner(modelOrario);
        JSpinner.DateEditor editorOrario = new JSpinner.DateEditor(spinnerOrario, "HH:mm");
        spinnerOrario.setEditor(editorOrario);
        spinnerOrario.setBounds(190, 160, 80, 25);
        spinnerOrario.setEnabled(false);
        mainPanel.add(spinnerOrario);

        JSeparator sep = new JSeparator();
        sep.setBounds(20, 195, 400, 10);
        mainPanel.add(sep);

        cercaTramiteIDBox = new JCheckBox("Cerca invece tramite ID");
        cercaTramiteIDBox.setBounds(30, 210, 200, 20);
        mainPanel.add(cercaTramiteIDBox);

        textFieldID = new JTextField();
        textFieldID.setBounds(240, 210, 100, 20);
        textFieldID.setVisible(false);
        mainPanel.add(textFieldID);

        indietroButton = new JButton("Indietro");
        indietroButton.setBounds(30, 300, 90, 30);
        mainPanel.add(indietroButton);

        cercaButton = new JButton("Cerca");
        cercaButton.setBounds(130, 300, 90, 30);
        mainPanel.add(cercaButton);

        mostraTuttiButton = new JButton("Vedi Tutti");
        mostraTuttiButton.setBounds(230, 300, 100, 30);
        mainPanel.add(mostraTuttiButton);

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400);
        setLocationRelativeTo(null);
        setVisible(true);
/**
 * Tutte le azioni per filtrare una ricerca basando su un fattore
 * Se si sceglie di selezionare l'ID,le altre funzioni non sarà possibile interagirci
 *
 */
        checkUsaPartenza.addActionListener(e -> textFieldPartenza.setEnabled(checkUsaPartenza.isSelected()));
        checkArrivo.addActionListener(e -> textFieldArrivo.setEnabled(checkArrivo.isSelected()));
        checkUsaData.addActionListener(e -> spinnerData.setEnabled(checkUsaData.isSelected()));
        checkUsaOrario.addActionListener(e -> spinnerOrario.setEnabled(checkUsaOrario.isSelected()));

        cercaTramiteIDBox.addActionListener(e -> {
            boolean isID = cercaTramiteIDBox.isSelected();
            textFieldID.setVisible(isID);
            checkUsaPartenza.setEnabled(!isID);
            checkArrivo.setEnabled(!isID);
            checkUsaData.setEnabled(!isID);
            checkUsaOrario.setEnabled(!isID);
            if (isID) textFieldID.setText("");
        });
        /**
         * Listener del pulsante Cerca:
         * Estrae i valori dai componenti abilitati, effettua il casting dei modelli
         * degli spinner (Date) in tipi moderni (LocalDate/LocalTime) e invoca il DAO.
         **/
        cercaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AeroportoDAO dao = new AeroportoDAO();
                List<Volo> risultati = null;


                if (cercaTramiteIDBox.isSelected()) {
                    String codice = textFieldID.getText();
                    Volo v = dao.findVoloByCodice(codice);
                    if (v != null) mostraRisultati(List.of(v));
                    else JOptionPane.showMessageDialog(null, "Nessun volo con ID: " + codice);
                    return;
                }

                try {
                    String partenza = checkUsaPartenza.isSelected() ? textFieldPartenza.getText().trim() : null;
                    String arrivo = checkArrivo.isSelected() ? textFieldArrivo.getText().trim() : null;

                    LocalDate data = null;
                    if (checkUsaData.isSelected()) {
                        Date d = (Date) spinnerData.getValue();
                        data = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    }

                    LocalTime ora = null;
                    if (checkUsaOrario.isSelected()) {
                        Date t = (Date) spinnerOrario.getValue();
                        ora = t.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                    }

                    if (partenza == null && arrivo == null && data == null && ora == null) {
                        JOptionPane.showMessageDialog(null, "Seleziona almeno un filtro o usa 'Vedi Tutti'");
                        return;
                    }

                    risultati = dao.ricercaDinamicaVoli(partenza, arrivo, data, ora);
                    mostraRisultati(risultati);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Errore dati: " + ex.getMessage());
                }
            }
        });
/**
 * Genera una vista formattata dei voli trovati.
 * <p>
 * Utilizza uno {@link StringBuilder} per costruire un riepilogo testuale e lo
 * inserisce in una {@link JTextArea} all'interno di uno scroll pane per gestire
 * liste di risultati lunghe.
 * </p>
 * * @param voli La lista di oggetti {@link Volo} da mostrare. Se null o vuota,
 * mostra un avviso all'utente.
 */
        mostraTuttiButton.addActionListener(e -> {
            try {

                mostraRisultati(new AeroportoDAO().findAllVoli());
            } catch (java.sql.SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Errore durante il recupero dei voli: " + ex.getMessage(),
                        "Errore Database",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (utenteLoggato != null) {
                    new UserInterface(utenteLoggato).setVisible(true);
                } else {
                    new LoginInterface().setVisible(true);
                }
                dispose();
            }
        });
    }

    private void mostraRisultati(List<Volo> voli) {
        if (voli == null || voli.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nessun volo trovato.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Voli trovati: ").append(voli.size()).append("\n\n");
        for (Volo v : voli) {
            sb.append("").append(v.getCodice())
                    .append(" | ").append(v.getCompagniaAerea()).append("\n")
                    .append("   ").append(v.getScaloPartenza()).append(" -> ").append(v.getScaloArrivo()).append("\n")
                    .append("   Data: ").append(v.getData())
                    .append("   Ora: ").append(v.getOra()).append("\n")
                    .append("----------------------------\n");
        }
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Risultati", JOptionPane.INFORMATION_MESSAGE);
    }

}