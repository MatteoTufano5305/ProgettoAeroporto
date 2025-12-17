package org.example.control;

import org.example.model.*;
import org.example.service.UserService;
import org.example.util.DatabaseManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Questa classe gestisce tutta l'interfaccia grafica
 * Agisce come "Controller" principale
 *
 * Le funzionalità sono divise in base all'utente
 *
 * UtenteGenerico:Prenotazione e ricerca volo
 * Admin:Gestione dei voli (Aggiunta,rimozione,modifica,assegnazione gate)
 */
public class MainAppGUI {
    /**
     * Finestra principale dell'applicazione
     */
    private JFrame mainFrame;
    /**
     * Utente autenticato nella sessione
     */
    private Login authenticatedUser;

    /**
     *  --- INTEGRAZIONE DATABASE (DAO & SERVICE) ---
      */
    private final UserService userService = new UserService();
    private final VoloDAO voloDAO = new VoloDAO();
    private final PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();


    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Costruttore dell'applicazione
     * Esegue un controllo preliminare sulla connessione al database.Se non è raggiungibile,
     * termina con un errore critico
     */
    public MainAppGUI() {
        // Controllo preliminare connessione DB
        if (!DatabaseManager.isDatabaseAvailable()) {
            JOptionPane.showMessageDialog(null,
                    "Errore Critico: Impossibile connettersi al Database (Porta 5433).\nControlla che PostgreSQL sia acceso.",
                    "Errore DB",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Inizializza il frame principale
        mainFrame = new JFrame("Sistema Gestione Aeroporto V3 (SQL Edition)");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(500, 400); // Leggermente più grande
        mainFrame.setLocationRelativeTo(null);

        showLoginScreen();
        mainFrame.setVisible(true);
    }

    /**
     * Schermata del Login
     *
     * Costruisce e mostra la schermata di login
     */

    private void showLoginScreen() {
        mainFrame.getContentPane().removeAll();
        mainFrame.setTitle("Login Aeroporto");
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Titolo
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel title = new JLabel("LOGIN");
        title.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(title, gbc);

        // Username
        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(passwordField, gbc);

        // Bottone
        JButton loginButton = new JButton("Accedi");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // --- AGGIUNTA FONDAMENTALE: CONTROLLO CAMPI VUOTI ---
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame,
                        "Inserisci Username e Password!",
                        "Attenzione",
                        JOptionPane.WARNING_MESSAGE);
                return; // <--- Questo STOPPA l'esecuzione, non va oltre!
            }
            // --- CHIAMATA AL DATABASE ---
            authenticatedUser = userService.login(username, password);

            if (authenticatedUser != null) {
                if (authenticatedUser instanceof Admin) {
                    showAdminMenu();
                } else if (authenticatedUser instanceof UtenteGenerico) {
                    showUserMenu((UtenteGenerico) authenticatedUser);
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Credenziali non valide.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainFrame.add(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    /**
     * Schermata Utente Generico
     *
     * Mostra la schermata per l'utente generico
     * Offre opzioni per cercare voli o prenotare
     * @param user
     */
    private void showUserMenu(UtenteGenerico user) {
        mainFrame.getContentPane().removeAll();
        mainFrame.setTitle("Menu Utente - Ciao " + user.getUsername());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));

        JLabel welcomeLabel = new JLabel("Benvenuto, " + user.getUsername()); // Nota: UserDAO deve aver letto il nome!
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(welcomeLabel);

        centerPanel.add(Box.createRigidArea(new Dimension(30, 40)));

        JButton prenotazioneButton = new JButton("Effettua Nuova Prenotazione");
        JButton ricercaButton = new JButton("Ricerca Volo");

        prenotazioneButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        ricercaButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(prenotazioneButton);
        centerPanel.add(Box.createRigidArea(new Dimension(20, 20)));
        centerPanel.add(ricercaButton);

        JButton indietroButton = new JButton("Logout");
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southPanel.add(indietroButton);

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(centerPanel, BorderLayout.CENTER);
        mainFrame.add(southPanel, BorderLayout.SOUTH);

        prenotazioneButton.addActionListener(e -> showScegliVoloForPrenotazione());
        ricercaButton.addActionListener(e -> showRicercaVoloScreen());
        indietroButton.addActionListener(e -> {
            authenticatedUser = null;
            showLoginScreen();
        });

        mainFrame.revalidate();
        mainFrame.repaint();
    }

    /**
     * Mostra un dialog per selezionare un volo dalla lista
     * Filtra i voli per mostrare solo quelli Programmati o in Ritardo
     */

    private void showScegliVoloForPrenotazione() {
        // Legge TUTTI i voli dal DB
        List<Volo> tuttiIVoli = voloDAO.findAll();

        // Filtra in locale quelli prenotabili
        List<Volo> voliDisponibili = tuttiIVoli.stream()
                .filter(v -> v.getStatoVolo().equals(StatoVolo.PROGRAMMATO) || v.getStatoVolo().equals(StatoVolo.IN_RITARDO))
                .collect(Collectors.toList());

        if (voliDisponibili.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Nessun volo disponibile nel Database.", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Volo[] voliArray = voliDisponibili.toArray(new Volo[0]);
        Volo selectedVolo = (Volo) JOptionPane.showInputDialog(
                mainFrame,
                "Scegli il Volo da prenotare:",
                "Seleziona Volo",
                JOptionPane.QUESTION_MESSAGE,
                null,
                voliArray,
                voliArray[0]);

        if (selectedVolo != null) {
            showPrenotazioneScreen(selectedVolo);
        }
    }

    /**
     * Mostra una mapppa con i posti disponibili con una griglia di bottoni
     * quelli in verde sono i posti liberi e quelli in rosso sono quelli occupati
     * @param volo
     */
    private void showPrenotazioneScreen(Volo volo) {
        mainFrame.getContentPane().removeAll();
        mainFrame.setTitle("Prenota Posto: " + volo.getCodice());

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel postiPanel = new JPanel(new GridLayout(5, 10, 5, 5));

        // --- CHIAMATA AL DB: Recupera posti occupati ---
        // Nota: Assicurati di aver aggiunto getPostiOccupati(String codice) in PrenotazioneDAO!
        List<Integer> postiOccupati = prenotazioneDAO.getPostiOccupati(volo.getCodice());

        for (int i = 1; i <= 50; i++) {
            JButton postoButton = new JButton(String.valueOf(i));
            final int postoNumero = i;

            if (postiOccupati.contains(i)) {
                postoButton.setBackground(Color.RED);
                postoButton.setEnabled(false); // Occupato
            } else {
                postoButton.setBackground(Color.GREEN);
                postoButton.addActionListener(e -> confermaPrenotazione(volo, postoNumero));
            }
            postiPanel.add(postoButton);
        }

        panel.add(new JLabel("<html><h3>Scegli un Posto (Verde = Libero)</h3></html>", JLabel.CENTER), BorderLayout.NORTH);
        panel.add(postiPanel, BorderLayout.CENTER);

        JButton indietro = new JButton("Annulla");
        indietro.addActionListener(e -> showUserMenu((UtenteGenerico) authenticatedUser));
        panel.add(indietro, BorderLayout.SOUTH);

        mainFrame.add(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    /**
     * Questa classe salva la prenotazione nel DB però richiede prima il nome,
     * così genera un biglietto univoco e lo salva nel Database
     * @param volo
     * @param postoNumero
     */
    private void confermaPrenotazione(Volo volo, int postoNumero) {
        String nomePasseggero = JOptionPane.showInputDialog(mainFrame, "Inserisci Nome Passeggero:");
        if (nomePasseggero == null || nomePasseggero.trim().isEmpty()) return;

        UtenteGenerico utente = (UtenteGenerico) authenticatedUser;
        String codicePrenotazione = "PRN-" + System.currentTimeMillis();
        String numeroBiglietto = "TIX-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();

        Prenotazione nuovaPrenotazione = new Prenotazione(
                codicePrenotazione,
                postoNumero,
                LocalDateTime.now(),
                nomePasseggero,
                numeroBiglietto,
                utente,
                volo
        );
        nuovaPrenotazione.setStato(StatoPrenotazione.IN_ATTESA);

        try {
            // --- SALVATAGGIO SU DB ---
            prenotazioneDAO.inserisciPrenotazione(nuovaPrenotazione);

            JOptionPane.showMessageDialog(mainFrame,
                    "Prenotazione salvata nel Database!\nCodice: " + codicePrenotazione,
                    "Successo", JOptionPane.INFORMATION_MESSAGE);
            showUserMenu(utente);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, "Errore SQL: " + e.getMessage(), "Errore Database", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Mostra la schermata di ricerca del volo con i vari filtri (tramite partenza,arrivo,codice)
     */
    private void showRicercaVoloScreen() {
        mainFrame.getContentPane().removeAll();
        mainFrame.setTitle("Ricerca Volo");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField partenzaField = new JTextField(15);
        JTextField arrivoField = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Partenza:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(partenzaField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Arrivo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(arrivoField, gbc);

        JButton cercaButton = new JButton("Cerca");
        JButton indietroButton = new JButton("Indietro");

        gbc.gridx = 0; gbc.gridy = 2; panel.add(indietroButton, gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(cercaButton, gbc);

        cercaButton.addActionListener(e -> {
            String part = partenzaField.getText().trim();
            String arr = arrivoField.getText().trim();

            // Logica di ricerca semplificata: Leggiamo tutto e filtriamo in Java
            // (In produzione si farebbe una query SQL specifica "SELECT ... WHERE ...")
            List<Volo> risultati = voloDAO.findAll().stream()
                    .filter(v -> (part.isEmpty() || v.getScaloPartenza().equalsIgnoreCase(part)) &&
                            (arr.isEmpty() || v.getScaloArrivo().equalsIgnoreCase(arr)))
                    .collect(Collectors.toList());

            showRisultatiRicerca(risultati);
        });

        indietroButton.addActionListener(e -> showUserMenu((UtenteGenerico) authenticatedUser));

        mainFrame.add(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    /**
     * Mostra la schermata con i risultati della ricerca richiesta
     * @param risultati
     */
    private void showRisultatiRicerca(List<Volo> risultati) {
        if (risultati.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Nessun volo trovato.", "Risultati", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] columnNames = {"Codice", "Partenza", "Arrivo", "Data/Ora", "Stato"};
        Object[][] data = new Object[risultati.size()][5];

        for (int i = 0; i < risultati.size(); i++) {
            Volo v = risultati.get(i);
            data[i][0] = v.getCodice();
            data[i][1] = v.getScaloPartenza();
            data[i][2] = v.getScaloArrivo();
            data[i][3] = v.getDataOra().format(DT_FORMATTER);
            data[i][4] = v.getStatoVolo();
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        JOptionPane.showMessageDialog(mainFrame, new JScrollPane(table), "Voli Trovati", JOptionPane.PLAIN_MESSAGE);
    }


    /**
     * Schermate Amministratore
     */

    /**
     * Mostra la schermata dell'amministratore con le varie funzioni
     */
    private void showAdminMenu() {
        mainFrame.getContentPane().removeAll();
        mainFrame.setTitle("Pannello Amministratore");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnInserisci = new JButton("Inserisci Nuovo Volo");
        JButton btnModifica = new JButton("Modifica Stato Volo");
        JButton btnCancella = new JButton("Cancella Volo");
        JButton btnLogout = new JButton("Logout");

        gbc.gridx = 0; gbc.gridy = 0; panel.add(btnInserisci, gbc);
        gbc.gridy = 1; panel.add(btnModifica, gbc);
        gbc.gridy = 2; panel.add(btnCancella, gbc);
        gbc.gridy = 3; panel.add(new JSeparator(), gbc);
        gbc.gridy = 4; panel.add(btnLogout, gbc);

        btnInserisci.addActionListener(e -> showInserisciVoloScreen());
        btnModifica.addActionListener(e -> showScegliVoloForModifica());
        btnCancella.addActionListener(e -> showScegliVoloForCancella());
        btnLogout.addActionListener(e -> {
            authenticatedUser = null;
            showLoginScreen();
        });

        mainFrame.add(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    /**
     * Mostra la schermata di Inserimento del volo
     *
     */
    private void showInserisciVoloScreen() {
        mainFrame.getContentPane().removeAll();
        mainFrame.setTitle("Inserisci Volo nel DB");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField codiceField = new JTextField(10);
        JTextField compagniaField = new JTextField(10);
        JTextField partenzaField = new JTextField(10);
        JTextField arrivoField = new JTextField(10);
        JTextField dataOraField = new JTextField(15);
        dataOraField.setText(LocalDateTime.now().plusDays(1).format(DT_FORMATTER));

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Codice (es. AZ123):"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panel.add(codiceField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Compagnia:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panel.add(compagniaField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Partenza:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panel.add(partenzaField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Arrivo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panel.add(arrivoField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Data (gg/MM/aaaa HH:mm):"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; panel.add(dataOraField, gbc);

        JButton conferma = new JButton("Salva nel DB");
        JButton indietro = new JButton("Indietro");

        gbc.gridx = 0; gbc.gridy = 5; panel.add(indietro, gbc);
        gbc.gridx = 1; gbc.gridy = 5; panel.add(conferma, gbc);

        conferma.addActionListener(e -> {
            try {
                LocalDateTime dataOra = LocalDateTime.parse(dataOraField.getText(), DT_FORMATTER);
                Volo v = new Volo(
                        codiceField.getText().toUpperCase(),
                        compagniaField.getText(),
                        dataOra,
                        partenzaField.getText(),
                        arrivoField.getText(),
                        StatoVolo.PROGRAMMATO
                );

                // --- INSERT NEL DB ---
                voloDAO.inserisciVolo(v);
                JOptionPane.showMessageDialog(mainFrame, "Volo salvato correttamente!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                showAdminMenu();

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Formato data errato. Usa: gg/MM/aaaa HH:mm", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Errore Database: " + ex.getMessage(), "Errore SQL", JOptionPane.ERROR_MESSAGE);
            }
        });

        indietro.addActionListener(e -> showAdminMenu());

        mainFrame.add(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    /**
     * Mostra la schermata della modifica,se il volo ha subito dei cambiamenti
     * in questa mostra i voli disponibili
      */
    private void showScegliVoloForModifica() {
        List<Volo> voli = voloDAO.findAll();
        if (voli.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Nessun volo presente nel DB.");
            return;
        }

        Volo[] voliArr = voli.toArray(new Volo[0]);
        Volo selected = (Volo) JOptionPane.showInputDialog(mainFrame, "Scegli volo:", "Modifica", JOptionPane.QUESTION_MESSAGE, null, voliArr, voliArr[0]);

        if (selected != null) showModificaStatoVoloScreen(selected);
    }

    /**
     * Qua invece si aggiorna lo stato di volo
     * @param volo
     */
    private void showModificaStatoVoloScreen(Volo volo) {
        mainFrame.getContentPane().removeAll();
        mainFrame.setTitle("Modifica: " + volo.getCodice());
        JPanel panel = new JPanel(new FlowLayout());

        JComboBox<StatoVolo> combo = new JComboBox<>(StatoVolo.values());
        combo.setSelectedItem(volo.getStatoVolo());

        JButton btnSalva = new JButton("Aggiorna Stato");

        panel.add(new JLabel("Nuovo Stato:"));
        panel.add(combo);
        panel.add(btnSalva);

        btnSalva.addActionListener(e -> {
            volo.setStatoVolo((StatoVolo) combo.getSelectedItem());
            // --- UPDATE SU DB ---
            // Nota: Assicurati di aver aggiunto updateVolo in VoloDAO!
            boolean ok = voloDAO.updateVolo(volo);
            if (ok) {
                JOptionPane.showMessageDialog(mainFrame, "Aggiornato!");
                showAdminMenu();
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Errore Update DB", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainFrame.add(panel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    /**
     * Mostra la schermata per cancellare
     */
    private void showScegliVoloForCancella() {
        List<Volo> voli = voloDAO.findAll();
        if (voli.isEmpty()) return;

        Volo[] voliArr = voli.toArray(new Volo[0]);
        Volo selected = (Volo) JOptionPane.showInputDialog(mainFrame, "Volo da Cancellare:", "Cancella", JOptionPane.WARNING_MESSAGE, null, voliArr, voliArr[0]);

        if (selected != null) {
            int conf = JOptionPane.showConfirmDialog(mainFrame, "Sicuro di voler eliminare " + selected.getCodice() + "?");
            if (conf == JOptionPane.YES_OPTION) {

                boolean ok = voloDAO.deleteVolo(selected.getCodice());
                if (ok) JOptionPane.showMessageDialog(mainFrame, "Volo eliminato.");
                else JOptionPane.showMessageDialog(mainFrame, "Errore Eliminazione (forse ci sono prenotazioni collegate?)");
            }
        }
    }
    /**
     * Punto di ingresso dell'applicazione.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainAppGUI());
    }
}