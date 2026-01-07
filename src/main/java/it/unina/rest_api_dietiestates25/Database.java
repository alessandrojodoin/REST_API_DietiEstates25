package it.unina.rest_api_dietiestates25;

import it.unina.rest_api_dietiestates25.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;

public class Database {

    private static Database database = null;
    private final SessionFactory sessionFactory;
    private Session session;


    private Database() {
        sessionFactory =
                new Configuration()
                        .addAnnotatedClass(Utente.class)
                        .addAnnotatedClass(Cliente.class)
                        .addAnnotatedClass(RiepilogoAttivita.class)
                        .addAnnotatedClass(Offerta.class)
                        .addAnnotatedClass(VisualizzazioneImmobile.class)
                        .addAnnotatedClass(ListinoImmobile.class)
                        .addAnnotatedClass(Immobile.class)
                        .addAnnotatedClass(Tag.class)
                        .addAnnotatedClass(StringTag.class)
                        .addAnnotatedClass(FloatTag.class)
                        .addAnnotatedClass(CheckboxTag.class)
                        .addAnnotatedClass(IntegerTag.class)
                        .addAnnotatedClass(AgenteImmobiliare.class)
                        .addAnnotatedClass(AmministratoreAgenzia.class)
                        .addAnnotatedClass(ClienteGoogle.class)
                        .addAnnotatedClass(FotoImmobile.class)
                        // PostgreSQL
                        .setProperty(AvailableSettings.JAKARTA_JDBC_URL, System.getenv("DATABASE_URL"))
                        // Credentials
                        .setProperty(AvailableSettings.JAKARTA_JDBC_USER, System.getenv("DATABASE_USERNAME"))
                        .setProperty(AvailableSettings.JAKARTA_JDBC_PASSWORD, System.getenv("DATABASE_PASSWORD"))
                        // Automatic schema export
                        .setProperty(AvailableSettings.JAKARTA_HBM2DDL_DATABASE_ACTION,
                                Action.SPEC_ACTION_DROP_AND_CREATE)
                        // SQL statement logging
                        .setProperty(AvailableSettings.SHOW_SQL, true)
                        .setProperty(AvailableSettings.FORMAT_SQL, true)
                        .setProperty(AvailableSettings.HIGHLIGHT_SQL, true)
                        // Create a new SessionFactory
                        .buildSessionFactory();



    }

    public static Database getInstance() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    public Session getSession() {
        if(session == null) {
            session = sessionFactory.openSession();
        }

        return session;
    }



    public SessionFactory getSessionFactory() {return sessionFactory;}
}
