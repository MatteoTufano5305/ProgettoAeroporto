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

    private void tornaIndietro() {
        dispose();
        if (userLogged instanceof Admin) {
            new AdminInterface((Admin) userLogged);
        } else {
            new LoginInterface();
        }
    }

    private String getTipoOperazione() {
        return this.tipoOperazione != null ? this.tipoOperazione : "";
    }
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
    public static void main(String[] args) {
        Admin adminTest = new Admin(1, "admin", "password");
        SwingUtilities.invokeLater(() -> {
            new SelezioneVolo(adminTest, "modifica");
        });
    }
}


