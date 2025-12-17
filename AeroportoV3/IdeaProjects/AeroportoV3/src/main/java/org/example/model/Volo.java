package org.example.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un volo all'interno del sistema
 *
 * Gestisce le informazioni essenziali del Volo (Codice,Orari,Partenza,Arrivo,Compagnia Aerea,Gate Assegnato,Stato del Volo e la Gestione dei posti)
 */
public class Volo {
    private String codice;
    private String compagniaAerea;
    private LocalDateTime dataOra;
    private String scaloPartenza;
    private String scaloArrivo;
    private StatoVolo statoVolo;
    private Gate gateAssegnato;

    /**
     * Numero di Default di posti
     */
    private int totalSeats = 100;
    private List<Integer> occupiedSeats = new ArrayList<>();

    public Volo() {

    }

    /**
     * Costruttore per impostare il volo
     * @param codice
     * @param compagniaAerea
     * @param dataOra
     * @param scaloPartenza
     * @param scaloArrivo
     * @param statoVolo
     */
    public Volo(String codice, String compagniaAerea,LocalDateTime dataOra,String scaloPartenza,String scaloArrivo,StatoVolo statoVolo) {
        this.codice = codice;
        this.compagniaAerea = compagniaAerea;
        this.dataOra = dataOra;
        this.scaloPartenza = scaloPartenza;
        this.scaloArrivo = scaloArrivo;
        this.statoVolo = statoVolo;

    }


    public String getCodice() {return codice;
    }

    public String getCompagniaAerea() {
        return compagniaAerea;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public String getScaloPartenza() {
        return scaloPartenza;
    }

    public String getScaloArrivo() {
        return scaloArrivo;
    }

    public StatoVolo getStatoVolo() {
        return statoVolo;
    }

    public void setStatoVolo(StatoVolo statoVolo) {
        this.statoVolo = statoVolo;
    }

    public Gate getGateAssegnato() {
        return gateAssegnato;
    }

    public void setGateAssegnato(Gate gateAssegnato) {
        this.gateAssegnato = gateAssegnato;
    }

    /**
     * Verifica se i posti sono disponibili
     * @param seatNumber
     * @return
     */
    public boolean isSeatAvailable(int seatNumber) {
        return seatNumber > 0 && seatNumber <= totalSeats && !occupiedSeats.contains(seatNumber);
    }

    /**
     * Tenta di prenotare un posto specifico
     * @param seatNumber
     * @return
     */
    public boolean bookSeat(int seatNumber) {
        if (isSeatAvailable(seatNumber)) {
            occupiedSeats.add(seatNumber);
            return true;
        }
        return false;
    }

    /**
     * Cancella la prenotazione di un posto,rendendolo disponibile
     * @param seatNumber
     * @return
     */
    public boolean cancelSeat(int seatNumber) {
        return occupiedSeats.remove(Integer.valueOf(seatNumber));
    }

    /**
     * Calcola il numero di posti disponibili
     * @return
     */
    public int getAvailableSeatsCount() {
        return totalSeats - occupiedSeats.size();
    }

    @Override
    public String toString() {
        return codice + " (" + scaloPartenza + " - " + scaloArrivo + ")";
    }

    public String getCodiceVolo() {
        return codice;
    }

    public String getDestinazione() {
        return scaloArrivo;
    }

    public void setCodiceVolo(String codiceVolo) {
        this.codice = codiceVolo;
    }

    public void setDestinazione(String destinazione) {
        this.scaloArrivo = destinazione;
    }

    public void setScaloPartenza(String scaloPartenza) {
        this.scaloPartenza = scaloPartenza;
    }

    public void setCompagniaAerea(String compagniaAerea) {
        this.compagniaAerea = compagniaAerea;
    }

    public void setScaloArrivo(String scaloArrivo) {
        this.scaloArrivo = scaloArrivo;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }
}