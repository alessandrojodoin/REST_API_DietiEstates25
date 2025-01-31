package it.unina.rest_api_dietiestates25;

import io.github.cdimascio.dotenv.Dotenv;
import it.unina.rest_api_dietiestates25.controller.AuthController;
import it.unina.rest_api_dietiestates25.model.*;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;


@Path("/hello-world")
public class HelloResource {
    public static void main(final String[] args){

        System.out.println("Hello World");
        

        SessionFactory sessionFactory =
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
                        // PostgreSQL
                        .setProperty(AvailableSettings.JAKARTA_JDBC_URL, "jdbc:postgresql://localhost:5432/")
                        // Credentials
                        .setProperty(AvailableSettings.JAKARTA_JDBC_USER, "postgres")
                        .setProperty(AvailableSettings.JAKARTA_JDBC_PASSWORD, "")
                        // Automatic schema export
                        .setProperty(AvailableSettings.JAKARTA_HBM2DDL_DATABASE_ACTION,
                                Action.SPEC_ACTION_DROP_AND_CREATE)
                        // SQL statement logging
                        .setProperty(AvailableSettings.SHOW_SQL, true)
                        .setProperty(AvailableSettings.FORMAT_SQL, true)
                        .setProperty(AvailableSettings.HIGHLIGHT_SQL, true)
                        // Create a new SessionFactory
                        .buildSessionFactory();


        AuthController authController = new AuthController(sessionFactory);
        authController.createCliente("Gennaro123", "gennaro@gennaro.it", "Gennaro", "Espostio", "password", "333-3333333");

    }
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }
}