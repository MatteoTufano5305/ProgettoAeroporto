package org.example.gui;

import org.example.DAO.AeroportoDAO;
import org.example.model.Prenotazione;
import org.example.model.StatoPrenotazione;
import org.example.model.UtenteGenerico;
import org.example.model.Volo;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Interfaccia grafica (GUI) per la visualizzazione e la selezione dei posti a sedere.
 * <p>
 * Questa classe estende {@link JFrame} e interagisce direttamente con {@link AeroportoDAO}
 * per recuperare lo stato dei posti e salvare le prenotazioni.
 * I posti sono rappresentati visivamente:
 * Verde:Posto libero.
 * <li><b>Rosso:</b> Posto occupato (già presente nel DB).
 * </ul>
 * </p>
 */
public class PrenotazioneGrid extends JFrame {


    private JPanel mainPanel;
    private JPanel gridPanel;
    private JLabel titleLabel;
    private JButton indietroButton;

    private final UtenteGenerico utente;
    private final Volo volo;
    private final AeroportoDAO dao;

    /**
     * Costruisce l'interfaccia di selezione posti.
     * Inizializza il DAO per le interrogazioni al database.
     *
     * @param utente L'utente loggato.
     * @param volo   Il volo selezionato.
     */
    public PrenotazioneGrid(UtenteGenerico utente, Volo volo) {
        super("Scegli il Posto");
        this.utente = utente;
        this.volo = volo;
        this.dao = new AeroportoDAO();

        initUI();
    }

    /**
     * Inizializza i componenti grafici e genera la griglia.
     */
    private void initUI() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        titleLabel = new JLabel("Volo " + volo.getCodice() + " | Scegli un posto verde", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(5, 10, 10, 10));

        List<Integer> occupati = dao.getPostiOccupati(volo.getCodice());

        for (int i = 1; i <= 50; i++) {
            JButton seatButton = createSeatButton(i, occupati.contains(i));
            gridPanel.add(seatButton);
        }

        mainPanel.add(gridPanel, BorderLayout.CENTER);


        indietroButton = new JButton("Annulla");
        mainPanel.add(indietroButton, BorderLayout.SOUTH);

        indietroButton.addActionListener(e -> {
            new UserInterface(utente).setVisible(true);
            dispose();
        });

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
    }

    /**
     * Helper per creare i bottoni della griglia.
     */
    private JButton createSeatButton(int numeroPosto, boolean isOccupato) {
        JButton btn = new JButton(String.valueOf(numeroPosto));

        if (isOccupato) {
            btn.setBackground(Color.RED);
            btn.setForeground(Color.WHITE);
            btn.setEnabled(false);
        } else {
            btn.setBackground(Color.GREEN);
            btn.setForeground(Color.BLACK);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(e -> gestisciPrenotazione(numeroPosto));
        }

        btn.setOpaque(true);
        btn.setBorderPainted(false);
        return btn;
    }

    /**
     * Gestisce la logica di salvataggio prenotazione chiamando il DAO.
     * Gestisce esplicitamente {@link SQLException} in caso di errori del DB.
     *
     * @param numeroPosto Il posto scelto.
     */
    private void gestisciPrenotazione(int numeroPosto) {
        String nomePasseggero = JOptionPane.showInputDialog(this, "Inserisci Nome e Cognome del Passeggero:");

        if (nomePasseggero == null || nomePasseggero.trim().isEmpty()) {
            return;
        }

        int conferma = JOptionPane.showConfirmDialog(this,
                "Confermi la prenotazione del posto " + numeroPosto + " per " + nomePasseggero + "?",
                "Conferma", JOptionPane.YES_NO_OPTION);

        if (conferma == JOptionPane.YES_OPTION) {
            try {
                String codicePrenotazione = "PRN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                String numeroBiglietto = "TIX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

                Prenotazione nuovaPrenotazione = new Prenotazione(
                        codicePrenotazione,
                        numeroPosto,
                        LocalDateTime.now(),
                        nomePasseggero,
                        numeroBiglietto,
                        utente,
                        volo
                );
                nuovaPrenotazione.setStato(StatoPrenotazione.CONFERMATA);


                dao.inserisciPrenotazione(nuovaPrenotazione);

                JOptionPane.showMessageDialog(this, "Prenotazione salvata!\nCodice: " + codicePrenotazione);


                new PrenotazioneGrid(utente, volo).setVisible(true);
                dispose();

            } catch (SQLException ex) {

                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Errore Database: " + ex.getMessage(),
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}