package org.example.service;

import org.example.DAO.AeroportoDAO;
import org.example.model.Prenotazione;

import java.sql.SQLException;

/**
 * Gestisce la logica del business relativa alle prenotazioni
 * Il compito principale è astrarre la complessità del database,cattura le eccezioni SQL di basso livello
 * e le rilancia al ServiceExpection permettendo di gestire l'errore senza dipendere dalle librerie SQL
 */
public class PrenotazioneService {

    private final AeroportoDAO prenotazioneDAO;

    /**
     * Costruttore predefinito
     */
    public PrenotazioneService() {
        this.prenotazioneDAO = new AeroportoDAO();
    }

    /**
     * Registra una nuova prenotazione
     * Questo metodo riceve un oggetto prenotazione completo e delega l'inserimento al DAO.
     * In futuro, qui potranno essere aggiunti controlli di validazione (es. verificare se il posto
     * è ancora libero prima di tentare l'inserimento)
     * @param prenotazione
     * @throws org.example.service.ServiceException
     */
    public void addPrenotazione(Prenotazione prenotazione) throws org.example.service.ServiceException {


        try {
            prenotazioneDAO.inserisciPrenotazione(prenotazione);
        } catch (SQLException e) {
            throw new org.example.service.ServiceException("Errore DB durante la creazione della prenotazione. Dettagli: " + e.getMessage(), e);
        }
    }


}