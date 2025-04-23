# Aeroporto - Modello di Dominio (Homework)

Questo pacchetto contiene l'implementazione del **Model** per il progetto di gestione dell'aeroporto, secondo quanto richiesto dal primo homework del corso.

## 📁 Struttura del progetto

```
AeroportoModel/
└── src/
    └── model/
        ├── Admin.java
        ├── Gate.java
        ├── InArrivo.java
        ├── InPartenza.java
        ├── Login.java
        ├── Main.java
        ├── Prenotazione.java
        ├── StatoPrenotazione.java
        ├── StatoVolo.java
        ├── UtenteGenerico.java
        └── Volo.java
```

## ✅ Contenuto

Tutte le classi del **diagramma del dominio** sono state tradotte in Java e posizionate nel package `model`. È incluso anche un file `Main.java` per testare il funzionamento delle classi.

## ▶️ Esecuzione

Per eseguire il test:
1. Importa il progetto in un IDE come **IntelliJ IDEA** o **Eclipse**
2. Assicurati che la struttura del pacchetto sia `src/model`
3. Esegui il file `Main.java`

## 📄 Descrizione

Il modello include:
- Classi per login, utenti e amministratori
- Classi per voli in partenza/arrivo, prenotazioni e gate
- Enum per gli stati di volo e prenotazione
- Metodi simulati per check-in, prenotazione e gestione dei voli

## 🛠️ Avvertenze

Questo modello è una **prima versione** e servirà come base per la futura integrazione con:
- Database relazionale
- Interfaccia grafica Java Swing

## 🧑‍💻 Autori

Gruppo di studenti del corso di Informatica della Federico II - 2° anno (Marino Ometo & Matteo Tufano)

---
Scadenza progetto: **24 aprile 2025**