package model;

public class Login {
    private int idUtente;
    private String username;
    private String password;
    private boolean verificaAdmin;

    public Login(int idUtente, String username, String password, boolean verificaAdmin) {
        this.idUtente = idUtente;
        this.username = username;
        this.password = password;
        this.verificaAdmin = verificaAdmin;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public boolean isVerificaAdmin() {
        return verificaAdmin;
    }
}