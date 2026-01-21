package it.unina.rest_api_dietiestates25.controller;
import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.*;
import org.hibernate.Session;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class OfferteController {

    private Database database = Database.getInstance();

    public Offerta createOfferta(ListinoImmobile listino, RiepilogoAttivita riepilogo, String emailOfferente, String nome,
                         String cognome, String telefono, int cifraInCentesimi) {
        Session session = database.getSession();

        Offerta offerta= new Offerta(listino, riepilogo, emailOfferente,nome, cognome, telefono,
                RisultatoOfferta.InRevisione, cifraInCentesimi,0);
        session.persist(offerta);

        return offerta;
    }

    public Offerta createControOfferta(Offerta offerta, int cifraContropropostaInCentesimi){
        Session session = database.getSession();
        offerta.setRisultatoOfferta(RisultatoOfferta.ContropropostaRicevuta);
        offerta.setCifraContropropostaInCentesimi(cifraContropropostaInCentesimi);
        session.merge(offerta);

        return offerta;
    }

    public Offerta getOfferta(int id){
        Session session = database.getSession();
        Offerta offerta= session.createSelectionQuery("from Offerta where id = :id", Offerta.class)
                .setParameter("id",id)
                .getSingleResultOrNull();
        return offerta;

    }

    public Offerta setOffertaAccettata(Offerta offerta){
        Session session = database.getSession();
        offerta.setRisultatoOfferta(RisultatoOfferta.Accettata);
        session.merge(offerta);

        return offerta;
    }

    public Offerta setOffertaRifiutata(Offerta offerta){
        Session session = database.getSession();

        offerta.setRisultatoOfferta(RisultatoOfferta.Rifiutata);
        session.merge(offerta);


        return offerta;
    }


    public List<Offerta> getOffertePerCliente(int clienteId){
        Session session = database.getSession();

         List<Offerta> offerte= session.createSelectionQuery("select o from Offerta o " +
                         "join o.riepilogo r " +
                         "join r.cliente c " +
                         "where c.id = :idCliente", Offerta.class)
                .setParameter("idCliente", clienteId)
                .getResultList();
         return offerte;

    }

    public List<Offerta> getOffertePerImmobile(int immobileId){
        Session session = database.getSession();

        List<Offerta> offerte= session.createSelectionQuery("select o from Offerta o " +
                        "join o.listino r "+
                        "where r.id = :idImmobile", Offerta.class)
                .setParameter("idImmobile", immobileId)
                .getResultList();
        return offerte;
    }





}
