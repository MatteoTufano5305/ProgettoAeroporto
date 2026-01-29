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

/**
 * Data Access Object (DAO)
 * Questa classe gestisce tutte le operazioni CRUD (Create, Read, Update, Delete)
 * relative a {@link Volo}, Utenti e {@link Prenotazione}, interagendo direttamente
 * con il database relazionale tramite JDBC.
 */
public class AeroportoDAO {

    private static final Logger LOGGER = Logger.getLogger(AeroportoDAO.class.getName());


    private static final String SQL_VOLO_INSERT = "INSERT INTO voli (codice, compagnia_aerea, scalo_partenza, scalo_arrivo, data_volo, ora_volo, stato_volo) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_VOLO_FIND_ALL = "SELECT * FROM voli";
    private static final String SQL_VOLO_UPDATE_GATE = "UPDATE voli SET gate = ? WHERE codice = ?";
    private static final String SQL_VOLO_CHECK_CONFLICT = "SELECT codice, ora_volo FROM voli WHERE gate = ? AND data_volo = ? AND codice != ? AND ora_volo BETWEEN ? AND ?";
    private static final String SQL_VOLO_FIND_BY_ID = "SELECT * FROM voli WHERE codice = ?";
    private static final String SQL_VOLO_SEARCH = "SELECT * FROM voli WHERE scalo_partenza = ? AND (data_volo > ? OR (data_volo = ? AND ora_volo >= ?))";
    private static final String SQL_VOLO_UPDATE_STATUS = "UPDATE voli SET stato_volo = ? WHERE codice = ?";
    private static final String SQL_VOLO_DELETE = "DELETE FROM voli WHERE codice = ?";

    private static final String SQL_USER_REGISTER = "INSERT INTO utenti (username, password, nome, is_admin) VALUES (?, ?, ?, ?)";
    private static final String SQL_USER_LOGIN = "SELECT * FROM utenti WHERE username = ? AND password = ?";

