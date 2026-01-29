package org.example.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Volo {
    private String codice;
    private String compagniaAerea;
    private LocalDate data;
    private LocalTime ora;
    private String scaloPartenza;
    private String scaloArrivo;
    private StatoVolo statoVolo;


    private Gate gateAssegnato;


    public Volo(String codice, String compagniaAerea, LocalDate data, LocalTime ora, String scaloPartenza, String scaloArrivo, StatoVolo statoVolo) {
        this.codice = codice;
        this.compagniaAerea = compagniaAerea;
        this.data = data;
        this.ora = ora;
        this.scaloPartenza = scaloPartenza;
        this.scaloArrivo = scaloArrivo;
        this.statoVolo = statoVolo;
        this.gateAssegnato = null;
    }


    public String getCodice() { return codice; }
    public String getCompagniaAerea() { return compagniaAerea; }
    public LocalDate getData() { return data; }
    public LocalTime getOra() { return ora; }
    public String getScaloPartenza() { return scaloPartenza; }
    public String getScaloArrivo() { return scaloArrivo; }
    public StatoVolo getStatoVolo() { return statoVolo; }
    public Gate getGateAssegnato() { return gateAssegnato; }



    public void setCodice(String codice) {
        this.codice = codice;
    }

    public void setCompagniaAerea(String compagniaAerea) {
        this.compagniaAerea = compagniaAerea;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setOra(LocalTime ora) {
        this.ora = ora;
    }

    public void setScaloPartenza(String scaloPartenza) {
        this.scaloPartenza = scaloPartenza;
    }

    public void setScaloArrivo(String scaloArrivo) {
        this.scaloArrivo = scaloArrivo;
    }

    public void setStatoVolo(StatoVolo statoVolo) {
        this.statoVolo = statoVolo;
    }

    public void setGateAssegnato(Gate gateAssegnato) {
        this.gateAssegnato = gateAssegnato;
    }

    @Override
    public String toString() {
        return codice + " - " + compagniaAerea + " (" + scaloPartenza + " -> " + scaloArrivo + ")";
    }
}