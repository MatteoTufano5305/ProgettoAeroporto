package org.example.model;
import org.example.control.Login;

/**
 Rappresenta un cliente e serve per gestire le operazioni specifiche (Ricerca Voli e Prenotazione)
 */
public class UtenteGenerico extends Login {

    /**
     * Nome reale dell'utente
     */
    private String nome;

    /**
     * Costruttore per impostare un nuovo cliente
     * E imposta forzatamente i privilegi di amministratore a false
     * @param idUtente
     * @param username
     * @param password
     * @param nome
     */
    public UtenteGenerico(int idUtente, String username, String password, String nome) {
        super(idUtente, username, password);
        this.nome = nome;
        this.verificaAdmin = false;
    }

    /**
     * Restituisce il nome anagrafico dell'utente
     * @return
     */

    public String getNome() {
        return nome;
    }

    /**
     * Simula l'azione di prenotazione da parte di un utente
     */
    public void effettuaPrenotazione() {
        System.out.println("Utente " + nome + " sta effettuando una prenotazione.");
    }

    /**
     * Simula il processo di ricerca del volo
     */
    public void ricercaVolo() {
        System.out.println("Utente " + nome + " sta ricercando un volo.");
    }

    /**
     * Verifica se l'utente ha i permessi da amministratore
     * @return
     */
    @Override
    public boolean getVerificaAdmin() {
        return verificaAdmin;
    }

    @Override
    public String toString() {
        return username + " (Utente Generico)";
    }
}