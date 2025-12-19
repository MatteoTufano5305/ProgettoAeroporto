# Progetto Aeroporto - Sistema di Gestione

Questo repository contiene l'implementazione completa del sistema di gestione aeroportuale. Il progetto è un'applicazione desktop sviluppata in **Java** che utilizza un'interfaccia grafica **Swing** e un **Database Relazionale** per la persistenza dei dati.

Il sistema segue l'architettura **MVC (Model-View-Controller)** separando la logica di business, l'interfaccia utente e l'accesso ai dati.

## Struttura del Progetto

Il progetto è suddiviso in moduli logici per garantire modularità e manutenibilità.

### Interfaccia Grafica (View)

Implementata con Java Swing (`src/Gui`).

```text

Gui/src/

├── Admin.form / .java          # Pannello Amministratore
├── CercaVolo.form / .java      # Ricerca voli
├── Login.form / .java          # Schermata di accesso
├── Main.java                   # Entry point GUI
├── Prenotazione.form / .java   # Gestione prenotazioni
└── UtenteGenerico.form / .java # Dashboard utente

# Progetto Aeroporto - Sistema di Gestione

Questo repository contiene l'implementazione completa del sistema di gestione aeroportuale. Il progetto è un'applicazione desktop sviluppata in **Java** che utilizza un'interfaccia grafica **Swing** e un **Database Relazionale** per la persistenza dei dati.

Il sistema segue l'architettura **MVC (Model-View-Controller)** separando la logica di business, l'interfaccia utente e l'accesso ai dati.

## Struttura del Progetto
Il progetto è suddiviso in moduli logici per garantire modularità e manutenibilità.

### Interfaccia Grafica (View)

Implementata con Java Swing (`src/Gui`).
* Contiene i file con estensione `.form` e `.java` per le diverse schermate, come ad esempio Login, Admin, CercaVolo e così via.

### Logica Applicativa (Backend)

Il core dell'applicazione si trova nel package `org.example`.
**Model (`model/`)**

Rappresentazione degli oggetti del dominio.

* `Volo`, `InArrivo`, `InPartenza`
* `Prenotazione`, `StatoPrenotazione`
* `UtenteGenerico`
* `Gate`

**Control & DAO (`control/`)**

Gestisce il flusso dei dati e la comunicazione con il database.
* `DatabaseManager`: Gestione connessione DB
* `UserDAO`, `VoloDAO`, `PrenotazioneDAO`: Data Access Objects
* `Admin`, `Login`: Logica di controllo utenti
**Service (`service/`)**

Livello di servizio per la logica di business complessa.
* `PrenotazioneService`
* `UserService`
* `VoloService`

## Tecnologie Utilizzate

* **Linguaggio:** Java
* **GUI:** Java Swing (Form Designer)
* **Database:** SQL (Relazionale)
* **Build Tool:** Maven
* **IDE consigliato:** IntelliJ IDEA / Eclipse

## Funzionalità

Il sistema permette di gestire:

* **Autenticazione:** Login differenziato per Utenti generici e Amministratori.
* **Gestione Voli:** Visualizzazione voli in arrivo e in partenza.
* **Prenotazioni:** Creazione e gestione delle prenotazioni passeggeri.
* **Gestione Aeroportuale:** Assegnazione Gate e monitoraggio stati volo.

## Installazione ed Esecuzione

1 **Clona la repository:**

```bash

git clone https://github.com/MatteoTufano5305/ProgettoAeroporto.git

```

2.  **Configura il Database:**

Assicurati di avere un database attivo e configura le credenziali in `DatabaseManager.java`.

3.  **Importa nell'IDE:**
Per iniziare, apri il tuo progetto con un ambiente di sviluppo come IntelliJ IDEA o Eclipse. Il modo migliore per farlo è importandolo come progetto Maven. Questo ti permetterà di sfruttare tutte le funzionalità di Maven per la gestione delle dipendenze e la compilazione del tuo progetto. In questo modo, potrai lavorare in modo più efficiente e organizzato.

4.  **Esegui:**
Avvia la classe `Main.java` situata nel modulo GUI per lanciare l'interfaccia grafica.

## Autori
Progetto realizzato dagli studenti del corso di Informatica della Federico II - 2° Anno:
* **Marino Ometo**
* **Matteo Tufano**

**Scadenza progetto:**31 marzo 2026**
