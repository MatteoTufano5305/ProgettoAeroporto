package org.example.control;
import org.example.model.Gate;

public class Admin extends Login {
    public Admin(int idUtente, String username, String password) {
        super(idUtente, username, password);
        this.verificaAdmin = true;
    }

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
        System.out.println("Admin sta assegnando un gate: " + gate.getNumero());
    }

    @Override
    public boolean getVerificaAdmin() {
        return verificaAdmin;
    }

    @Override
    public String toString() {
        return username + " (Admin)";
    }
}