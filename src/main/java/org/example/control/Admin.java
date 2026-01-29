package org.example.control;

import org.example.DAO.AeroportoDAO;
import org.example.model.Gate;
import org.example.model.Volo;

/**
 * Rappresenta un utente con privilegi elevati all'interno del sistema.
 * <p>
 * A differenza di {@link org.example.model.UtenteGenerico}, questa classe fornisce i metodi di controllo
 * per la gestione dei voli. Non interagisce direttamente con il database,
 * ma con le operazioni attraverso l'{@link AeroportoDAO}.
 * </p>
 */
public class Admin extends Login {

    /**
     * Crea un profilo Amministratore.
     * I privilegi di amministrazione sono implicitamente gestiti dal metodo
     * {@link #getVerificaAdmin()}.
     * * @param id   Identificativo univoco dell'admin.
     * @param user Nome utente per il login.
     * @param pass Password associata.
     */
    public Admin(int id, String user, String pass) {
        super(id, user, pass);
    }

    /**
     * Verifica per vedere se possiede i permessi di amministratore.
     * @return {@code true} costantemente, in quanto l'oggetto è di tipo Admin.
     */
    @Override
    public boolean getVerificaAdmin() {
        return true;
    }

    /**
     * Coordina l'aggiornamento dei dati di un volo esistente.
     * @param volo L'oggetto Volo contenente le modifiche da persistere.
     * @param dao  L'istanza del DAO da utilizzare per l'operazione.
     * @return {@code true} se la modifica è avvenuta con successo.
     */
    public boolean modificaVolo(Volo volo, AeroportoDAO dao) {
        return dao.updateStatoVolo(volo);
    }

    /**
     * Autorizza la rimozione di un volo dal sistema.
     * @param codiceVolo Stringa univoca del volo da eliminare.
     * @param dao        Istanza del DAO.
     * @return Esito dell'operazione di cancellazione.
     */
    public boolean cancellaVolo(String codiceVolo, AeroportoDAO dao) {
        return dao.deleteVolo(codiceVolo);
    }

    /**
     * Gestisce l'assegnazione di un Gate specifico a un volo.
     * @param gate L'oggetto {@link Gate} da assegnare.
     */
    public void assegnaGate(Gate gate) {
        if (gate != null) {
            System.out.println("Admin " + username + " ha assegnato il gate: " + gate.getNumero());
        }
    }

    @Override
    public String toString() {
        return "Amministratore: " + username;
    }
}