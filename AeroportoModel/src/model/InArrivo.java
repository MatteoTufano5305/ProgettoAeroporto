package model;

public class InArrivo extends Volo {
    private final String destinazione = "Napoli";

    public InArrivo(String codice, String compagnia, java.time.LocalDateTime data, String orario, StatoVolo stato) {
        super(codice, compagnia, data, orario, stato);
    }
}