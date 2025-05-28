// Admin.java (inherits from Login)
public class Admin extends Login {
    public Admin(int idUtente, String username, String password) {
        super(idUtente, username, password, true); // Admin is always an admin
    }

    public void modificaVolo() {
        System.out.println("Admin sta modificando un volo.");
        // Logic for modifying a flight
    }

    public void inserisciVolo() {
        System.out.println("Admin sta inserendo un nuovo volo.");
        // Logic for inserting a flight
    }

    public void cancellaVolo() {
        System.out.println("Admin sta cancellando un volo.");
        // Logic for deleting a flight
    }

    public void assegnaGate(Gate gate) {
        System.out.println("Admin sta assegnando un gate: " + gate.getNumero());
        // Logic for assigning a gate
    }

    @Override
    public boolean getVerificaAdmin() {
        return true; // An admin is an admin
    }
}
