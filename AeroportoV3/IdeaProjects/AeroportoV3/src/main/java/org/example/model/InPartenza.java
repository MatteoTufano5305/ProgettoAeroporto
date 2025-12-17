package org.example.model;

/**
 * Questa classe è un componente del modello dati che incapsula i dettagli
 * relative al viaggio in uscita all'aeroporto.
 * Può essere utilizzata per modellare i voli che originano dallo scalo locale
 */
public class InPartenza {
    private String origine;
    private Gate gate;

    public InPartenza(String origine, Gate gate) {
        this.origine = origine;
        this.gate = gate;
    }

    public String getOrigine() {
        return origine;
    }

    public Gate getGate() {
        return gate;
    }

    public void setGate(Gate gate) {
        this.gate = gate;
    }
}