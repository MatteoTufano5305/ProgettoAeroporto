package org.example.model;
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