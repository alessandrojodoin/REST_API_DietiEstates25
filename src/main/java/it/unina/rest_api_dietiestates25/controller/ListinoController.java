package it.unina.rest_api_dietiestates25.controller;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.*;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


import java.util.List;
import java.util.ArrayList;


import java.util.List;
import java.util.Set;

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


    private void addIntegerTagFilter(
            CriteriaBuilder cb,
            CriteriaQuery<?> cq,
            List<Predicate> predicates,
            Join<ListinoImmobile, Immobile> immobile,
            String tagName,
            Integer minValue
    ) {
        if (minValue == null) return;

        Subquery<Integer> sub = cq.subquery(Integer.class);
        Root<IntegerTag> tag = sub.from(IntegerTag.class);

        sub.select(tag.get("valore"))
                .where(
                        cb.and(
                                cb.equal(tag.get("immobile"), immobile),
                                cb.equal(tag.get("nome"), tagName),
                                cb.ge(tag.get("valore"), minValue)
                        )
                );

        predicates.add(cb.exists(sub));
    }


    private void addBooleanTagFilter(
            CriteriaBuilder cb,
            CriteriaQuery<?> cq,
            List<Predicate> predicates,
            Join<ListinoImmobile, Immobile> immobile,
            String tagName,
            Boolean value
    ) {
        if (value == null) return;

        Subquery<Long> sub = cq.subquery(Long.class);
        Root<Tag> tag = sub.from(Tag.class);

        sub.select(cb.literal(1L))
                .where(
                        cb.and(
                                cb.equal(tag.get("immobile"), immobile),
                                cb.equal(tag.get("nome"), tagName)
                        )
                );

        if (value) {
            predicates.add(cb.exists(sub));
        } else {
            predicates.add(cb.not(cb.exists(sub)));
        }
    }


    @PersistenceContext
    private EntityManager em;
    public List<ListinoImmobile> getImmobileListFiltri(Integer minPrice, Integer maxPrice, String propertyType, Integer bathrooms, Integer bedrooms, Integer areaSize, String energyClass, String citta,
                                                   Boolean Terrazzo, Boolean Balcone, Boolean Ascensore, Boolean Garage, Boolean Giardino, Boolean PostoAuto, Boolean AccessoDisabili
                                                  ) {

        Session session = database.getSession();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ListinoImmobile> cq = cb.createQuery(ListinoImmobile.class);
        Root<ListinoImmobile> listino = cq.from(ListinoImmobile.class);

        Join<ListinoImmobile, Immobile> immobile = listino.join("immobile");

        List<Predicate> predicates = new ArrayList<>();

        if (minPrice != null)
            predicates.add(cb.ge(listino.get("prezzo"), minPrice));

        if (maxPrice != null)
            predicates.add(cb.le(listino.get("prezzo"), maxPrice));

        if (propertyType != null)
            predicates.add(cb.equal(listino.get("tipologiaContratto"), propertyType));

        predicates.add(cb.isFalse(listino.get("isVenduto")));

        if (citta != null)
            predicates.add(cb.equal(immobile.get("citta"), citta));

        if (propertyType != null)
            predicates.add(cb.equal(immobile.get("tipoImmobile"), propertyType));

        addBooleanTagFilter(cb, cq, predicates, immobile, "Terrazzo", Terrazzo);
        addBooleanTagFilter(cb, cq, predicates, immobile, "Balcone", Balcone);
        addBooleanTagFilter(cb, cq, predicates, immobile, "Ascensore", Ascensore);
        addBooleanTagFilter(cb, cq, predicates, immobile, "Garage", Garage);
        addBooleanTagFilter(cb, cq, predicates, immobile, "Giardino", Giardino);
        addBooleanTagFilter(cb, cq, predicates, immobile, "PostoAuto", PostoAuto);
        addBooleanTagFilter(cb, cq, predicates, immobile, "AccessoDisabili", AccessoDisabili);

        addIntegerTagFilter(cb, cq, predicates, immobile, "Bagni", bathrooms);
        addIntegerTagFilter(cb, cq, predicates, immobile, "Camere", bedrooms);
        addIntegerTagFilter(cb, cq, predicates, immobile, "Superficie", areaSize);

        cq.distinct(true);
        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        return em.createQuery(cq).getResultList();

    }

    public void aggiungiVisualizzazione(int listinoId, String username){
        Session session = database.getSession();

        ListinoImmobile listino= getListino(listinoId);

        AuthController authController= new AuthController();
        Cliente cliente= authController.getCliente(username);

        RiepilogoAttivita riepilogo= cliente.getRiepilogo();
        VisualizzazioneImmobile newVisualizzazione = riepilogo.findVisualizzazione(listino);
        if(newVisualizzazione != null){
            newVisualizzazione.dateTime();
            session.merge(newVisualizzazione);
        }else{
            VisualizzazioneImmobile viewImmobile= new VisualizzazioneImmobile(listino, cliente);
            riepilogo.addVisualizzazione(viewImmobile);
            listino.setNumeroVisualizzazioni(listino.getNumeroVisualizzazioni()+1);

            session.persist(viewImmobile);
        }

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


    public void setImmobileToVenduto(int listinoId){
        Session session = database.getSession();
        ListinoImmobile listino= getListino(listinoId);
        listino.setVenduto(true);
        session.merge(listino);
    }

    public boolean esisteOffertaAccettata(ListinoImmobile listino){

        for( Offerta offerta: listino.getOfferte()){
            if(offerta.getRisultatoOfferta() == RisultatoOfferta.Accettata){
                return true;
            }
        }

        OfferteEsterneController offerteEsterneController = new OfferteEsterneController();

        for( Offerta offerta: offerteEsterneController.getOffertePerImmobile(listino.getId())){
            if(offerta.getRisultatoOfferta() == RisultatoOfferta.Accettata){
                return true;
            }
        }

        return false;
    }
}