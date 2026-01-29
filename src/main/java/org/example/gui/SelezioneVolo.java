package org.example.gui;

import org.example.DAO.AeroportoDAO;
import org.example.control.Login;
import org.example.control.Admin;
import org.example.model.UtenteGenerico;
import org.example.model.Volo;
import org.example.model.StatoVolo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Interfaccia grafica per la selezione di un volo dal database.
 * <p>
 * Questa finestra fa da ponte per alcune funzioni
 * <ul>
 * <li><b>Admin:</b> Permette di selezionare un volo per la modifica dello stato (tramite {@code JComboBox})
 * o per la cancellazione definitiva.</li>
 * <li><b>UtenteGenerico:</b> Permette di selezionare un volo per procedere alla prenotazione dei posti.</li>
 * </ul>
 * </p>
 * * @author Matteo Tufano,Marino Ometo
 */
public class SelezioneVolo extends JFrame {
    private JPanel panel;
    private JComboBox<Volo> comboBoxVoli;
    private JLabel label1;

    private JComboBox<StatoVolo> comboBoxStato;

    private JLabel modificaStatoLabel;
    private JButton confermaButton;
    private JButton indietroButton;

    private String tipoOperazione;
    private Login userLogged;

    /**
     * Inizializza una nuova finestra di selezione volo.
     * * @param utente L'istanza dell'utente loggato ({@link Admin} o {@link UtenteGenerico}).
     * @param tipoOperazione Stringa che definisce l'azione
     * Se null, viene impostata di default su "MODIFICA".
     */
    public SelezioneVolo(Login utente, String tipoOperazione) {
        super("Seleziona un Volo");

        this.userLogged = utente;

        this.tipoOperazione = (tipoOperazione == null) ? "MODIFICA" : tipoOperazione;

        if (utente instanceof Admin) {
            setTitle("Admin: " + this.tipoOperazione.toUpperCase() + " Volo");
        } else if (utente instanceof UtenteGenerico) {
            setTitle("Utente: Prenotazione Volo");
            this.tipoOperazione = "PRENOTA";
        }

        panel = new JPanel();
        panel.setLayout(null);

        label1 = new JLabel("Seleziona il volo:");
        label1.setBounds(10, 10, 200, 20);
        panel.add(label1);

        comboBoxVoli = new JComboBox<>();
        comboBoxVoli.setBounds(10, 35, 320, 30);
        panel.add(comboBoxVoli);

        modificaStatoLabel = new JLabel("Nuovo stato:");
        modificaStatoLabel.setBounds(10, 80, 200, 20);
        panel.add(modificaStatoLabel);

        comboBoxStato = new JComboBox<>(StatoVolo.values());
        comboBoxStato.setBounds(10, 100, 200, 30);
        panel.add(comboBoxStato);

        boolean isModificaMode = (utente instanceof Admin) && "modifica".equalsIgnoreCase(this.tipoOperazione);
        modificaStatoLabel.setVisible(isModificaMode);
        comboBoxStato.setVisible(isModificaMode);

        caricaVoliDalDatabase();

        confermaButton = new JButton("Conferma");
        confermaButton.setBounds(10, 160, 100, 30);
        panel.add(confermaButton);

        indietroButton = new JButton("Indietro");
        indietroButton.setBounds(120, 160, 100, 30);
        panel.add(indietroButton);

        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 250);
        setLocationRelativeTo(null);
        setVisible(true);

        indietroButton.addActionListener(e -> {
            tornaIndietro();
        });

        confermaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Volo voloSelezionato = (Volo) comboBoxVoli.getSelectedItem();

                if (voloSelezionato == null) {
                    JOptionPane.showMessageDialog(null, "Nessun volo selezionato!");
                    return;
                }

                if (userLogged instanceof Admin) {


                    if ("elimina".equalsIgnoreCase(getTipoOperazione())) {
                        int risposta = JOptionPane.showConfirmDialog(null,
                                "Eliminare il volo " + voloSelezionato.getCodice() + "?",
                                "Conferma", JOptionPane.YES_NO_OPTION);

                        if (risposta == JOptionPane.YES_OPTION) {
                            AeroportoDAO dao = new AeroportoDAO();
                            if (dao.deleteVolo(voloSelezionato.getCodice())) {
                                JOptionPane.showMessageDialog(null, "Volo eliminato!");
                                tornaIndietro();
                            } else {
                                JOptionPane.showMessageDialog(null, "Errore eliminazione.");
                            }
                        }


                    } else if ("modifica".equalsIgnoreCase(getTipoOperazione())) {

                        StatoVolo nuovoStato = (StatoVolo) comboBoxStato.getSelectedItem();

                        voloSelezionato.setStatoVolo(nuovoStato);

                        AeroportoDAO dao = new AeroportoDAO();

                        boolean aggiornato = dao.updateStatoVolo(voloSelezionato);

                        if (aggiornato) {
                            JOptionPane.showMessageDialog(null, "Stato aggiornato correttamente a: " + nuovoStato);
                            tornaIndietro();
                        } else {
                            JOptionPane.showMessageDialog(null, "Errore: impossibile aggiornare il Database.");
                        }
                    }

                } else if (userLogged instanceof UtenteGenerico) {
                    dispose();
                    PrenotazioneGrid prenotazioneGUI = new PrenotazioneGrid((UtenteGenerico) userLogged, voloSelezionato);
                    prenotazioneGUI.setVisible(true);
                }
            }
        });
    }

    /**
     * Chiude la finestra corrente e reindirizza l'utente all'interfaccia corretta
     * Se è un {@link UtenteGenerico} allora lo reindirizza a {@link UserInterface}
     * Se invece è un {@link Admin},lo riporta a {@link AdminInterface}
     */
    private void tornaIndietro() {
        dispose();
        if (userLogged instanceof Admin) {
            new AdminInterface((Admin) userLogged);
        } else {
            new LoginInterface();
        }
    }

    /**
     * Qua si ottiene il tipo di operazione che deve eseguire
     * @return
     */
    private String getTipoOperazione() {
        return this.tipoOperazione != null ? this.tipoOperazione : "";
    }

    /**
     * Recupera la lista dei voli aggiornati da {@link AeroportoDAO}
     */
    private void caricaVoliDalDatabase() {
        AeroportoDAO dao = new AeroportoDAO();

        try {
            List<Volo> listaVoli = dao.findAllVoli();
            for (Volo v : listaVoli) {
                comboBoxVoli.addItem(v);
            }
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Impossibile caricare la lista dei voli: " + e.getMessage(),
                    "Errore Database",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}



