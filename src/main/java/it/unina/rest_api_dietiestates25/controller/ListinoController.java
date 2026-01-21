package it.unina.rest_api_dietiestates25.controller;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.AgenteImmobiliare;
import it.unina.rest_api_dietiestates25.model.Immobile;
import it.unina.rest_api_dietiestates25.model.ListinoImmobile;
import org.hibernate.SessionFactory;
import org.hibernate.Session;

import java.util.List;

public class ListinoController {

    private Database database = Database.getInstance();


    public ListinoImmobile createListino(Immobile immobile, String nome, String descrizione, String tipologiaContratto, int speseCondominiali, int prezzo, AgenteImmobiliare creatore){
        Session session = database.getSession();
        ListinoImmobile listino= new ListinoImmobile(immobile, nome, descrizione, tipologiaContratto,speseCondominiali, prezzo, creatore);
        session.persist(listino);


        return listino;

    }

    public ListinoImmobile getListino(int id){
        Session session = database.getSession();
        ListinoImmobile listino= session.createSelectionQuery("from ListinoImmobile where id = :id", ListinoImmobile.class)
                .setParameter("id",id)
                .getSingleResultOrNull();
        return listino;
    }


    public List<ListinoImmobile> getImmobileListPerAgente(int agenteId){
        Session session = database.getSession();

        return session.createSelectionQuery("from ListinoImmobile L where L.creatore.id= :id", ListinoImmobile.class)
                .setParameter("id", agenteId)
                .getResultList();
    }

    public List<ListinoImmobile> getImmobileList(){
        Session session = database.getSession();

        return session.createSelectionQuery("from ListinoImmobile", ListinoImmobile.class)
                .getResultList();
    }
}
