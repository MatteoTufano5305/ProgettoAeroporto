package model;

public class Prenotazione {
    private String codicePrenotazione;
    private String codiceVolo;
    private String postoAssegnato;
    private String nomePasseggero;
    private String numeroBiglietto;
    private int idUtente;
    private StatoPrenotazione stato;

    public Prenotazione(String codicePrenotazione, String codiceVolo, String nomePasseggero, int idUtente) {
        this.codicePrenotazione = codicePrenotazione;
        this.codiceVolo = codiceVolo;
        this.nomePasseggero = nomePasseggero;
        this.idUtente = idUtente;
        this.stato = StatoPrenotazione.IN_ATTESA;
    }

    public void checkIn() {
        System.out.println("Check-in effettuato per " + nomePasseggero);
        this.stato = StatoPrenotazione.CONFERMATA;
    }

    public void checkOut() {
        System.out.println("Check-out effettuato per " + nomePasseggero);
        this.stato = StatoPrenotazione.CANCELLATA;
    }
}