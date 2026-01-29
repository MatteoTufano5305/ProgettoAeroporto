# Progetto Aeroporto - Homework per OO

Questo repository contiene l'implementazione completa del sistema di gestione aeroportuale. Il progetto è un'applicazione desktop sviluppata in **Java** che utilizza un'interfaccia grafica **Swing** e un **Database Relazionale** per la persistenza dei dati.

Il sistema segue l'architettura **MVC (Model-View-Controller)** separando la logica di business, l'interfaccia utente e l'accesso ai dati.

##Struttura del Progetto

Il progetto è suddiviso in moduli logici per garantire modularità e manutenibilità.

###Interfaccia Grafica (View)
Implementata con Java Swing (`src/main/java/org/example/gui`).
* Contiene i file `.form` e `.java` per le schermate (Login, Admin, CercaVolo, ecc.).

###Logica Applicativa (Backend)
Il core dell'applicazione si trova nel package `org.example`.

**Model (`model/`)**
Rappresentazione degli oggetti del dominio.
* `Volo`,`StatoVolo`, `InArrivo`, `InPartenza`
* `Prenotazione`, `StatoPrenotazione`
* `UtenteGenerico`
* `Gate`

**Control & DAO (`control/`)**
Gestisce il flusso dei dati e la comunicazione con il database.
* `DatabaseManager`: Gestione connessione DB
* `AeroportoDAO`: Data Access Objects
* `Admin`, `Login`: Logica di controllo utenti

**Service (`service/`)**
Livello di servizio per la logica di business complessa.
* `ServiceException`


##Tecnologie Utilizzate

* **Linguaggio:** Java
* **GUI:** Java Swing (Form Designer)
* **Database:** SQL (Relazionale)
* **Build Tool:** Maven
* **IDE consigliato:** IntelliJ IDEA / Eclipse

##Funzionalità

Il sistema permette di gestire:
* **Autenticazione:** Login differenziato per Utenti generici e Amministratori.
* **Gestione Voli:** Visualizzazione voli in arrivo e in partenza.
* **Prenotazioni:** Creazione e gestione delle prenotazioni passeggeri.
* **Gestione Aeroportuale:** Assegnazione Gate e monitoraggio stati volo.

##Installazione ed Esecuzione

1.  **Clona il repository:**
    ```bash
    git clone https://github.com/MatteoTufano5305/ProgettoAeroporto.git
    ```
2.  **Configura il Database:**
    Assicurati di avere un database attivo e configura le credenziali in `DatabaseManager.java`.
3.  **Importa nell'IDE:**
    Apri il progetto con IntelliJ IDEA o Eclipse importandolo come progetto Maven.
4.  **Esegui:**
    Avvia la classe `Main.java` situata nel modulo GUI per lanciare l'interfaccia grafica.

##Autori

Progetto realizzato dagli studenti del corso di Informatica della Federico II - 2° Anno:
* **Marino Ometo**
* **Matteo Tufano**

---
**Scadenza progetto:** 2026
