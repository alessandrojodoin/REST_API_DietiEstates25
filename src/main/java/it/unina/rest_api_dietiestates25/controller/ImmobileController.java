package it.unina.rest_api_dietiestates25.controller;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.Session;

import java.awt.image.BufferedImage;

public class ImmobileController {


    private final SessionFactory sessionFactory= Database.getInstance().getSessionFactory();


    public void createImmobile(String nome, String posizioneGeografica, String indirizzo, String citta, String provincia){
        sessionFactory.inTransaction(session -> {
            Immobile immobile= new Immobile(nome, posizioneGeografica, indirizzo, citta, provincia);
            session.persist(immobile);
        });
    }

    public void aggiungiIntegerTag(String nome, int valore, Immobile immobile){
        sessionFactory.inTransaction(session -> {
            IntegerTag tag= new IntegerTag(nome, valore);
            immobile.getTags().add(tag);
            session.persist(tag);
            session.persist(immobile);
        });
    }

    public void aggiungiCheckboxTag(String nome, boolean valore, Immobile immobile){
        sessionFactory.inTransaction(session -> {
            CheckboxTag tag= new CheckboxTag(nome, valore);
            immobile.getTags().add(tag);
            session.persist(tag);
            session.persist(immobile);
        });
    }

    public void aggiungiFloatTag(String nome, float valore, Immobile immobile){
        sessionFactory.inTransaction(session -> {
            FloatTag tag= new FloatTag(nome, valore);
            immobile.getTags().add(tag);
            session.persist(tag);
            session.persist(immobile);
        });
    }

    public void aggiungiStringTag(String nome, String valore, Immobile immobile){
        sessionFactory.inTransaction(session -> {
            StringTag tag= new StringTag(nome, valore);
            immobile.getTags().add(tag);
            session.persist(tag);
            session.persist(immobile);
        });
    }




    public Immobile getImmobile(int id){
        Session session= sessionFactory.openSession();

        Immobile immobile= session.createSelectionQuery("from Immobile where id = :id", Immobile.class)
                        .setParameter("id", id)
                        .getSingleResultOrNull();
        session.close();

        return immobile;
    }

    public FotoImmobile getImage(int fotoId){
        Session session= sessionFactory.openSession();

        FotoImmobile fotoImmobile = session.createSelectionQuery("from FotoImmobile where id = :id", FotoImmobile.class)
                .setParameter("id", fotoId)
                .getSingleResultOrNull();
        session.close();

        return fotoImmobile;
    }

    public void addImage(Immobile immobile, BufferedImage image){
        sessionFactory.inTransaction(session -> {
            FotoImmobile fotoImmobile = new FotoImmobile(image, immobile);
            immobile.addFoto(fotoImmobile);
            session.persist(fotoImmobile);
        });
    }


}
