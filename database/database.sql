--
-- PostgreSQL database dump
--

\restrict E8lK5c73kChTZDpP6ZBcFqaC4mDIFDEc19IhWnIgmHo7z7PhIkScaC6JPYUobjH

-- Dumped from database version 18.1
-- Dumped by pg_dump version 18.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: pgagent; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA pgagent;


ALTER SCHEMA pgagent OWNER TO postgres;

--
-- Name: SCHEMA pgagent; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA pgagent IS 'pgAgent system tables';


--
-- Name: pgagent; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS pgagent WITH SCHEMA pgagent;


--
-- Name: EXTENSION pgagent; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pgagent IS 'A PostgreSQL job scheduler';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: prenotazioni; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prenotazioni (
    codice_prenotazione character varying(50) NOT NULL,
    posto_assegnato integer,
    data_prenotazione timestamp without time zone,
    nome_passeggero character varying(100),
    numero_biglietto character varying(50),
    stato character varying(20),
    id_utente integer NOT NULL,
    codice_volo character varying(20) NOT NULL
);


ALTER TABLE public.prenotazioni OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    username character varying(50) NOT NULL,
    password character varying(50) NOT NULL,
    ruolo character varying(20) DEFAULT 'USER'::character varying
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: utenti; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.utenti (
    id_utente integer NOT NULL,
    username character varying(50) NOT NULL,
    password character varying(50) NOT NULL,
    is_admin boolean DEFAULT false,
    nome character varying(100)
);


ALTER TABLE public.utenti OWNER TO postgres;

--
-- Name: utenti_id_utente_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.utenti_id_utente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.utenti_id_utente_seq OWNER TO postgres;

--
-- Name: utenti_id_utente_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.utenti_id_utente_seq OWNED BY public.utenti.id_utente;


--
-- Name: voli; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.voli (
    codice character varying(10) NOT NULL,
    compagnia_aerea character varying(100),
    scalo_partenza character varying(100),
    scalo_arrivo character varying(100),
    data_volo date NOT NULL,
    ora_volo time without time zone NOT NULL,
    stato_volo character varying(50)
);


ALTER TABLE public.voli OWNER TO postgres;

--
-- Name: utenti id_utente; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utenti ALTER COLUMN id_utente SET DEFAULT nextval('public.utenti_id_utente_seq'::regclass);


--
-- Data for Name: pga_jobagent; Type: TABLE DATA; Schema: pgagent; Owner: postgres
--

COPY pgagent.pga_jobagent (jagpid, jaglogintime, jagstation) FROM stdin;
8828	2026-01-13 23:14:04.231042+01	MatteoTufano
\.


--
-- Data for Name: pga_jobclass; Type: TABLE DATA; Schema: pgagent; Owner: postgres
--

COPY pgagent.pga_jobclass (jclid, jclname) FROM stdin;
\.


--
-- Data for Name: pga_job; Type: TABLE DATA; Schema: pgagent; Owner: postgres
--

COPY pgagent.pga_job (jobid, jobjclid, jobname, jobdesc, jobhostagent, jobenabled, jobcreated, jobchanged, jobagentid, jobnextrun, joblastrun) FROM stdin;
\.


--
-- Data for Name: pga_schedule; Type: TABLE DATA; Schema: pgagent; Owner: postgres
--

COPY pgagent.pga_schedule (jscid, jscjobid, jscname, jscdesc, jscenabled, jscstart, jscend, jscminutes, jschours, jscweekdays, jscmonthdays, jscmonths) FROM stdin;
\.


--
-- Data for Name: pga_exception; Type: TABLE DATA; Schema: pgagent; Owner: postgres
--

COPY pgagent.pga_exception (jexid, jexscid, jexdate, jextime) FROM stdin;
\.


--
-- Data for Name: pga_joblog; Type: TABLE DATA; Schema: pgagent; Owner: postgres
--

COPY pgagent.pga_joblog (jlgid, jlgjobid, jlgstatus, jlgstart, jlgduration) FROM stdin;
\.


--
-- Data for Name: pga_jobstep; Type: TABLE DATA; Schema: pgagent; Owner: postgres
--

COPY pgagent.pga_jobstep (jstid, jstjobid, jstname, jstdesc, jstenabled, jstkind, jstcode, jstconnstr, jstdbname, jstonerror, jscnextrun) FROM stdin;
\.


--
-- Data for Name: pga_jobsteplog; Type: TABLE DATA; Schema: pgagent; Owner: postgres
--

COPY pgagent.pga_jobsteplog (jslid, jsljlgid, jsljstid, jslstatus, jslresult, jslstart, jslduration, jsloutput) FROM stdin;
\.


--
-- Data for Name: prenotazioni; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.prenotazioni (codice_prenotazione, posto_assegnato, data_prenotazione, nome_passeggero, numero_biglietto, stato, id_utente, codice_volo) FROM stdin;
PREN-1764942076677	15	2025-12-05 14:41:16.678111	Matteo Tufano	TICKET-001	IN_ATTESA	3	AZ500
PRN-24D9C867	13	2026-01-13 16:29:40.792072	Matteo Tufano	TIX-6F666B1A	CONFERMATA	1	AZ100
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (username, password, ruolo) FROM stdin;
admin	admin123	ADMIN
\.


--
-- Data for Name: utenti; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.utenti (id_utente, username, password, is_admin, nome) FROM stdin;
1	admin	admin123	t	\N
2	mario	rossi	f	\N
3	matteo	tufano	f	Matteo Tufano
\.


--
-- Data for Name: voli; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.voli (codice, compagnia_aerea, scalo_partenza, scalo_arrivo, data_volo, ora_volo, stato_volo) FROM stdin;
AZ100	ITA Airways	Roma Fiumicino	Milano Linate	2025-06-15	08:30:00	PROGRAMMATO
LH450	Lufthansa	Francoforte	New York JFK	2025-06-15	14:00:00	PROGRAMMATO
FR876	Ryanair	Londra Stansted	Roma Ciampino	2025-06-16	18:45:00	IN_RITARDO
DL112	Delta	New York JFK	Roma Fiumicino	2025-06-20	09:15:00	PROGRAMMATO
\.


--
-- Name: utenti_id_utente_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.utenti_id_utente_seq', 6, true);


--
-- Name: prenotazioni prenotazioni_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prenotazioni
    ADD CONSTRAINT prenotazioni_pkey PRIMARY KEY (codice_prenotazione);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (username);


--
-- Name: utenti utenti_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utenti
    ADD CONSTRAINT utenti_pkey PRIMARY KEY (id_utente);


--
-- Name: utenti utenti_username_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.utenti
    ADD CONSTRAINT utenti_username_key UNIQUE (username);


--
-- Name: voli voli_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.voli
    ADD CONSTRAINT voli_pkey PRIMARY KEY (codice);


--
-- Name: prenotazioni prenotazioni_id_utente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prenotazioni
    ADD CONSTRAINT prenotazioni_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES public.utenti(id_utente);


--
-- PostgreSQL database dump complete
--

\unrestrict E8lK5c73kChTZDpP6ZBcFqaC4mDIFDEc19IhWnIgmHo7z7PhIkScaC6JPYUobjH

