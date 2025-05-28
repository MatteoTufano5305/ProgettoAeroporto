package org.example.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Volo {
    private String codice;
    private String compagniaAerea;
    private LocalDateTime dataOra;
    private String scaloPartenza;
    private String scaloArrivo;
    private StatoVolo statoVolo;
    private Gate gateAssegnato;


    private int totalSeats = 100;
    private List<Integer> occupiedSeats = new ArrayList<>();

    public Volo(String codice, String compagniaAerea, LocalDateTime dataOra, String scaloPartenza, String scaloArrivo, StatoVolo statoVolo) {
        this.codice = codice;
        this.compagniaAerea = compagniaAerea;
        this.dataOra = dataOra;
        this.scaloPartenza = scaloPartenza;
        this.scaloArrivo = scaloArrivo;
        this.statoVolo = statoVolo;
        // occupiedSeats initialized here
    }

    public String getCodice() {
        return codice;
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

    public boolean isSeatAvailable(int seatNumber) {
        return seatNumber > 0 && seatNumber <= totalSeats && !occupiedSeats.contains(seatNumber);
    }

    public boolean bookSeat(int seatNumber) {
        if (isSeatAvailable(seatNumber)) {
            occupiedSeats.add(seatNumber);
            return true;
        }
        return false;
    }

    public boolean cancelSeat(int seatNumber) {
        return occupiedSeats.remove(Integer.valueOf(seatNumber));
    }

    public int getAvailableSeatsCount() {
        return totalSeats - occupiedSeats.size();
    }

    @Override
    public String toString() {
        return codice + " (" + scaloPartenza + " - " + scaloArrivo + ")";
    }
}