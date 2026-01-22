package org.example.model;

/**
 * Questa classe modella il punto d'imbarco per i passeggeri
 * Caratterizzato da un ID e da uno stato operativo
 */
public class Gate {
    private String numero;
    private String stato;

    /**
     * Costruisce un nuovo oggetto Gate
     * @param numero
     * @param stato
     */
    public Gate(String numero, String stato) {
        this.numero = numero;
        this.stato = stato;
    }
    public Gate(String numero) {
        this.numero = numero;
        this.stato = "Attivo";
    }
    /**
     * Restituisce l'ID del Gate
     * @return
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Restituisce lo stato del Gate
     * @return
     */
    public String getStato() {
        return stato;
    }

    /**
     * Aggiorna lo stato del Gate
     * @param stato
     */
    public void setStato(String stato) {
        this.stato = stato;
    }

    @Override
    public String toString() {
        return numero + " (" + stato + ")";
    }
}