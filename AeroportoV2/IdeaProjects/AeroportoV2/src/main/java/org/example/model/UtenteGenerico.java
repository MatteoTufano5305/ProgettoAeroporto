package org.example.model;
import org.example.control.Login;

public class UtenteGenerico extends Login {
    private String nome;

    public UtenteGenerico(int idUtente, String username, String password, String nome) {
        super(idUtente, username, password);
        this.nome = nome;
        this.verificaAdmin = false;
    }

    public String getNome() {
        return nome;
    }

    public void effettuaPrenotazione() {
        System.out.println("Utente " + nome + " sta effettuando una prenotazione.");
    }

    public void ricercaVolo() {
        System.out.println("Utente " + nome + " sta ricercando un volo.");
    }

    @Override
    public boolean getVerificaAdmin() {
        return verificaAdmin;
    }

    @Override
    public String toString() {
        return username + " (Utente Generico)";
    }
}