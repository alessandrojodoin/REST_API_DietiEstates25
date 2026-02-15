package it.unina.rest_api_dietiestates25.controller;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.*;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.List;

public class VisiteController {

    private final Database database = Database.getInstance();

    /**
     * Prenota una visita per un immobile.
     * Controlla fascia oraria, limite 2 settimane e disponibilità slot.
     */
    public Visita prenota(Cliente cliente, int immobileId, LocalDateTime dataOra) {
        Session session = database.getSession();

        // Recupera il listino associato all'immobile
        ListinoImmobile listino = session.createSelectionQuery(
                        "from ListinoImmobile l where l.immobile.id = :id",
                        ListinoImmobile.class
                )
                .setParameter("id", immobileId)
                .getSingleResultOrNull();

        if (listino == null)
            throw new IllegalArgumentException("Listino non trovato per immobileId: " + immobileId);

        int agenteId = listino.getCreatore().getId();

        // Controllo data entro 2 settimane
        if (dataOra.isAfter(LocalDateTime.now().plusWeeks(2)))
            throw new IllegalArgumentException("La visita deve essere prenotata entro 2 settimane");

        // Controllo fascia oraria: 9-12 o 15-19
        int ora = dataOra.getHour();
        if (!((ora >= 9 && ora < 13) || (ora >= 14 && ora < 19)))
            throw new IllegalArgumentException("Orario della visita non valido. Fasce disponibili: 9-12, 15-19");

        // Controllo disponibilità slot
        if (!isSlotDisponibile(agenteId, dataOra, session))
            throw new IllegalArgumentException("Slot già prenotato");

        // Crea la visita
        Visita visita = new Visita();
        visita.setImmobileId(immobileId);
        visita.setClienteId(cliente.getId());
        visita.setAgenteId(agenteId);
        visita.setDataOra(dataOra);
        visita.setStato(StatoVisita.RICHIESTA);
        visita.setCreataIl(LocalDateTime.now());

        session.persist(visita);
        return visita;
    }

    /**
     * Controlla se lo slot del giorno/ora è disponibile per l'agente.
     */
    public boolean isSlotDisponibile(int agenteId, LocalDateTime dataOra, Session session) {
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
