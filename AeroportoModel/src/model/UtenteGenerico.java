package model;

public class UtenteGenerico extends Login {

    private String nome;
    public UtenteGenerico(int idUtente, String username, String password, String nome) {
        super(idUtente, username, password, false);
        this.nome = nome;
    }

    public void effettuaPrenotazioni() {
        System.out.println("Prenotazione effettuata.");
    }

    public void ricercaVolo() {
        System.out.println("Ricerca volo effettuata.");
    }
}