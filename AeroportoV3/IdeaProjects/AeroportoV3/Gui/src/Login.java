// Login.java (abstract or concrete, depending on whether it represents a base for users)
public abstract class Login {
    protected int idUtente;
    protected String username;
    protected String password;
    protected boolean verificaAdmin;

    public Login(int idUtente, String username, String password, boolean verificaAdmin) {
        this.idUtente = idUtente;
        this.username = username;
        this.password = password;
        this.verificaAdmin = verificaAdmin;
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

    public boolean isVerificaAdmin() {
        return verificaAdmin;
    }

    // This method needs to be implemented by concrete subclasses or by a separate service
    public abstract boolean getVerificaAdmin();
}