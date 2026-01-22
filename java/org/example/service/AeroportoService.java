package org.example.service;

import org.example.DAO.AeroportoDAO;
import org.example.control.Login;
import org.example.model.Prenotazione;
import org.example.model.Volo;
import java.time.LocalDate;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * AeroportoService è il punto di accesso principale (Facade) per la logica di business.
 * Centralizza la gestione di Utenti, Prenotazioni e Voli.
 */
public class AeroportoService {

    private final AeroportoDAO aeroportoDAO;

    /**
     * Costruttore con Dependency Injection.
     * Utile per i test o per passare configurazioni specifiche del DAO.
     */
    public AeroportoService(AeroportoDAO aeroportoDAO) {
        this.aeroportoDAO = aeroportoDAO;
    }

    /**
     * Costruttore predefinito.
     * Crea un'istanza standard del DAO.
     */
    public AeroportoService() {
        this(new AeroportoDAO());
    }


    /**
     * Tenta di autenticare un utente.
     * @return Un Optional contenente l'utente se trovato, altrimenti Optional.empty()
     */
    public Optional<Login> login(String username, String password) {
        return Optional.ofNullable(aeroportoDAO.findByUsernameAndPassword(username, password));
    }


    public void addPrenotazione(Prenotazione prenotazione) throws ServiceException {
        try {


            aeroportoDAO.inserisciPrenotazione(prenotazione);
        } catch (SQLException e) {
            throw new ServiceException("Errore DB durante la creazione della prenotazione: " + e.getMessage(), e);
        }
    }



    public void addVolo(Volo volo) throws ServiceException {
        validateVolo(volo);
        try {
            aeroportoDAO.inserisciVolo(volo);
        } catch (SQLException e) {
            throw new ServiceException("Errore DB durante l'inserimento del volo: " + e.getMessage(), e);
        }
    }

    public List<Volo> findAllVoli() throws ServiceException {
        try {

            return aeroportoDAO.findAllVoli();
        } catch (SQLException e) {
            throw new ServiceException("Impossibile recuperare la lista dei voli: " + e.getMessage(), e);
        }
    }


    private void validateVolo(Volo volo) throws ServiceException {
        if (volo == null) {
            throw new ServiceException("L'oggetto volo non può essere nullo.");
        }
        if (volo.getCodice() == null || volo.getCodice().trim().isEmpty()) {
            throw new ServiceException("Il codice del volo è obbligatorio.");
        }
        if (volo.getScaloPartenza() == null || volo.getScaloPartenza().trim().isEmpty()) {
            throw new ServiceException("Lo scalo di partenza è obbligatorio.");
        }
        if (volo.getScaloArrivo() == null || volo.getScaloArrivo().trim().isEmpty()) {
            throw new ServiceException("Lo scalo di arrivo è obbligatorio.");
        }
        // Esempio: Controllo date
        if (volo.getData() != null && volo.getData().isBefore(LocalDate.now())) {
            throw new ServiceException("Non puoi inserire un volo nel passato.");
        }
    }
}