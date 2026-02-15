package it.unina.rest_api_dietiestates25.controller;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.*;
import org.hibernate.Session;

import java.util.List;

public class OfferteEsterneController {
    private final Database database = Database.getInstance();

    public Offerta createOfferteEsterne(ListinoImmobile listino, String emailOfferente, String nome,
                                        String cognome, String telefono, int cifraInCentesimi) {
        Session session = database.getSession();

        Offerta offerta= new Offerta(listino, emailOfferente,nome, cognome, telefono,
                RisultatoOfferta.IN_REVISIONE, cifraInCentesimi);
        session.persist(offerta);

        return offerta;
    }

    public List<Offerta> getOffertePerImmobile(int immobileId){
        Session session = database.getSession();


        return session.createSelectionQuery("select o from Offerta o " +
                        "join o.listino r "+
                        "where r.id = :idImmobile and o.riepilogo is null", Offerta.class)
                .setParameter("idImmobile", immobileId)
                .getResultList();

    }

}
