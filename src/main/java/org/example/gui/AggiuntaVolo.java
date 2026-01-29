package org.example.gui;

import javax.swing.*;
import org.example.control.Admin;
import org.example.DAO.AeroportoDAO;
import org.example.model.StatoVolo;
import org.example.model.Volo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
/**
 * Interfaccia grafica dedicata all'inserimento di nuovi voli nel sistema.
 * <p>
 * La classe fornisce un modulo completo per la raccolta dei dati necessari a
 * istanziare un oggetto {@link Volo}. Gestisce l'interazione tra:
 * <ul>
 * <li><b>Componenti testuali:</b> Per codici e scali.</li>
 * <li><b>JSpinner:</b> Per la selezione precisa di date e orari.</li>
 * <li><b>JComboBox:</b> Per la selezione dello {@link StatoVolo} da un set predefinito.</li>
 * </ul>
 * </p>
 * * @author Matteo Tufano,Marino Ometo
 */
public class AggiuntaVolo extends JFrame {
    private JPanel aggiuntaVolo;
    private JLabel titolo;
    private JLabel IdVolo;
    private JLabel CompagniaAerea;
    private JLabel Partenza;
    private JLabel Arrivo;
    private JLabel Data;
    private JLabel Ora;
    private JLabel Stato;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JSpinner comboBox1;
    private JSpinner spinner1;
    private JComboBox<StatoVolo> comboBox2;
    private JButton indietroButton;
    private JButton aggiungiButton;

    private Admin adminLogged;
    /**
     * Inizializza la finestra di inserimento.
     * * @param admin L'oggetto {@link Admin} che esegue l'operazione, necessario per
     * garantire il ritorno alla dashboard corretta tramite {@link AdminInterface}.
     */
    public AggiuntaVolo(Admin admin) {
        super("Aggiunta Volo");
        this.adminLogged=admin;
        aggiuntaVolo = new JPanel();
        aggiuntaVolo.setLayout(null);

        titolo = new JLabel("Inserisci i dati per il volo");
        titolo.setBounds(10, 10, 200, 20);
        aggiuntaVolo.add(titolo);

        IdVolo = new JLabel("Id Volo");
        IdVolo.setBounds(30, 70, 100, 20);
        aggiuntaVolo.add(IdVolo);

        textField1 = new JTextField();
        textField1.setBounds(170, 70, 150, 20);
        aggiuntaVolo.add(textField1);

        CompagniaAerea = new JLabel("Compagnia Aerea");
        CompagniaAerea.setBounds(30, 100, 120, 20);
        aggiuntaVolo.add(CompagniaAerea);

        textField2 = new JTextField();
        textField2.setBounds(170, 100, 150, 20);
        aggiuntaVolo.add(textField2);

        Partenza = new JLabel("Partenza");
        Partenza.setBounds(30, 130, 100, 20);
        aggiuntaVolo.add(Partenza);

        textField3 = new JTextField();
        textField3.setBounds(170, 130, 150, 20);
        aggiuntaVolo.add(textField3);

        Arrivo = new JLabel("Arrivo");
        Arrivo.setBounds(30, 160, 100, 20);
        aggiuntaVolo.add(Arrivo);

        textField4 = new JTextField();
        textField4.setBounds(170, 160, 150, 20);
        aggiuntaVolo.add(textField4);

        Data = new JLabel("Data");
        Data.setBounds(30, 190, 100, 20);
        aggiuntaVolo.add(Data);

        SpinnerDateModel dataModel = new SpinnerDateModel(new Date(),null,null,Calendar.DAY_OF_MONTH);
        comboBox1 = new JSpinner(dataModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(comboBox1,"dd-MM-yyyy");
        comboBox1.setEditor(dateEditor);
        comboBox1.setBounds(170, 190, 150, 20);
        aggiuntaVolo.add(comboBox1);

        Ora = new JLabel("Ora");
        Ora.setBounds(30, 220, 100, 20);
        aggiuntaVolo.add(Ora);

        SpinnerDateModel oraModel = new SpinnerDateModel(new Date(),null,null,Calendar.MINUTE);
        spinner1 = new JSpinner(oraModel);
        JSpinner.DateEditor oraEditor = new JSpinner.DateEditor(spinner1,"HH:mm");
        spinner1.setEditor(oraEditor);
        spinner1.setBounds(170, 220, 150, 20);
        aggiuntaVolo.add(spinner1);

        Stato = new JLabel("Stato");
        Stato.setBounds(30, 250, 100, 20);
        aggiuntaVolo.add(Stato);

        comboBox2 = new JComboBox<>(StatoVolo.values());
        comboBox2.setBounds(170, 250, 150, 20);
        aggiuntaVolo.add(comboBox2);

        indietroButton = new JButton("Indietro");
        indietroButton.setBounds(20, 300, 100, 30);
        aggiuntaVolo.add(indietroButton);

        aggiungiButton = new JButton("Aggiungi");
        aggiungiButton.setBounds(230, 300, 100, 30);
        aggiuntaVolo.add(aggiungiButton);

        setContentPane(aggiuntaVolo);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        indietroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminInterface(adminLogged).setVisible(true);
                dispose();
            }
        });
        aggiungiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inserisciVolo();
            }
        });
    }
    /**
     * Elabora i dati inseriti nei campi della GUI per persistere un nuovo volo.
     * <p>
     * Il processo segue questi step:
     * <ol>
     * <li><b>Validazione:</b> Verifica che i campi testuali non siano vuoti.</li>
     * <li><b>Parsing:</b> Converte i valori dei {@link JSpinner} (Date) in
     * {@link LocalDate} e {@link LocalTime} utilizzando il {@link ZoneId} di sistema.</li>
     * <li><b>Istanziazione:</b> Crea un nuovo oggetto {@link Volo}.</li>
     * <li><b>Persistenza:</b> Invia l'oggetto all'{@link AeroportoDAO} per il salvataggio.</li>
     * </ol>
     * </p>
     * * @throws Exception Gestisce eventuali errori di conversione o problemi di connessione al database
     * mostrando un {@link JOptionPane} di errore.
     */
    private void inserisciVolo() {
        try {
            String codice = textField1.getText().trim();
            String compagnia = textField2.getText().trim();
            String partenza = textField3.getText().trim();
            String arrivo = textField4.getText().trim();

            if (codice.isEmpty() || compagnia.isEmpty() || partenza.isEmpty() || arrivo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Compila tutti i campi di testo!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date dataSelezionata = (Date) comboBox1.getValue();
            LocalDate dataVolo = dataSelezionata.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            Date oraSelezionata = (Date) spinner1.getValue();
            LocalTime oraVolo = oraSelezionata.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

            StatoVolo statoSelezionato = (StatoVolo) comboBox2.getSelectedItem();

            Volo nuovoVolo = new Volo(codice, compagnia, dataVolo, oraVolo, partenza, arrivo, statoSelezionato);

            AeroportoDAO dao = new AeroportoDAO();
            dao.inserisciVolo(nuovoVolo);

            JOptionPane.showMessageDialog(this, "Volo " + codice + " aggiunto con successo!");




        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

}
