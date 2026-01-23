package it.unina.rest_api_dietiestates25.controller;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.*;
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

    public void aggiungiVisualizzazione(int listinoId, String username){
        Session session = database.getSession();

        ListinoImmobile listino= getListino(listinoId);
        listino.setNumeroVisualizzazioni(listino.getNumeroVisualizzazioni()+1);

        AuthController authController= new AuthController();
        Cliente cliente= authController.getCliente(username);

        RiepilogoAttivita riepilogo= cliente.getRiepilogo();
        VisualizzazioneImmobile viewImmobile= new VisualizzazioneImmobile(listino, cliente);
        riepilogo.addVisualizzazione(viewImmobile);

        session.persist(viewImmobile);
        session.merge(cliente);
        session.merge(riepilogo);
        session.persist(listino);
    }


    public List<ListinoImmobile> getImmobileListPerCliente(int clienteId){
        Session session = database.getSession();

        return session.createSelectionQuery("select L from VisualizzazioneImmobile V "
                                              + "join V.immobile L "
                                              + "where V.cliente.id = :id", ListinoImmobile.class)
                .setParameter("id", clienteId)
                .getResultList();
    }

}