package model;

import java.time.LocalDateTime;

public class InPartenza extends Volo {
    private Gate gate;
    private final String origine = "Napoli";

    public InPartenza(String codice, String compagnia, LocalDateTime data, String orario, Gate gate, StatoVolo stato) {
        super(codice, compagnia, data, orario, stato);
        this.gate = gate;
    }

    public Gate getGate() {
        return gate;
    }

    public String getOrigine() {
        return origine;
    }

    public void setGate(Gate gate) {
        this.gate = gate;
    }
}