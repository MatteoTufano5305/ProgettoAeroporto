package org.example.control;

import org.example.model.Gate;
/**
 Rappresenta un amministratore e serve per gestire le operazioni specifiche sui voli (Aggiunta,rimozione,modifica,assegnazione gate)
 */

public class Admin extends Login {

    /**
     * Costruttore per creare un nuovo admin
     * E,a differenza dell'utente generico, imposta forzatamente i privilegi di amministratore a true
     * @param id
     * @param user
     * @param pass
     */
    public Admin(int id, String user, String pass) {
        super(id, user, pass);
    }

    /**
     * Verifica se l'utente ha i permessi da amministratore
     * @return
     */
    @Override
    public boolean getVerificaAdmin() {
        return true;
    }

    /**
     * Metodi specifici dell'admin
     */

    public void modificaVolo() {
        System.out.println("Admin sta modificando un volo.");

    }

    public void inserisciVolo() {
        System.out.println("Admin sta inserendo un nuovo volo.");

    }

    public void cancellaVolo() {
        System.out.println("Admin sta cancellando un volo.");

    }

    public void assegnaGate(Gate gate) {
        if (gate != null) {
            System.out.println("Admin sta assegnando il gate numero: " + gate.getNumero());
        } else {
            System.out.println("Errore: Gate non valido.");
        }
    }

    @Override
    public String toString() {
        return username + " (Admin)";
    }
}