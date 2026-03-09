package it.unina.rest_api_dietiestates25.controller;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.*;
import org.hibernate.Session;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

public class VisiteController {

    private final Database database;
    public VisiteController(){
        database = Database.getInstance();
    }
    public VisiteController(Database database){
        this.database = database;
    }

    /**
     * Prenota una visita per un immobile.
     * Controlla fascia oraria, limite 2 settimane e disponibilità slot.
     */
    public Visita prenota(Cliente cliente, ListinoImmobile listino, Instant dataOra, String modeVisita) {
        Session session = database.getSession();

        if (listino == null)
            throw new IllegalArgumentException("Listino non trovato");

        int agenteId = listino.getCreatore().getId();

        // Controllo data entro 2 settimane
        if (dataOra.isAfter(Instant.now().plus(Duration.ofDays(14))))
            throw new IllegalArgumentException("La visita deve essere prenotata entro 2 settimane");

        // Controllo fascia oraria
        ZonedDateTime zdt = dataOra.atZone(ZoneId.of("Europe/Rome"));
        int ora = zdt.getHour();
        if (!((ora >= 9 && ora < 13) || (ora >= 14 && ora < 19)))
            throw new IllegalArgumentException("Orario della visita non valido. Fasce disponibili: 9-12, 15-19");

        // Controllo disponibilità slot
        if (!isSlotDisponibile(agenteId, dataOra))
            throw new IllegalArgumentException("Slot già prenotato");

        Visita visita = new Visita(listino, cliente, listino.getCreatore(), dataOra, StatoVisita.RICHIESTA, modeVisita);

        session.persist(visita);
        return visita;
    }

    /**
     * Controlla se lo slot del giorno/ora è disponibile per l'agente.
     */
    public boolean isSlotDisponibile(int agenteId, Instant dataOra) {
        Session session = database.getSession();

        if(dataOra == null){
            throw new NullPointerException("Data inserita non valida.");
        }

        if(agenteId <= 0){
            throw new IllegalArgumentException("Agente Immobiliare non trovato.");
        }

        AuthController authController = new AuthController();
        if(authController.getUtente(agenteId) == null){
            throw new IllegalArgumentException("Utente non trovato.");
        }

        String hql = "select count(v) from Visita v " +
                "where v.agenteId = :agente " +
                "and v.dataOra = :data " +
                "and v.stato <> :rifiutata";

        Long count = session.createSelectionQuery(hql, Long.class)
                .setParameter("agente", agenteId)
                .setParameter("data", dataOra)
                .setParameter("rifiutata", StatoVisita.RIFIUTATA)
                .getSingleResult();


        return count.intValue() == 0;

    }


    public List<Visita> getVisitePerCliente(int clienteId) {
        Session session = database.getSession();
        return session.createSelectionQuery("from Visita v where v.clienteId = :id", Visita.class)
                .setParameter("id", clienteId)
                .getResultList();
    }

    public List<Visita> getVisitePerAgente(int agenteId) {
        Session session = database.getSession();
        return session.createSelectionQuery("from Visita v where v.agenteId = :id", Visita.class)
                .setParameter("id", agenteId)
                .getResultList();
    }

    public Visita getVisita(int visitaId){
        Session session = database.getSession();
        return session.createSelectionQuery("from Visita v where v.id = :id", Visita.class)
                .setParameter("id", visitaId)
                .getSingleResultOrNull();
    }

    public void conferma(int visitaId) {
        Session session = database.getSession();
        Visita visita = session.get(Visita.class, visitaId);
        if (visita == null) throw new IllegalArgumentException("Visita non trovata");
        visita.setStato(StatoVisita.CONFERMATA);
        session.merge(visita);
    }

    public void rifiuta(int visitaId) {
        Session session = database.getSession();
        Visita visita = session.get(Visita.class, visitaId);
        if (visita == null) throw new IllegalArgumentException("Visita non trovata");
        visita.setStato(StatoVisita.RIFIUTATA);
        session.merge(visita);
    }

}
