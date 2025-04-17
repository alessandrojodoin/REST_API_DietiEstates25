package it.unina.rest_api_dietiestates25.controller;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.*;
import org.hibernate.Session;


import java.awt.image.BufferedImage;

public class ImmobileController {


    private final Session session = Database.getInstance().getSession();


    public Immobile createImmobile(String tipoImmobile, String latitudine, String longitudine, String indirizzo, String citta, String provincia){



        session.beginTransaction();
        Immobile immobile = new Immobile(tipoImmobile, latitudine, longitudine, indirizzo, citta, provincia);
        session.persist(immobile);
        session.getTransaction().commit();


        return immobile;
    }

    public void aggiungiIntegerTag(String nome, int valore, Immobile immobile){
        session.beginTransaction();
        IntegerTag tag= new IntegerTag(nome, valore);
        immobile.addTag(tag);
        session.persist(tag);
        session.merge(immobile);
        session.getTransaction().commit();
    }

    public void aggiungiGenericTag(String nome, Immobile immobile){

        session.beginTransaction();
        Tag tag = new Tag(nome);
        immobile.addTag(tag);
        session.persist(tag);
        session.merge(immobile);
        session.getTransaction().commit();

    }

    public void aggiungiCheckboxTag(String nome, boolean valore, Immobile immobile){

        session.beginTransaction();
        CheckboxTag tag= new CheckboxTag(nome, valore);
        immobile.addTag(tag);
        session.persist(tag);
        session.merge(immobile);
        session.getTransaction().commit();

    }

    public void aggiungiFloatTag(String nome, float valore, Immobile immobile){

        session.beginTransaction();
        FloatTag tag= new FloatTag(nome, valore);
        immobile.addTag(tag);
        session.persist(tag);
        session.merge(immobile);
        session.getTransaction().commit();

    }

    public void aggiungiStringTag(String nome, String valore, Immobile immobile){

        session.beginTransaction();
        StringTag tag= new StringTag(nome, valore);
        immobile.addTag(tag);
        session.persist(tag);
        session.merge(immobile);
        session.getTransaction().commit();

    }




    public Immobile getImmobile(int id){


        return session.createSelectionQuery("from Immobile where id = :id", Immobile.class)
                        .setParameter("id", id)
                        .getSingleResultOrNull();
    }

    public FotoImmobile getImage(int immobileId, int fotoId){


        return session.createSelectionQuery("from FotoImmobile where id = :id and immobile = :immobileId", FotoImmobile.class)
                .setParameter("id", fotoId)
                .setParameter("immobileId", immobileId)
                .getSingleResultOrNull();


    }

    public void addImage(Immobile immobile, BufferedImage image){

        session.beginTransaction();
        FotoImmobile fotoImmobile = new FotoImmobile(image, immobile);
        immobile.addFoto(fotoImmobile);
        session.persist(fotoImmobile);
        session.getTransaction().commit();

    }


}