    private static final String SQL_PRENOTAZIONE_INSERT = "INSERT INTO prenotazioni (codice_prenotazione, posto_assegnato, data_prenotazione, nome_passeggero, numero_biglietto, stato, id_utente, codice_volo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_PRENOTAZIONE_GET_POSTI = "SELECT posto_assegnato FROM prenotazioni WHERE codice_volo = ?";
    private static final String SQL_PRENOTAZIONI_BY_USER = "SELECT * FROM prenotazioni p " + "JOIN voli v ON p.codice_volo = v.codice " + "WHERE p.id_utente = ?";
    private static final String SQL_PRENOTAZIONE_DELETE = "DELETE FROM prenotazioni WHERE codice_prenotazione = ?";
    /**
     * Inserisce un nuovo volo nel database.
     *
     * @param volo L'oggetto {@link Volo} contenente i dati da persistere.
     * @throws SQLException Se si verifica un errore durante l'inserimento (es. violazione vincolo unique sul codice).
     */
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
            LOGGER.log(Level.SEVERE, "Errore inserimento volo: " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Recupera l'elenco completo di tutti i voli presenti nel sistema.
     *
     * @return Una {@link List} di oggetti {@link Volo}. Ritorna una lista vuota se non ci sono voli.
     * @throws SQLException Se si verifica un errore di connessione o esecuzione query.
     */
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

    /**
     * Cerca un volo specifico tramite il suo codice ID.
     *
     * @param codice Il codice alfanumerico del volo (es. "AZ123").
     * @return L'oggetto {@link Volo} se trovato, altrimenti {@code null}.
     */
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
            LOGGER.log(Level.SEVERE, "Errore ricerca volo per ID: " + codice, e);
        }
        return null;
    }

    /**
     * Aggiorna il gate assegnato ad uno specifico volo.
     *
     * @param codiceVolo Il codice del volo da aggiornare.
     * @param nuovoGate  Il nome/codice del nuovo gate (es. "A1").
     * @return {@code true} se l'aggiornamento ha avuto successo, {@code false} altrimenti.
     */
    public boolean updateGate(String codiceVolo, String nuovoGate) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_VOLO_UPDATE_GATE)) {

            pstmt.setString(1, nuovoGate);
            pstmt.setString(2, codiceVolo);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore assegnazione gate per volo: " + codiceVolo, e);
            return false;
        }
    }

    /**
     * Verifica se un Gate è occupato in una specifica fascia oraria.
     *
     * Il controllo viene effettuato considerando un buffer di +/- 60 minuti
     * rispetto all'orario indicato.
     *
     * @param gate              Il gate da controllare.
     * @param data              La data del volo.
     * @param ora               L'orario del volo.
     * @param codiceVoloEscluso Il codice del volo attuale (per evitare conflitti con se stesso durante update).
     * @return {@code true} se il gate è già occupato da un altro volo nel range orario, {@code false} se libero.
     */
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
                    LOGGER.info("Conflitto gate rilevato per " + gate);
                    return true;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore controllo conflitti gate", e);
            throw new RuntimeException("Errore database durante controllo gate", e);
        }
        return false;
    }

    /**
     * Aggiorna lo stato corrente del volo (es. IN_ORARIO, CANCELLATO, IN_RITARDO).
     *
     * @param volo L'oggetto Volo contenente il codice e il nuovo stato aggiornato.
     * @return {@code true} se l'aggiornamento è riuscito, {@code false} altrimenti.
     */
    public boolean updateStatoVolo(Volo volo) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_VOLO_UPDATE_STATUS)) {

            pstmt.setString(1, volo.getStatoVolo().toString());
            pstmt.setString(2, volo.getCodice());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore update stato volo: " + volo.getCodice(), e);
            return false;
        }
    }

    /**
     * Rimuove un volo dal database.
     *
     * @param codice Il codice identificativo del volo da eliminare.
     * @return {@code true} se l'eliminazione è avvenuta, {@code false} se il volo non esisteva o errore DB.
     */
    public boolean deleteVolo(String codice) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_VOLO_DELETE)) {
            pstmt.setString(1, codice);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore cancellazione volo: " + codice, e);
            return false;
        }
    }

    /**
     * Esegue una ricerca dinamica dei voli basata su filtri opzionali.
     * I parametri passati come {@code null} o stringhe vuote vengono ignorati nella query.
     * I filtri applicati lavorano in logica AND.
     *
     * @param partenza La città o codice dello scalo di partenza (match parziale con LIKE).
     * @param arrivo   La città o codice dello scalo di arrivo (match parziale con LIKE).
     * @param data     La data esatta del volo.
     * @param ora      L'orario minimo del volo (trova voli da quell'ora in poi).
     * @return Una lista di voli che soddisfano i criteri.
     */
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
            LOGGER.log(Level.SEVERE, "Errore ricerca dinamica voli", e);
        }
        return risultati;
    }

    /**
     * Registra un nuovo utente nel sistema.
     * @param username L'username scelto per il login.
     * @param password La password in chiaro (Nota: considerare hashing per produzione).
     * @param nome     Il nome completo dell'utente.
     * @param isAdmin  {@code true} se l'utente deve avere privilegi di amministratore.
     * @return {@code true} se la registrazione ha successo, {@code false} altrimenti.
     */
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

    /**
     * Autentica un utente verificando username e password.
     *
     * @param username L'username fornito.
     * @param password La password fornita.
     * @return Un oggetto {@link Login} (sottoclasse {@link Admin} o {@link UtenteGenerico}) se le credenziali sono corrette,
     * altrimenti {@code null}.
     */
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

    /**
     * Salva una nuova prenotazione nel database.
     *
     * @param prenotazione L'oggetto {@link Prenotazione} completo.
     * @throws SQLException Se si verifica un errore SQL (es. posto già occupato non gestito a livello logico).
     */
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

    /**
     * Restituisce la lista dei numeri di posto già prenotati per un determinato volo.
     *
     * @param codiceVolo Il codice del volo da controllare.
     * @return Una {@link List} di interi rappresentanti i posti occupati.
     */
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



    /**
     * Recupera lo storico delle prenotazioni di un utente specifico.
     * Esegue una JOIN con la tabella voli per avere i dettagli di partenza/arrivo.
     */
    public List<Prenotazione> getPrenotazioniByUtente(int idUtente) {
        String sql = "SELECT p.*, v.* FROM prenotazioni p " +
                "JOIN voli v ON p.codice_volo = v.codice " +
                "WHERE p.id_utente = ?";

        List<Prenotazione> lista = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUtente);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Volo volo = new Volo(
                            rs.getString("codice_volo"),
                            rs.getString("compagnia_aerea"),
                            rs.getDate("data_volo").toLocalDate(),
                            rs.getTime("ora_volo").toLocalTime(),
                            rs.getString("scalo_partenza"),
                            rs.getString("scalo_arrivo"),
                            StatoVolo.valueOf(rs.getString("stato_volo"))
                    );


                    Prenotazione p = new Prenotazione(
                            rs.getString("codice_prenotazione"),
                            rs.getInt("posto_assegnato"),
                            rs.getTimestamp("data_prenotazione").toLocalDateTime(),
                            rs.getString("nome_passeggero"),
                            rs.getString("numero_biglietto"),
                            null,
                            volo
                    );
                    p.setStato(org.example.model.StatoPrenotazione.valueOf(rs.getString("stato")));

                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore recupero prenotazioni utente " + idUtente, e);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Errore conversione ENUM (StatoVolo o StatoPrenotazione)", e);
        }

        return lista;
    }
    /**
     * Elimina una prenotazione dal database dato il suo codice.
     * @param codicePrenotazione Il codice univoco della prenotazione.
     * @return true se l'eliminazione è avvenuta, false altrimenti.
     */
    public boolean deletePrenotazione(String codicePrenotazione) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_PRENOTAZIONE_DELETE)) {

            pstmt.setString(1, codicePrenotazione);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore cancellazione prenotazione: " + codicePrenotazione, e);
            return false;
        }
    }

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