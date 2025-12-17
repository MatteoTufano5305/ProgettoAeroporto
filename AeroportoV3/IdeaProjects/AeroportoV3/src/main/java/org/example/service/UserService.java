package org.example.service;

import org.example.control.Login;
import org.example.control.UserDAO;

/**
 * Questa classe funge da intermediario tra i controller dell'interfaccia dell'utente e il livello di persistenza dei dati
 */
public class UserService {
    /**
     * Riferimento dal Data Access Object
     */
    private UserDAO userDAO = new UserDAO();

    /**
     * Tenta di autenticare un utente verificando le credenziali fornite
     *
     * Il metodo delega la verifica effettiva al {@Link userDAO}
     * Non gestisce le eccezioni poiché vengono gestite all'interno del livello DAO
     *
     * @param username
     * @param password
     * @return
     */
    public Login login(String username, String password) {
        // NON serve il try-catch qui, perché lo fa già il DAO!
        return userDAO.findByUsernameAndPassword(username, password);
    }
}