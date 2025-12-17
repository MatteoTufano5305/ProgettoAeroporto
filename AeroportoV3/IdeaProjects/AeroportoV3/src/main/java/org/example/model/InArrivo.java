package org.example.model;

/**
 * Questa classe è un componente del modello dati che incapsula le informazioni
 * relative alla destinazione finale di un flusso di arrivo.
 * Può essere utilizzata per distinguere logicamente i voli in arrivo da quelli in partenza
 * o per filtrare visualizzazioni specifiche.
 */
public class InArrivo {
    private String destinazione;

    public InArrivo(String destinazione) {
        this.destinazione = destinazione;
    }

    public String getDestinazione() {
        return destinazione;
    }
}