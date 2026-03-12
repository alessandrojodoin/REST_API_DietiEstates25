package it.unina.rest_api_dietiestates25.controller;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.*;
import org.hibernate.Session;

import org.hibernate.query.Query;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class ListinoController {



    private final Database database;
    private final AuthController authController;

    // Costruttore di default
    public ListinoController() {
        this.database = Database.getInstance();
        this.authController = new AuthController();
    }

    // Costruttore per test
    public ListinoController(Database database, AuthController authController) {
        this.database = database;
        this.authController = authController;
    }


    public void createListino(Immobile immobile, String nome, String descrizione, String tipologiaContratto, int speseCondominiali, int prezzo, AgenteImmobiliare creatore){
        Session session = database.getSession();
        ListinoImmobile listino= new ListinoImmobile(immobile, nome, descrizione, tipologiaContratto,speseCondominiali, prezzo, creatore);
        session.persist(listino);

    }

    public ListinoImmobile getListino(int id){
        Session session = database.getSession();
        return session.createSelectionQuery("from ListinoImmobile where id = :id", ListinoImmobile.class)
                .setParameter("id",id)
                .getSingleResultOrNull();

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


    private void addIntegerTagFilter(StringBuilder hql,
                                     Map<String, Object> params,
                                     String tagName,
                                     Integer value) {

        if (value != null) {
            String safeTag = tagName.replaceAll("\\s+", "_");

            String paramNameName = "tagName_" + safeTag;
            String paramNameValue = "tagValue_" + safeTag;

            hql.append(" AND EXISTS (")
                    .append("SELECT t.id FROM IntegerTag t ")
                    .append("WHERE t.immobile = i ")
                    .append("AND t.nome = :").append(paramNameName).append(" ")
                    .append("AND cast(t.valore as integer) >= :").append(paramNameValue)
                    .append(") ");

            params.put(paramNameName, tagName);
            params.put(paramNameValue, value);
        }
    }


    private void addBooleanTagFilter(StringBuilder hql,
                                     Map<String, Object> params,
                                     String tagName,
                                     Boolean value) {

        if (value != null && value) {
            String safeTag = tagName.replaceAll("\\s+", "_");

            String paramName = "tag_" + safeTag;
            String paramValue = "tagValue_"+ safeTag;


            hql.append(" AND EXISTS (")
                    .append("SELECT t.id FROM CheckboxTag t ")
                    .append("WHERE t.immobile = i ")
                    .append("AND t.nome = :").append(paramName).append(" ")
                    .append("AND cast(t.valore as boolean) = :").append(paramValue)
                    .append(") ");

            params.put(paramName, tagName);
            params.put(paramValue, value);
        }
    }

    private void addOtherTags(StringBuilder hql,
                              Map<String, Object> params,
                              String tagName, String value){
        if (value != null) {

            String safeTag = tagName.replaceAll("\\s+", "_");

            String paramName = "tag_" + safeTag;
            String paramNameValue = "tagValue_" + safeTag;
            hql.append(" AND EXISTS (")
                    .append("SELECT t.id FROM StringTag t ")
                    .append("WHERE t.immobile = i ")
                    .append("AND t.nome = :").append(paramName).append(" ")
                    .append("AND t.valore = :").append(paramNameValue)
                    .append(") ");

            params.put(paramName, tagName);
            params.put(paramNameValue, value);
        }
    }

    public List<ListinoImmobile> getImmobileListFiltri(
            Integer minPrice,
            Integer maxPrice,
            String tipologiaContratto,
            Integer bathrooms,
            Integer bedrooms,
            Integer areaSize,
            String energyClass,
            String citta,
            Boolean terrazzo,
            Boolean balcone,
            Boolean ascensore,
            Boolean garage,
            Boolean giardino,
            Boolean postoAuto,
            Boolean accessoDisabili
    ) {

        Session session = database.getSession();

            StringBuilder hql = new StringBuilder(
                    "SELECT DISTINCT l FROM ListinoImmobile l " +
                            "JOIN l.immobile i " +
                            "WHERE l.isVenduto = false "
            );

            Map<String, Object> params = new HashMap<>();

            if (minPrice != null) {
                hql.append(" AND l.prezzo >= :minPrice ");
                params.put("minPrice", minPrice);
            }

            if (maxPrice != null) {
                hql.append(" AND l.prezzo <= :maxPrice ");
                params.put("maxPrice", maxPrice);
            }

            if (citta != null) {
                hql.append(" AND i.citta = :citta ");
                params.put("citta", citta);
            }

            if (tipologiaContratto != null) {
                hql.append(" AND l.tipologiaContratto = :tipologiaContratto ");
                params.put("tipologiaContratto", tipologiaContratto);
            }


            addOtherTags(hql, params, "energyClass", energyClass);
            // -------- BOOLEAN TAG --------

            addBooleanTagFilter(hql, params, "terrazzo", terrazzo);
            addBooleanTagFilter(hql, params, "balcone", balcone);
            addBooleanTagFilter(hql, params, "ascensore", ascensore);
            addBooleanTagFilter(hql, params, "garage", garage);
            addBooleanTagFilter(hql, params, "giardino", giardino);
            addBooleanTagFilter(hql, params, "postoAuto", postoAuto);
            addBooleanTagFilter(hql, params, "accessoDisabili", accessoDisabili);

            // -------- INTEGER TAG --------

            addIntegerTagFilter(hql, params, "Bagni", bathrooms);
            addIntegerTagFilter(hql, params, "Camere", bedrooms);
            addIntegerTagFilter(hql, params, "Superficie", areaSize);

            Query<ListinoImmobile> query =
                    session.createQuery(hql.toString(), ListinoImmobile.class);

            params.forEach(query::setParameter);

            return query.getResultList();

    }


    public void aggiungiVisualizzazione(int listinoId, String username){
        Session session = database.getSession();

        if(listinoId <= 0){
            throw new IllegalArgumentException("Listino non trovato");
        }
        if(username == null || username.equals("")){
            throw new IllegalArgumentException("Utente non trovato");
        }
        ListinoImmobile listino= getListino(listinoId);
        if(listino == null){
            throw new IllegalArgumentException("Immobile non trovato");
        }


        Cliente cliente= this.authController.getCliente(username);
        if(cliente == null){
            throw new IllegalArgumentException("Utente non trovato");
        }

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


    public Instant getIstanteVisualizzazione(ListinoImmobile listino, Cliente cliente){
        Session session = database.getSession();

        return session.createSelectionQuery("select V.istanteVisualizzazione from VisualizzazioneImmobile V "
                        + "where V.cliente = :cliente "
                            + "and V.immobile = :listino", Instant.class)
                .setParameter("cliente", cliente)
                .setParameter("listino", listino)
                .getSingleResultOrNull();

    }

    public void setImmobileToVenduto(int listinoId){
        Session session = database.getSession();
        ListinoImmobile listino= getListino(listinoId);
        listino.setVenduto(true);
        session.merge(listino);
    }

    public boolean esisteOffertaAccettata(ListinoImmobile listino){

        for( Offerta offerta: listino.getOfferte()){
            if(offerta.getRisultatoOfferta() == RisultatoOfferta.ACCETTATA){
                return true;
            }
        }

        OfferteEsterneController offerteEsterneController = new OfferteEsterneController();

        for( Offerta offerta: offerteEsterneController.getOffertePerImmobile(listino.getId())){
            if(offerta.getRisultatoOfferta() == RisultatoOfferta.ACCETTATA){
                return true;
            }
        }

        return false;
    }
}