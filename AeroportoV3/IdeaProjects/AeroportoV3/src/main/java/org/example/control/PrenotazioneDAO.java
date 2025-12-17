package org.example.control;

import org.example.model.Prenotazione;
import org.example.util.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class PrenotazioneDAO {

    /**
     * Salva una prenotazione nel database.
     */
    public void inserisciPrenotazione(Prenotazione prenotazione) throws SQLException {
        String sql = "INSERT INTO prenotazioni " +
                "(codice_prenotazione, posto_assegnato, data_prenotazione, " +
                "nome_passeggero, numero_biglietto, stato, id_utente, codice_volo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 1. Stringhe e Interi semplici
            pstmt.setString(1, prenotazione.getCodicePrenotazione());
            pstmt.setInt(2, prenotazione.getPostoAssegnato());

            // 3. Data (Conversione da LocalDateTime a Timestamp SQL)
            pstmt.setTimestamp(3, Timestamp.valueOf(prenotazione.getDataPrenotazione()));

            // 4. Altre stringhe
            pstmt.setString(4, prenotazione.getNomePasseggero());
            pstmt.setString(5, prenotazione.getNumeroBiglietto());

            // 6. Enum (Convertiamo lo Stato in Stringa)
            pstmt.setString(6, prenotazione.getStato().toString());

            // 7. CHIAVI ESTERNE
            pstmt.setInt(7, prenotazione.getUtente().getIdUtente());
            // Assicurati che nel model Volo il metodo si chiami getCodice()
            pstmt.setString(8, prenotazione.getVolo().getCodice());

            pstmt.executeUpdate();
            System.out.println("Prenotazione " + prenotazione.getCodicePrenotazione() + " salvata!");

        } catch (SQLException e) {
            System.err.println("Errore salvataggio prenotazione: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Recupera la lista dei numeri di posto già occupati per un determinato volo.
     * Questo serve alla GUI per colorare i sedili di rosso.
     *
     * @param codiceVolo Il codice del volo (es. AZ123)
     * @return Una lista di numeri interi (i posti occupati)
     */
    public List<Integer> getPostiOccupati(String codiceVolo) {
        List<Integer> postiOccupati = new ArrayList<>();
        String sql = "SELECT posto_assegnato FROM prenotazioni WHERE codice_volo = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codiceVolo);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Aggiungiamo il numero del posto alla lista
                    postiOccupati.add(rs.getInt("posto_assegnato"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore recupero posti occupati: " + e.getMessage());
            // In caso di errore restituiamo una lista vuota per non bloccare il programma
        }

        return postiOccupati;
    }
}