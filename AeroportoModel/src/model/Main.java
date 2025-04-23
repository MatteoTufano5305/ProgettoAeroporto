package model;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Gate gateA = new Gate("A1", "Libero");

        StatoVolo stato = StatoVolo.PROGRAMMATO;

        InPartenza volo = new InPartenza("NAP123", "Alitalia", LocalDateTime.now(), "15:00", gateA, stato);
        System.out.println("Volo " + volo.getCodice() + " stato: " + volo.getStatoDelVolo());

        UtenteGenerico utente = new UtenteGenerico(1, "marinoematteo", "ciao12345", "Matteo Marino");
        utente.effettuaPrenotazioni();

        Prenotazione pren = new Prenotazione("P123", "NAP123", "Marino Matteo", 1);
        pren.checkIn();
    }
}