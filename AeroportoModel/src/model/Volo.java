package model;

import java.time.LocalDateTime;

public class Volo {
    private String codice;
    private String compagniaAerea;
    private LocalDateTime dataDelVolo;
    private String orarioPrevisto;
    private StatoVolo statoDelVolo;

    public Volo(String codice, String compagniaAerea, LocalDateTime dataDelVolo, String orarioPrevisto, StatoVolo statoDelVolo) {
        this.codice = codice;
        this.compagniaAerea = compagniaAerea;
        this.dataDelVolo = dataDelVolo;
        this.orarioPrevisto = orarioPrevisto;
        this.statoDelVolo = statoDelVolo;
    }

    public String getCodice() {
        return codice;
    }

    public StatoVolo getStatoDelVolo() {
        return statoDelVolo;
    }

    public void setStatoDelVolo(StatoVolo stato) {
        this.statoDelVolo = stato;
    }
}