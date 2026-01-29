package org.example.control;

/**
 * Classe che rappresenta l'entità base del Login
 *
 * Questa classe definisce gli attributi comuni a tutti gli utenti e impone un contratto
 * per la verifica dei privilegi tramite un modello astratto
 */

public abstract class Login {
    protected int idUtente;
    protected String username;
    protected String password;
    protected boolean verificaAdmin;

    /**
     * Costruttore per inizializzare le credenziali
     * @param idUtente
     * @param username
     * @param password
     */
    public Login(int idUtente, String username, String password) {
        this.idUtente = idUtente;
        this.username = username;
        this.password = password;
        this.verificaAdmin = false;
    }

    /**
     * I Get restituiscono i valori
     * @return
     */
    public int getIdUtente() {
        return idUtente;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Metodo astratto per verificare i privilegi dell'utente
     * @return
     */

    public abstract boolean getVerificaAdmin();
}