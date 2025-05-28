package org.example.model;
public class Gate {
    private String numero;
    private String stato;

    public Gate(String numero, String stato) {
        this.numero = numero;
        this.stato = stato;
    }

    public String getNumero() {
        return numero;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    @Override
    public String toString() {
        return numero + " (" + stato + ")";
    }
}