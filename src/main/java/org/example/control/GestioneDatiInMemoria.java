package org.example.control;

import org.example.model.*;

import java.time.LocalDate;
import java.time.LocalTime; // Assicurati che ci sia questo import
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe che simula l'interazione con un database gestendo i dati in memoria RAM.
 */
public class GestioneDatiInMemoria {

    private static final List<Login> UTENTI = new ArrayList<>();
    private static final List<Gate> GATES = new ArrayList<>();
    private static final List<Volo> VOLI = new ArrayList<>();
    private static final List<Prenotazione> PRENOTAZIONI = new ArrayList<>();

    static {
        UTENTI.add(new Admin(1, "admin", "adminpass"));
        UTENTI.add(new UtenteGenerico(2, "user1", "userpass", "Mario Rossi"));


        GATES.add(new Gate("A1", "Libero"));
        GATES.add(new Gate("A2", "Libero"));
        GATES.add(new Gate("B1", "Libero"));

        VOLI.add(new Volo("AZ100", "Alitalia", LocalDate.now(), LocalTime.of(14, 30), "Roma", "Milano", StatoVolo.PROGRAMMATO));
        VOLI.add(new Volo("LH450", "Lufthansa", LocalDate.now().plusDays(1), LocalTime.of(9, 0), "Francoforte", "New York", StatoVolo.IN_VOLO));
    }



    public Login authenticateUser(String username, String password) {
        return UTENTI.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public List<Login> getAllUtenti() {
        return new ArrayList<>(UTENTI);
    }

    public boolean insertUtente(Login utente) {
        return UTENTI.add(utente);
    }


    public List<Volo> getAllVoli() {
        return new ArrayList<>(VOLI);
    }

    public Volo findFlightByCode(String code) {
        return VOLI.stream()
                .filter(v -> v.getCodice().equalsIgnoreCase(code))
                .findFirst()
                .orElse(null);
    }

    public boolean insertVolo(Volo volo) {
        if (findFlightByCode(volo.getCodice()) != null) {
            return false;
        }
        return VOLI.add(volo);
    }

    /**
     * Aggiorna tutti i campi modificabili di un Volo esistente.
     */
    public boolean updateVolo(Volo updatedVolo) {
        Volo existingVolo = findFlightByCode(updatedVolo.getCodice());
        if (existingVolo != null) {
            existingVolo.setCompagniaAerea(updatedVolo.getCompagniaAerea());
            existingVolo.setScaloPartenza(updatedVolo.getScaloPartenza());
            existingVolo.setScaloArrivo(updatedVolo.getScaloArrivo());
            existingVolo.setData(updatedVolo.getData());
            existingVolo.setOra(updatedVolo.getOra());


            existingVolo.setStatoVolo(updatedVolo.getStatoVolo());
            existingVolo.setGateAssegnato(updatedVolo.getGateAssegnato());
            return true;
        }
        return false;
    }

    public boolean updateVoloStato(Volo updatedVolo) {
        Volo existingVolo = findFlightByCode(updatedVolo.getCodice());
        if (existingVolo != null) {
            existingVolo.setStatoVolo(updatedVolo.getStatoVolo());
            return true;
        }
        return false;
    }

    public boolean updateVoloGate(Volo volo, Gate gate) {
        Volo existingVolo = findFlightByCode(volo.getCodice());
        if (existingVolo != null) {
            if (existingVolo.getGateAssegnato() != null) {
                existingVolo.getGateAssegnato().setStato("Libero");
            }
            existingVolo.setGateAssegnato(gate);
            if (gate != null) {
                gate.setStato("Occupato");
            }
            return true;
        }
        return false;
    }

    public boolean deleteVolo(String codice) {
        deletePrenotazioniByVolo(codice);
        return VOLI.removeIf(v -> v.getCodice().equalsIgnoreCase(codice));
    }

    public List<Volo> searchFlights(String partenza, String arrivo) {
        return VOLI.stream()
                .filter(v -> partenza == null || partenza.isEmpty() || v.getScaloPartenza().contains(partenza))
                .filter(v -> arrivo == null || arrivo.isEmpty() || v.getScaloArrivo().contains(arrivo))
                .collect(Collectors.toList());
    }


    public List<Gate> getAllGates() {
        return new ArrayList<>(GATES);
    }

    public boolean insertGate(Gate gate) {
        return GATES.add(gate);
    }

    public Gate findGateByNumber(String gateNumber) {
        return GATES.stream()
                .filter(g -> g.getNumero().equalsIgnoreCase(gateNumber))
                .findFirst()
                .orElse(null);
    }

    public List<Gate> getFreeGates() {
        return GATES.stream()
                .filter(g -> g.getStato().equalsIgnoreCase("Libero"))
                .collect(Collectors.toList());
    }

    public void updateGateStato(String numero, String stato) {
        Gate gate = findGateByNumber(numero);
        if (gate != null) {
            gate.setStato(stato);
        }
    }


    public boolean isSeatAvailable(String voloCodice, int posto) {
        return PRENOTAZIONI.stream()
                .noneMatch(p -> p.getVoloCodice().equalsIgnoreCase(voloCodice) && p.getNumeroPosto() == posto);
    }

    public List<Integer> getBookedSeats(String voloCodice) {
        return PRENOTAZIONI.stream()
                .filter(p -> p.getVoloCodice().equalsIgnoreCase(voloCodice))

                .map(Prenotazione::getNumeroPosto)
                .collect(Collectors.toList());
    }

    public boolean insertPrenotazione(Prenotazione p) {
        return PRENOTAZIONI.add(p);
    }

    public void deletePrenotazioniByVolo(String voloCodice) {
        PRENOTAZIONI.removeIf(p -> p.getVoloCodice().equalsIgnoreCase(voloCodice));
    }
}