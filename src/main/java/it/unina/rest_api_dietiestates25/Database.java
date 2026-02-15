package it.unina.rest_api_dietiestates25;

import it.unina.rest_api_dietiestates25.model.*;
import jakarta.enterprise.context.ApplicationScoped;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.JdbcSettings;


@ApplicationScoped
public class Database {

    private static Database database = null;
    private final SessionFactory sessionFactory;
    //private Session session;


    private Database() {
        System.out.println(System.getenv("DATABASE_URL"));
        sessionFactory =
                new Configuration()
                        .addAnnotatedClass(Utente.class)
                        .addAnnotatedClass(Cliente.class)
                        .addAnnotatedClass(RiepilogoAttivita.class)
                        .addAnnotatedClass(OffertaUtente.class)
                        .addAnnotatedClass(Offerta.class)
                        .addAnnotatedClass(VisualizzazioneImmobile.class)
                        .addAnnotatedClass(Visita.class)
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
                        .addAnnotatedClass(AccountSupporto.class)
                        // PostgreSQL
                        .setProperty(JdbcSettings.JAKARTA_JDBC_URL, System.getenv("DATABASE_URL"))
                        // Credentials
                        .setProperty(JdbcSettings.JAKARTA_JDBC_USER, System.getenv("DATABASE_USERNAME"))
                        .setProperty(JdbcSettings.JAKARTA_JDBC_PASSWORD, System.getenv("DATABASE_PASSWORD"))
                        // Automatic schema export
                        //.setProperty(AvailableSettings.JAKARTA_HBM2DDL_DATABASE_ACTION,
                          //   Action.SPEC_ACTION_DROP_AND_CREATE)
                        // SQL statement logging
                        .setProperty(JdbcSettings.SHOW_SQL, true)
                        .setProperty(JdbcSettings.FORMAT_SQL, true)
                        .setProperty(JdbcSettings.HIGHLIGHT_SQL, true)
                        // Create a new SessionFactory
                        .buildSessionFactory();



    }

    public static Database getInstance() {
        if (database == null) {
            database = new Database();
        }
        return database;
    }

    private final ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();

    public void openSession() {
        sessionThreadLocal.set(sessionFactory.openSession());
    }

    public Session getSession() {
        return sessionThreadLocal.get();
    }

    public void closeSession() {
        Session s = sessionThreadLocal.get();
        if (s != null) s.close();
        sessionThreadLocal.remove();
    }


}
