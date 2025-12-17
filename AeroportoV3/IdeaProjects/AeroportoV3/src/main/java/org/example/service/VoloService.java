package org.example.control;

import org.example.control.VoloDAO;
import org.example.model.Volo;
import java.sql.SQLException;
import java.util.List;

/**
 * Questa classe valida prima i dati dei voli prima dell'inserimento e di astrarre le eccezioni di basso livello
 * trasformandole in eccezioni di servizio gestibili dalla GUI
 */

public class VoloService {
    /**
     * Riferimento al DAO
     */
    private final VoloDAO voloDAO;

    /**
     * Costruttore predefinito
     * inizializza l'istanza del DAO
     */

    public VoloService() {
        this.voloDAO = new VoloDAO();
    }

    /**
     * Aggiunge un nuovo Volo
     *
     * Il metodo esegue delle verifiche sulla validità dei dati
     * @param volo
     * @throws org.example.service.ServiceException
     */
    public void addVolo(Volo volo) throws org.example.service.ServiceException {


        if (volo.getCodiceVolo() == null || volo.getCodiceVolo().trim().isEmpty()) {
            throw new org.example.service.ServiceException("Il codice del volo non può essere vuoto.");
        }

        try {
            voloDAO.inserisciVolo(volo);
        } catch (SQLException e) {

            throw new org.example.service.ServiceException("Errore del database durante l'inserimento del volo. Dettagli: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera la lista di tutti i voli presenti nel sistema
     * @return
     * @throws org.example.service.ServiceException
     * @throws SQLException
     */

    public List<Volo> findAllVoli() throws org.example.service.ServiceException, SQLException {

        return voloDAO.findAll();
    }

}
