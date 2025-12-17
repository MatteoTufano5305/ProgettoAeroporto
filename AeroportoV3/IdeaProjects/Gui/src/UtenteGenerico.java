// UtenteGenerico.java (inherits from Login)
public class UtenteGenerico extends Login {
    private String nome;

    public UtenteGenerico(int idUtente, String username, String password, boolean verificaAdmin, String nome) {
        super(idUtente, username, password, verificaAdmin);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void effettuaPrenotazione() {
        System.out.println("Utente " + nome + " sta effettuando una prenotazione.");
        // Logic for making a reservation will be handled by the GUI and data management
    }

    public void ricercaVolo() {
        System.out.println("Utente " + nome + " sta ricercando un volo.");
        // Logic for searching a flight will be handled by the GUI and data management
    }

    @Override
    public boolean getVerificaAdmin() {
        return false; // A generic user is not an admin
    }
}