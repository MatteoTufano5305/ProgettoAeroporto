package org.example.DAO;

import org.example.control.Admin;
import org.example.control.Login;
import org.example.model.*;
import org.example.util.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AeroportoDAO {

    private static final Logger LOGGER = Logger.getLogger(AeroportoDAO.class.getName());


    private static final String SQL_VOLO_INSERT = "INSERT INTO voli (codice, compagnia_aerea, scalo_partenza, scalo_arrivo, data_volo, ora_volo, stato_volo) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_VOLO_FIND_ALL = "SELECT * FROM voli";
    private static final String SQL_VOLO_UPDATE_GATE = "UPDATE voli SET gate = ? WHERE codice = ?";
    private static final String SQL_VOLO_CHECK_CONFLICT = "SELECT codice, ora_volo FROM voli WHERE gate = ? AND data_volo = ? AND codice != ? AND ora_volo BETWEEN ? AND ?";
    private static final String SQL_VOLO_FIND_BY_ID = "SELECT * FROM voli WHERE codice = ?";
    private static final String SQL_VOLO_SEARCH = "SELECT * FROM voli WHERE scalo_partenza = ? AND (data_volo > ? OR (data_volo = ? AND ora_volo >= ?))";
    private static final String SQL_VOLO_FIND_AFTER = "SELECT * FROM voli WHERE (data_volo > ? OR (data_volo = ? AND ora_volo >= ?))";
    private static final String SQL_VOLO_UPDATE_STATUS = "UPDATE voli SET stato_volo = ? WHERE codice = ?";
    private static final String SQL_VOLO_DELETE = "DELETE FROM voli WHERE codice = ?";

    private static final String SQL_USER_REGISTER = "INSERT INTO utenti (username, password, nome, is_admin) VALUES (?, ?, ?, ?)";
    private static final String SQL_USER_LOGIN = "SELECT * FROM utenti WHERE username = ? AND password = ?";

    private static final String SQL_PRENOTAZIONE_INSERT = "INSERT INTO prenotazioni (codice_prenotazione, posto_assegnato, data_prenotazione, nome_passeggero, numero_biglietto, stato, id_utente, codice_volo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_PRENOTAZIONE_GET_POSTI = "SELECT posto_assegnato FROM prenotazioni WHERE codice_volo = ?";


    public void inserisciVolo(Volo volo) throws SQLException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_VOLO_INSERT)) {

            pstmt.setString(1, volo.getCodice());
            pstmt.setString(2, volo.getCompagniaAerea());
            pstmt.setString(3, volo.getScaloPartenza());
            pstmt.setString(4, volo.getScaloArrivo());
            pstmt.setDate(5, Date.valueOf(volo.getData()));
            pstmt.setTime(6, Time.valueOf(volo.getOra()));
            pstmt.setString(7, volo.getStatoVolo().toString());

            pstmt.executeUpdate();
            LOGGER.info(() -> "Volo " + volo.getCodice() + " inserito con successo.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore inserimento volo", e);
            throw e; // Rilancia al Service
        }
    }


    public List<Volo> findAllVoli() throws SQLException {
        List<Volo> voli = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_VOLO_FIND_ALL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                voli.add(mapRowToVolo(rs));
            }
        }

        return voli;
    }

    public Volo findVoloByCodice(String codice) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_VOLO_FIND_BY_ID)) {

            pstmt.setString(1, codice);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToVolo(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore ricerca volo per ID", e);
        }
        return null;
    }

    public boolean updateGate(String codiceVolo, String nuovoGate) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_VOLO_UPDATE_GATE)) {

            pstmt.setString(1, nuovoGate);
            pstmt.setString(2, codiceVolo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore assegnazione gate", e);
            return false;
        }
    }

    public boolean isGateOccupato(String gate, LocalDate data, LocalTime ora, String codiceVoloEscluso) {
        LocalTime oraInizio = ora.minusMinutes(60);
        LocalTime oraFine = ora.plusMinutes(60);

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_VOLO_CHECK_CONFLICT)) {

            pstmt.setString(1, gate);
            pstmt.setDate(2, Date.valueOf(data));
            pstmt.setString(3, codiceVoloEscluso);
            pstmt.setTime(4, Time.valueOf(oraInizio));
            pstmt.setTime(5, Time.valueOf(oraFine));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    LOGGER.info("Conflitto gate rilevato.");
                    return true;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore controllo conflitti gate", e);
            throw new RuntimeException(e);
        }
        return false;
    }

    public List<Volo> cercaVoli(String scaloPartenza, LocalDate dataInput, LocalTime oraInput) {
        List<Volo> risultati = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_VOLO_SEARCH)) {

            pstmt.setString(1, scaloPartenza);
            pstmt.setDate(2, Date.valueOf(dataInput));
            pstmt.setDate(3, Date.valueOf(dataInput));
            pstmt.setTime(4, Time.valueOf(oraInput));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    risultati.add(mapRowToVolo(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore ricerca voli", e);
        }
        return risultati;
    }

    public List<Volo> findVoliByDataOraAfter(LocalDate dataInput, LocalTime oraInput) {
        List<Volo> risultati = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_VOLO_FIND_AFTER)) {

            pstmt.setDate(1, Date.valueOf(dataInput));
            pstmt.setDate(2, Date.valueOf(dataInput));
            pstmt.setTime(3, Time.valueOf(oraInput));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    risultati.add(mapRowToVolo(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore ricerca voli after", e);
        }
        return risultati;
    }

    public boolean updateStatoVolo(Volo volo) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_VOLO_UPDATE_STATUS)) {

            pstmt.setString(1, volo.getStatoVolo().toString());
            pstmt.setString(2, volo.getCodice());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore update stato volo", e);
            return false;
        }
    }

    public boolean deleteVolo(String codice) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_VOLO_DELETE)) {
            pstmt.setString(1, codice);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore cancellazione volo", e);
            return false;
        }
    }

    public List<Volo> ricercaDinamicaVoli(String partenza, String arrivo, LocalDate data, LocalTime ora) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM voli WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (partenza != null && !partenza.trim().isEmpty()) {
            sqlBuilder.append(" AND scalo_partenza LIKE ?");
            params.add("%" + partenza + "%");
        }
        if (arrivo != null && !arrivo.trim().isEmpty()) {
            sqlBuilder.append(" AND scalo_arrivo LIKE ?");
            params.add("%" + arrivo + "%");
        }
        if (data != null) {
            sqlBuilder.append(" AND data_volo = ?");
            params.add(Date.valueOf(data));
        }
        if (ora != null) {
            sqlBuilder.append(" AND ora_volo >= ?");
            params.add(Time.valueOf(ora));
        }

        List<Volo> risultati = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    risultati.add(mapRowToVolo(rs));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore ricerca dinamica", e);
        }
        return risultati;
    }

    // --- METODI UTENTI ---

    // Rimosso 'static'
    public static boolean registerUser(String username, String password, String nome, boolean isAdmin) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_USER_REGISTER)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, nome);
            pstmt.setBoolean(4, isAdmin);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                LOGGER.info(() -> "Utente registrato: " + username);
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore registrazione utente: " + username, e);
        }
        return false;
    }

    public Login findByUsernameAndPassword(String username, String password) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_USER_LOGIN)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore login utente: " + username, e);
        }
        return null;
    }

    // --- METODI PRENOTAZIONI ---

    public void inserisciPrenotazione(Prenotazione prenotazione) throws SQLException {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_PRENOTAZIONE_INSERT)) {

            pstmt.setString(1, prenotazione.getCodicePrenotazione());
            pstmt.setInt(2, prenotazione.getPostoAssegnato());
            pstmt.setTimestamp(3, Timestamp.valueOf(prenotazione.getDataPrenotazione()));
            pstmt.setString(4, prenotazione.getNomePasseggero());
            pstmt.setString(5, prenotazione.getNumeroBiglietto());
            pstmt.setString(6, prenotazione.getStato().toString());
            pstmt.setInt(7, prenotazione.getUtente().getIdUtente());
            pstmt.setString(8, prenotazione.getVolo().getCodice());

            pstmt.executeUpdate();
            LOGGER.info(() -> "Prenotazione " + prenotazione.getCodicePrenotazione() + " salvata con successo.");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore salvataggio prenotazione", e);
            throw e;
        }
    }

    public List<Integer> getPostiOccupati(String codiceVolo) {
        List<Integer> postiOccupati = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_PRENOTAZIONE_GET_POSTI)) {

            pstmt.setString(1, codiceVolo);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    postiOccupati.add(rs.getInt("posto_assegnato"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore recupero posti occupati per volo: " + codiceVolo, e);
        }
        return postiOccupati;
    }

    // --- MAPPERS ---

    private Volo mapRowToVolo(ResultSet rs) throws SQLException {
        Volo v = new Volo(
                rs.getString("codice"),
                rs.getString("compagnia_aerea"),
                rs.getDate("data_volo").toLocalDate(),
                rs.getTime("ora_volo").toLocalTime(),
                rs.getString("scalo_partenza"),
                rs.getString("scalo_arrivo"),
                StatoVolo.valueOf(rs.getString("stato_volo"))
        );

        String gateString = rs.getString("gate");
        if (gateString != null && !gateString.isEmpty()) {
            v.setGateAssegnato(new Gate(gateString));
        }
        return v;
    }

    private Login mapRowToUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_utente");
        String user = rs.getString("username");
        String pass = rs.getString("password");
        String nome = rs.getString("nome");
        boolean isAdmin = rs.getBoolean("is_admin");

        if (isAdmin) {
            return new Admin(id, user, pass);
        } else {
            return new UtenteGenerico(id, user, pass, nome);
        }
    }
}