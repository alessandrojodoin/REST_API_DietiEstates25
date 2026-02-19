package it.unina.rest_api_dietiestates25.controller;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.*;
import jakarta.transaction.Transactional;
import org.hibernate.Session;


import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;


public class ImmobileController {

private final Database database = Database.getInstance();



    public Immobile createImmobile(String tipoImmobile, String latitudine, String longitudine, String indirizzo, String citta, String provincia){
        Session session = database.getSession();
        Immobile immobile = new Immobile(tipoImmobile, latitudine, longitudine, indirizzo, citta, provincia);
        session.persist(immobile);

        return immobile;
    }
@Transactional
    public void aggiungiIntegerTag(String nome, int valore, Immobile immobile){
        Session session = database.getSession();
        IntegerTag tag= new IntegerTag(nome, valore);
        immobile.addTag(tag);
        session.persist(tag);
        session.merge(immobile);

    }

    public void aggiungiGenericTag(String nome, Immobile immobile){
        Session session = database.getSession();
        Tag tag = new Tag(nome);
        immobile.addTag(tag);
        session.persist(tag);
        session.merge(immobile);

    }

    public void aggiungiCheckboxTag(String nome, boolean valore, Immobile immobile){
        Session session = database.getSession();
        CheckboxTag tag= new CheckboxTag(nome, valore);
        immobile.addTag(tag);
        session.persist(tag);
        session.merge(immobile);


    }

    public void aggiungiFloatTag(String nome, float valore, Immobile immobile){
        Session session = database.getSession();
        FloatTag tag= new FloatTag(nome, valore);
        immobile.addTag(tag);
        session.persist(tag);
        session.merge(immobile);

    }

    public void aggiungiStringTag(String nome, String valore, Immobile immobile){
        Session session = database.getSession();
        StringTag tag= new StringTag(nome, valore);
        immobile.addTag(tag);
        session.persist(tag);
        session.merge(immobile);

    }


    public Immobile getImmobile(int id){
        Session session = database.getSession();

        return session.createSelectionQuery("from Immobile where id = :id", Immobile.class)
                        .setParameter("id", id)
                        .getSingleResultOrNull();
    }

    public FotoImmobile getImage(int immobileId, int fotoId){
        Session session = database.getSession();
        return session.createSelectionQuery("from FotoImmobile where id = :id and immobile = :immobileId", FotoImmobile.class)
                .setParameter("id", fotoId)
                .setParameter("immobileId", getImmobile(immobileId))
                .getSingleResultOrNull();

    }

    public void addImage(Immobile immobile, BufferedImage bufferedImage) {
        Session session = database.getSession();
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = writers.next();

            ImageWriteParam param = writer.getDefaultWriteParam();
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.7f); // 70% qualità

            writer.setOutput(new MemoryCacheImageOutputStream(outputStream));
            writer.write(null, new IIOImage(bufferedImage, null, null), param);
            writer.dispose();

            byte[] compressedImage = outputStream.toByteArray();

            FotoImmobile foto = new FotoImmobile(compressedImage, immobile);
            immobile.addFoto(foto);

            session.persist(foto);
            session.merge(immobile);
        } catch (Exception e) {
            throw new RuntimeException("Errore compressione immagine", e);
        }
    }



        }
