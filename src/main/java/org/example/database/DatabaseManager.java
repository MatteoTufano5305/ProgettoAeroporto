package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe di utilità per la gestione della connessione al database PostgreSQL.
 * Serve come punto unico di accesso al database per i DAO del sistema.
 *
 * @author Sistema Aeroporto
 * @version 1.0
 */
public class DatabaseManager {

    /**
     * URL di connessione al database PostgreSQL locale.
     * Nota: La porta è impostata su 5433 (diversa dallo standard 5432).
     */
    private static final String URL = "jdbc:postgresql://localhost:5433/postgres";

    /**
     * Nome utente per l'accesso al server PostgreSQL.
     */
    private static final String USER = "postgres";

    /**
     * Password per l'accesso al server PostgreSQL.
     */
    private static final String PASSWORD = "2345";

    /**
     * Ottiene una connessione attiva al database PostgreSQL.
     *
     * @return una connessione Connection al database
     * @throws SQLException se si verifica un errore durante la connessione
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Verifica se il database è disponibile e raggiungibile.
     * Tenta di stabilire una connessione e la chiude immediatamente.
     *
     * @return true se il database è disponibile, false altrimenti
     */
    public static boolean isDatabaseAvailable() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}