package org.example.control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe per la gestione della connessione al Database PostgreSQL
 * Serve come punto unico di accesso al database per i DAO del sistema
 */
public class DatabaseManager {

    /**
     * URL di connessione JDBC per il database PostgreSQL (Host,porta,nome del DB)
     */
    private static final String URL = "jdbc:postgresql://localhost:5433/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "2345";

    /**
     * Stabilisce e restituisce una nuova connessione al Database
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static boolean isDatabaseAvailable() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

}