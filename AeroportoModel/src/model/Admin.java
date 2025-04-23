package model;

public class Admin extends Login {

    public Admin(int idUtente, String username, String password) {
        super(idUtente, username, password, true);
    }

    public void modificaVolo() {
        System.out.println("Volo modificato.");
    }

    public void inserisciVolo() {
        System.out.println("Volo inserito.");
    }

    public void cancellaVolo() {
        System.out.println("Volo cancellato.");
    }
}