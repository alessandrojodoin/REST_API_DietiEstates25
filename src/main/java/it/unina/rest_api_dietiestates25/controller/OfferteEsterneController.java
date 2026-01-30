package it.unina.rest_api_dietiestates25.controller;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.*;
import org.hibernate.Session;

import java.util.List;

public class OfferteEsterneController {
    private Database database = Database.getInstance();

    public OfferteEsterne createOfferteEsterne(ListinoImmobile listino, String emailOfferente, String nome,
                                 String cognome, String telefono, int cifraInCentesimi) {
        Session session = database.getSession();

        OfferteEsterne offerta= new OfferteEsterne(listino, emailOfferente,nome, cognome, telefono,
                RisultatoOfferta.InRevisione, cifraInCentesimi);
        session.persist(offerta);

        return offerta;
    }

    public OfferteEsterne getOfferteEsterne(int id){
        Session session = database.getSession();
        OfferteEsterne offerta= session.createSelectionQuery("from OfferteEsterne where id = :id", OfferteEsterne.class)
                .setParameter("id",id)
                .getSingleResultOrNull();
        return offerta;

    }

    public OfferteEsterne setOfferteEsterneAccettata(OfferteEsterne offerta){
        Session session = database.getSession();
        offerta.setRisultatoOfferta(RisultatoOfferta.Accettata);
        session.merge(offerta);

        return offerta;
    }

    public OfferteEsterne setOfferteEsterneRifiutata(OfferteEsterne offerta){
        Session session = database.getSession();

        offerta.setRisultatoOfferta(RisultatoOfferta.Rifiutata);
        session.merge(offerta);

        return offerta;
    }

    public List<OfferteEsterne> getOffertePerImmobile(int immobileId){
        Session session = database.getSession();

        List<OfferteEsterne> offerte= session.createSelectionQuery("select o from OfferteEsterne o " +
                        "join o.listino r "+
                        "where r.id = :idImmobile", OfferteEsterne.class)
                .setParameter("idImmobile", immobileId)
                .getResultList();
        return offerte;
    }

}
