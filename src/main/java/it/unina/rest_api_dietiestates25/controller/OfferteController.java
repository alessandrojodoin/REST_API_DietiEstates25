package it.unina.rest_api_dietiestates25.controller;
import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.ListinoImmobile;
import it.unina.rest_api_dietiestates25.model.Offerta;
import it.unina.rest_api_dietiestates25.model.RiepilogoAttivita;
import it.unina.rest_api_dietiestates25.model.RisultatoOfferta;
import org.hibernate.Session;

public class OfferteController {

    private final Session session = Database.getInstance().getSession();

    public Offerta createOfferta(ListinoImmobile listino, RiepilogoAttivita riepilogo, String emailOfferente, String nome,
                         String cognome, String telefono, int cifraInCentesimi) {
        session.beginTransaction();
        Offerta offerta= new Offerta(listino, riepilogo, emailOfferente,nome, cognome, telefono,
                RisultatoOfferta.InRevisione, cifraInCentesimi,0);
        session.persist(offerta);
        session.getTransaction().commit();

        return offerta;
    }

    public Offerta createControOfferta(Offerta offerta, int cifraContropropostaInCentesimi){
        session.beginTransaction();
        offerta.setRisultatoOfferta(RisultatoOfferta.ContropropostaRicevuta);
        offerta.setCifraContropropostaInCentesimi(cifraContropropostaInCentesimi);
        session.merge(offerta);
        session.getTransaction().commit();

        return offerta;
    }

    public Offerta getOfferta(int id){
        Offerta offerta= session.createSelectionQuery("from Offerta where id = :id", Offerta.class)
                .setParameter("id",id)
                .getSingleResultOrNull();
        return offerta;

    }


    public Offerta setOffertaAccettata(Offerta offerta){
        session.beginTransaction();
        offerta.setRisultatoOfferta(RisultatoOfferta.Accettata);
        session.merge(offerta);
        session.getTransaction().commit();

        return offerta;
    }

    public Offerta setOffertaRifiutata(Offerta offerta){
        session.beginTransaction();
        offerta.setRisultatoOfferta(RisultatoOfferta.Rifiutata);
        session.merge(offerta);
        session.getTransaction().commit();

        return offerta;
    }
}
