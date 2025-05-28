package org.example.control;
import javax.swing.*;

import org.example.model.Gate;
import org.example.model.Prenotazione;
import org.example.model.StatoVolo;
import org.example.model.UtenteGenerico;
import org.example.model.Volo;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MainAppGUI {

    private JFrame loginFrame;
    private Login authenticatedUser;

    private List<Login> users = new ArrayList<>();
    private List<Volo> flights = new ArrayList<>();
    private List<Prenotazione> reservations = new ArrayList<>();
    private List<Gate> gates = new ArrayList<>();

    public MainAppGUI() {
        initializeDummyData();
        showLoginScreen();
    }

    private void initializeDummyData() {
        gates.add(new Gate("A1", "Libero"));
        gates.add(new Gate("A2", "Occupato"));
        gates.add(new Gate("B1", "Libero"));

        users.add(new Admin(1, "admin", "adminpass"));
        users.add(new UtenteGenerico(2, "user1", "userpass", "Mario Rossi"));
        users.add(new UtenteGenerico(3, "user2", "pass123", "Luigi Bianchi"));

        Volo volo1 = new Volo("AZ123", "Alitalia", LocalDateTime.of(2025, 6, 1, 10, 0),
                "Roma", "Milano", StatoVolo.PROGRAMMATO);
        volo1.setGateAssegnato(gates.get(0));
        gates.get(0).setStato("Occupato");
        flights.add(volo1);

        Volo volo2 = new Volo("FR456", "Ryanair", LocalDateTime.of(2025, 6, 1, 14, 30),
                "Milano", "Londra", StatoVolo.PROGRAMMATO);
        flights.add(volo2);

        Volo volo3 = new Volo("LH789", "Lufthansa", LocalDateTime.of(2025, 6, 2, 9, 0),
                "Francoforte", "Parigi", StatoVolo.RITARDO);
        flights.add(volo3);

        reservations.add(new Prenotazione("P123", 15, LocalDateTime.now(), "Mario Rossi",
                "TKT001", (UtenteGenerico) users.get(1), volo1));
        volo1.bookSeat(15);
        volo1.bookSeat(16); 
        volo1.bookSeat(17);
    }

    private Login authenticate(String username, String password) {
        for (Login user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private Volo findFlightByCode(String code) {
        for (Volo flight : flights) {
            if (flight.getCodice().equalsIgnoreCase(code)) {
                return flight;
            }
        }
        return null;
    }

    private List<Volo> searchFlights(String partenza, String arrivo, LocalDateTime dataOra) {
        List<Volo> result = new ArrayList<>();
        for (Volo flight : flights) {
            boolean matchPartenza = (partenza == null || partenza.isEmpty() || flight.getScaloPartenza().equalsIgnoreCase(partenza));
            boolean matchArrivo = (arrivo == null || arrivo.isEmpty() || flight.getScaloArrivo().equalsIgnoreCase(arrivo));
            boolean matchDate = (dataOra == null || (flight.getDataOra().getYear() == dataOra.getYear() &&
                    flight.getDataOra().getMonth() == dataOra.getMonth() &&
                    flight.getDataOra().getDayOfMonth() == dataOra.getDayOfMonth()));
            if (matchPartenza && matchArrivo && matchDate) {
                result.add(flight);
            }
        }
        return result;
    }

    private Gate findGateByNumber(String gateNumber) {
        for (Gate gate : gates) {
            if (gate.getNumero().equalsIgnoreCase(gateNumber)) {
                return gate;
            }
        }
        return null;
    }


    private void showLoginScreen() {
        loginFrame = new JFrame("Login");
        loginFrame.setSize(300, 200);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        loginFrame.add(panel);
        placeLoginComponents(panel);

        loginFrame.setVisible(true);
    }

    private void placeLoginComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Conferma");
        loginButton.setBounds(100, 90, 100, 25);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());

                authenticatedUser = authenticate(username, password);

                if (authenticatedUser != null) {
                    loginFrame.dispose();
                    if (authenticatedUser instanceof Admin) {
                        showAdminMenu();
                    } else {
                        showUserMenu();
                    }
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Credenziali non valide", "Errore di Login", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void showUserMenu() {
        JFrame userMenuFrame = new JFrame("Benvenuto " + authenticatedUser.getUsername());
        userMenuFrame.setSize(400, 250);
        userMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        userMenuFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        userMenuFrame.add(panel);

        JButton effettuaPrenotazioneButton = new JButton("Effettua Prenotazione");
        effettuaPrenotazioneButton.setBounds(100, 50, 200, 30);
        panel.add(effettuaPrenotazioneButton);

        JButton ricercaVoloButton = new JButton("Ricerca Volo");
        ricercaVoloButton.setBounds(100, 100, 200, 30);
        panel.add(ricercaVoloButton);

        JButton indietroButton = new JButton("Indietro");
        indietroButton.setBounds(10, 180, 100, 25);
        panel.add(indietroButton);

        effettuaPrenotazioneButton.addActionListener(e -> showPrenotazioneScreen(userMenuFrame));
        ricercaVoloButton.addActionListener(e -> showRicercaVoloScreen(userMenuFrame));
        indietroButton.addActionListener(e -> {
            userMenuFrame.dispose();
            showLoginScreen();
        });

        userMenuFrame.setVisible(true);
    }

    private void showAdminMenu() {
        JFrame adminMenuFrame = new JFrame("Benvenuto Admin");
        adminMenuFrame.setSize(400, 300);
        adminMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminMenuFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));
        adminMenuFrame.add(panel);

        JButton inserisciVoloButton = new JButton("Inserisci Volo");
        JButton modificaVoloButton = new JButton("Modifica Volo");
        JButton cancellaVoloButton = new JButton("Cancella Volo");
        JButton assegnaGateButton = new JButton("Assegna Gate");
        JButton indietroButton = new JButton("Indietro");

        panel.add(inserisciVoloButton);
        panel.add(modificaVoloButton);
        panel.add(cancellaVoloButton);
        panel.add(assegnaGateButton);
        panel.add(indietroButton);

        inserisciVoloButton.addActionListener(e -> showInserisciVoloScreen(adminMenuFrame));
        modificaVoloButton.addActionListener(e -> showModificaVoloSelectionScreen(adminMenuFrame));
        cancellaVoloButton.addActionListener(e -> showCancellaVoloSelectionScreen(adminMenuFrame));
        assegnaGateButton.addActionListener(e -> showAssegnaGateScreen(adminMenuFrame));
        indietroButton.addActionListener(e -> {
            adminMenuFrame.dispose();
            showLoginScreen();
        });

        adminMenuFrame.setVisible(true);
    }



    private void showPrenotazioneScreen(JFrame parentFrame) {
        JFrame prenotazioneFrame = new JFrame("Effettua Prenotazione");
        prenotazioneFrame.setSize(700, 600);
        prenotazioneFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        prenotazioneFrame.setLocationRelativeTo(parentFrame);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        prenotazioneFrame.add(mainPanel);


        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        mainPanel.add(selectionPanel, BorderLayout.NORTH);

        if (flights.isEmpty()) {
            JOptionPane.showMessageDialog(prenotazioneFrame, "Nessun volo disponibile per la prenotazione.", "Errore", JOptionPane.INFORMATION_MESSAGE);
            prenotazioneFrame.dispose();
            return;
        }

        JLabel voloLabel = new JLabel("Scegli Volo:");
        JComboBox<Volo> voloComboBox = new JComboBox<>(flights.toArray(new Volo[0]));
        selectionPanel.add(voloLabel);
        selectionPanel.add(voloComboBox);


        JPanel seatMapPanel = new JPanel();
        seatMapPanel.setLayout(new GridLayout(10, 10, 2, 2));
        JScrollPane scrollPane = new JScrollPane(seatMapPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);


        JPanel bookingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        mainPanel.add(bookingPanel, BorderLayout.SOUTH);

        JLabel selectedSeatLabel = new JLabel("Posto Selezionato: Nessuno");
        JTextField postoText = new JTextField(5);
        postoText.setEditable(false);
        bookingPanel.add(selectedSeatLabel);
        bookingPanel.add(postoText);

        JButton confermaButton = new JButton("Conferma Prenotazione");
        JButton indietroButton = new JButton("Indietro");
        bookingPanel.add(confermaButton);
        bookingPanel.add(indietroButton);


        voloComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Volo selectedVolo = (Volo) voloComboBox.getSelectedItem();
                updateSeatMap(selectedVolo, seatMapPanel, selectedSeatLabel, postoText);
            }
        });


        Volo initialVolo = (Volo) voloComboBox.getSelectedItem();
        if (initialVolo != null) {
            updateSeatMap(initialVolo, seatMapPanel, selectedSeatLabel, postoText);
        }

        confermaButton.addActionListener(e -> {
            Volo selectedVolo = (Volo) voloComboBox.getSelectedItem();
            if (selectedVolo == null) {
                JOptionPane.showMessageDialog(prenotazioneFrame, "Seleziona un volo.", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int posto;
            try {
                posto = Integer.parseInt(postoText.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(prenotazioneFrame, "Seleziona un posto valido cliccando sulla mappa.", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (selectedVolo.isSeatAvailable(posto)) {
                if (selectedVolo.bookSeat(posto)) {
                    String prenotazioneCode = "P" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
                    String bigliettoNum = "TKT" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
                    UtenteGenerico currentUser = (UtenteGenerico) authenticatedUser;
                    Prenotazione newReservation = new Prenotazione(prenotazioneCode, posto, LocalDateTime.now(),
                            currentUser.getNome(), bigliettoNum, currentUser, selectedVolo);
                    reservations.add(newReservation);
                    JOptionPane.showMessageDialog(prenotazioneFrame, "Prenotazione Effettuata!\nCodice: " + prenotazioneCode, "Successo", JOptionPane.INFORMATION_MESSAGE);
                    updateSeatMap(selectedVolo, seatMapPanel, selectedSeatLabel, postoText);

                } else {
                    JOptionPane.showMessageDialog(prenotazioneFrame, "Impossibile prenotare il posto.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(prenotazioneFrame, "Posto già occupato o non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        indietroButton.addActionListener(e -> prenotazioneFrame.dispose());
        prenotazioneFrame.setVisible(true);
    }


    private void updateSeatMap(Volo volo, JPanel seatMapPanel, JLabel selectedSeatLabel, JTextField postoText) {
        seatMapPanel.removeAll();
        selectedSeatLabel.setText("Posto Selezionato: Nessuno");
        postoText.setText("");

        int totalSeats = 100;
        for (int i = 1; i <= totalSeats; i++) {
            JButton seatButton = new JButton(String.valueOf(i));
            seatButton.setPreferredSize(new Dimension(50, 50));
            seatButton.setFont(new Font("Arial", Font.PLAIN, 10));

            if (volo.isSeatAvailable(i)) {
                seatButton.setBackground(new Color(144, 238, 144));
                seatButton.setForeground(Color.BLACK);
                seatButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int selectedSeat = Integer.parseInt(seatButton.getText());
                        selectedSeatLabel.setText("Posto Selezionato: " + selectedSeat);
                        postoText.setText(String.valueOf(selectedSeat));
                    }
                });
            } else {
                seatButton.setBackground(new Color(255, 99, 71));
                seatButton.setForeground(Color.WHITE);
                seatButton.setEnabled(false);
            }
            seatMapPanel.add(seatButton);
        }
        seatMapPanel.revalidate();
        seatMapPanel.repaint();
    }


    private void showRicercaVoloScreen(JFrame parentFrame) {

    }

    private void showInserisciVoloScreen(JFrame parentFrame) {

    }

    private void showModificaVoloSelectionScreen(JFrame parentFrame) {

    }

    private void showModificaVoloScreen(Volo voloToModify, JFrame parentFrame) {

    }

    private void showCancellaVoloSelectionScreen(JFrame parentFrame) {

    }

    private void showAssegnaGateScreen(JFrame parentFrame) {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainAppGUI();
            }
        });
    }
}