package org.example.control;

import org.example.model.UtenteGenerico;
import org.example.util.DatabaseManager;
// Assumo che Login sia la classe modello. Se si chiama diversamente, cambia qui:
// import org.example.model.Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Questa classe gestisce le operazioni CRUD (in questo caso specifico, la lettura/autenticazione)
 * sulla tabella 'utenti' del database.
 * Implementa il pattern DAO per separare la logica di accesso ai dati dalla logica di business.
 *
 */
public class UserDAO {
    /**
     * Questo metodo
     * -Cerca un utente nel DB tramite nome e password
     * -Esegue una query sicura usando {@link PreparedStatement} per prevenire
     * attacchi di tipo SQL Injection
     * -Legge il flag 'is_admin' dal database.
     * Se true: restituisce un oggetto {@code Admin}.</li>
     * Se false: restituisce un oggetto {@code UtenteGenerico}.</li>
     * @param username
     * @param password
     * @return
     */
    public Login findByUsernameAndPassword(String username, String password) {
        String query = "SELECT * FROM utenti WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id_utente");
                    String user = rs.getString("username");
                    String pass = rs.getString("password");
                    String nome = rs.getString("nome"); // <--- Leggiamo il nome!
                    boolean isAdmin = rs.getBoolean("is_admin");

                    if (isAdmin) {
                        // Se l'Admin non ha il campo 'nome', passiamo solo user/pass
                        return new Admin(id, user, pass);
                    } else {
                        // Se è un utente generico, passiamo anche il nome letto dal DB
                        return new UtenteGenerico(id, user, pass, nome);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore Login: " + e.getMessage());
        }
        return null;
    }
}