package it.unina.rest_api_dietiestates25.controller;
import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.*;
import org.hibernate.Session;

import java.util.List;

public class OfferteController {

    private final Database database = Database.getInstance();

    public OffertaUtente createOfferta(ListinoImmobile listino, RiepilogoAttivita riepilogo, String emailOfferente, String nome,
                                       String cognome, String telefono, int cifraInCentesimi) {
        Session session = database.getSession();

        OffertaUtente offertaUtente = new OffertaUtente(listino, riepilogo, emailOfferente,nome, cognome, telefono,
                RisultatoOfferta.InRevisione, cifraInCentesimi,0);
        session.persist(offertaUtente);

        return offertaUtente;
    }

    public void createControOfferta(OffertaUtente offertaUtente, int cifraContropropostaInCentesimi){
        Session session = database.getSession();
        offertaUtente.setRisultatoOfferta(RisultatoOfferta.ContropropostaRicevuta);
        offertaUtente.setCifraContropropostaInCentesimi(cifraContropropostaInCentesimi);
        session.merge(offertaUtente);

    }

    public Offerta getOfferta(int id){
        Session session = database.getSession();
        return session.createSelectionQuery("from Offerta where id = :id", Offerta.class)
                .setParameter("id",id)
                .getSingleResultOrNull();

    }

    public OffertaUtente getOffertaUtente(int id){
        Session session = database.getSession();
        return session.createSelectionQuery("from OffertaUtente where id = :id", OffertaUtente.class)
                .setParameter("id",id)
                .getSingleResultOrNull();


    }

    public void setOffertaAccettata(Offerta offerta){
        Session session = database.getSession();
        offerta.setRisultatoOfferta(RisultatoOfferta.Accettata);
        session.merge(offerta);

    }


    public void annullaAccettazione(Offerta offerta){
        Session session = database.getSession();
        offerta.setRisultatoOfferta(RisultatoOfferta.InRevisione);
        session.merge(offerta);


    }

    public void setOffertaRifiutata(Offerta offerta){
        Session session = database.getSession();

        offerta.setRisultatoOfferta(RisultatoOfferta.Rifiutata);
        session.merge(offerta);

    }


    public List<OffertaUtente> getOffertePerCliente(int clienteId){
        Session session = database.getSession();

         return session.createSelectionQuery("select o from OffertaUtente o " +
                         "join o.riepilogo r " +
                         "join r.cliente c " +
                         "where c.id = :idCliente", OffertaUtente.class)
                .setParameter("idCliente", clienteId)
                .getResultList();

    }

    public List<OffertaUtente> getOffertePerImmobile(int immobileId){
        Session session = database.getSession();

        return session.createSelectionQuery("select o from OffertaUtente o " +
                        "join o.listino r "+
                        "where r.id = :idImmobile", OffertaUtente.class)
                .setParameter("idImmobile", immobileId)
                .getResultList();

    }

    public void deleteOfferta(int offertaId){
        Session session = database.getSession();

        session.remove(this.getOfferta(offertaId));

    }


}
