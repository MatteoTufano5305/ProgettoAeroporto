package org.example.control;

public abstract class Login {
    protected int idUtente;
    protected String username;
    protected String password;
    protected boolean verificaAdmin;

    public Login(int idUtente, String username, String password) {
        this.idUtente = idUtente;
        this.username = username;
        this.password = password;
        this.verificaAdmin = false;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public abstract boolean getVerificaAdmin();
}