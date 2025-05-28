package org.example.model;
import java.time.LocalDateTime;

public class Prenotazione {
    private String codicePrenotazione;
    private int postoAssegnato;
    private LocalDateTime dataPrenotazione;
    private String nomePasseggero;
    private String numeroBiglietto;
    private StatoPrenotazione stato;
    private UtenteGenerico utente;
    private Volo volo;

    public Prenotazione(String codicePrenotazione, int postoAssegnato, LocalDateTime dataPrenotazione,
                        String nomePasseggero, String numeroBiglietto, UtenteGenerico utente, Volo volo) {
        this.codicePrenotazione = codicePrenotazione;
        this.postoAssegnato = postoAssegnato;
        this.dataPrenotazione = dataPrenotazione;
        this.nomePasseggero = nomePasseggero;
        this.numeroBiglietto = numeroBiglietto;
        this.stato = StatoPrenotazione.IN_ATTESA;
        this.utente = utente;
        this.volo = volo;
    }

    public String getCodicePrenotazione() {
        return codicePrenotazione;
    }

    public int getPostoAssegnato() {
        return postoAssegnato;
    }

    public LocalDateTime getDataPrenotazione() {
        return dataPrenotazione;
    }

    public String getNomePasseggero() {
        return nomePasseggero;
    }

    public String getNumeroBiglietto() {
        return numeroBiglietto;
    }

    public StatoPrenotazione getStato() {
        return stato;
    }

    public void setStato(StatoPrenotazione stato) {
        this.stato = stato;
    }

    public UtenteGenerico getUtente() {
        return utente;
    }

    public Volo getVolo() {
        return volo;
    }

    public void checkIn() {
        this.stato = StatoPrenotazione.CONFERMATA;
        System.out.println("Prenotazione " + codicePrenotazione + " checked in.");
    }

    public void checkOut() {
        this.stato = StatoPrenotazione.CANCELLATA;
        System.out.println("Prenotazione " + codicePrenotazione + " checked out (cancelled).");
    }
}