package it.unina.rest_api_dietiestates25.controller;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.AgenteImmobiliare;
import it.unina.rest_api_dietiestates25.model.Immobile;
import it.unina.rest_api_dietiestates25.model.ListinoImmobile;
import org.hibernate.SessionFactory;
import org.hibernate.Session;

public class ListinoController {

    private final SessionFactory sessionFactory= Database.getInstance().getSessionFactory();


    public void createListino(Immobile immobile, String tipologiaContratto, int speseCondominiali, int prezzo, AgenteImmobiliare creatore){
        sessionFactory.inTransaction(session -> {
            ListinoImmobile listino= new ListinoImmobile(immobile,tipologiaContratto,speseCondominiali, prezzo, creatore);
            session.persist(listino);
        });
    }

    public ListinoImmobile getListino(int id){
        Session session= sessionFactory.openSession();
        ListinoImmobile listino= session.createSelectionQuery("from ListinoImmobile where id like :id", ListinoImmobile.class)
                .setParameter("id",id)
                .getSingleResultOrNull();
        session.close();
        return listino;
    }
}
