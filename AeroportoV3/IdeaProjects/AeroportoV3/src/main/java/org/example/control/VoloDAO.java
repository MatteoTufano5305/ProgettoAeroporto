package org.example.control;

import org.example.model.Volo;
import org.example.model.StatoVolo;
import org.example.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VoloDAO {

    /**
     * Inserisce un nuovo volo nel database.
     */
    public void inserisciVolo(Volo volo) throws SQLException {
        String SQL = "INSERT INTO voli (codice, compagnia_aerea, scalo_partenza, scalo_arrivo, data_ora, stato_volo) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false); // Transazione manuale

            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, volo.getCodice());
            pstmt.setString(2, volo.getCompagniaAerea());
            pstmt.setString(3, volo.getScaloPartenza());
            pstmt.setString(4, volo.getScaloArrivo());
            pstmt.setObject(5, volo.getDataOra());
            pstmt.setString(6, volo.getStatoVolo().toString());

            int righeAffette = pstmt.executeUpdate();

            if (righeAffette > 0) {
                conn.commit();
                System.out.println("✅ Volo " + volo.getCodice() + " inserito con successo.");
            } else {
                conn.rollback();
                System.out.println("⚠️ Nessun volo inserito, rollback eseguito.");
            }

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("❌ Errore inserimento! Eseguito ROLLBACK.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e; // Rilancia l'errore al main
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }

    /**
     * Recupera tutti i voli dal database.
     */
    public List<Volo> findAll() {
        String SQL = "SELECT * FROM voli";
        List<Volo> voli = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Volo v = new Volo(
                        rs.getString("codice"),
                        rs.getString("compagnia_aerea"),
                        rs.getObject("data_ora", java.time.LocalDateTime.class),
                        rs.getString("scalo_partenza"),
                        rs.getString("scalo_arrivo"),
                        StatoVolo.valueOf(rs.getString("stato_volo"))
                );
                // Se hai aggiunto la colonna 'gate' o 'id_gate' nel DB, puoi leggerla qui:
                // String gate = rs.getString("gate");
                // if(gate != null) v.setGate(gate);

                voli.add(v);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore lettura voli: " + e.getMessage(), e);
        }
        return voli;
    }

    /**
     * Aggiorna lo STATO di un volo esistente.
     * @param volo L'oggetto volo con i dati aggiornati
     * @return true se l'aggiornamento è riuscito
     */
    public boolean updateVolo(Volo volo) {
        // Aggiorniamo solo lo stato. Se vuoi aggiornare anche il gate, devi avere la colonna nel DB
        String SQL = "UPDATE voli SET stato_volo = ? WHERE codice = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, volo.getStatoVolo().toString());
            pstmt.setString(2, volo.getCodice());

            int righe = pstmt.executeUpdate();
            return righe > 0;

        } catch (SQLException e) {
            System.err.println("Errore update volo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cancella un volo dal database.
     * @param codice Il codice del volo da eliminare (es. AZ123)
     * @return true se la cancellazione è riuscita
     */
    public boolean deleteVolo(String codice) {
        // Attenzione: Se ci sono prenotazioni collegate a questo volo,
        // SQL potrebbe dare errore se non hai messo "ON DELETE CASCADE" nella tabella prenotazioni.
        String SQL = "DELETE FROM voli WHERE codice = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, codice);

            int righe = pstmt.executeUpdate();
            return righe > 0;

        } catch (SQLException e) {
            System.err.println("Errore cancellazione volo: " + e.getMessage());
            return false;
        }
    }
}