package it.unina.rest_api_dietiestates25;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import it.unina.rest_api_dietiestates25.model.*;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;


import java.io.IOException;
import java.net.URI;

@Path("/hello-world")
public class HelloResource {

    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://0.0.0.0:8080/api/1.0/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in it.unina.webtech package
        final ResourceConfig rc = new ResourceConfig().packages("it.unina.rest_api_dietiestates25");

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {return sessionFactory;}

    public static void main(final String[] args) throws IOException {

        System.out.println("Hello world");
        

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


        //AuthController authController = new AuthController(sessionFactory);
        //authController.createCliente("Gennaro123", "gennaro@gennaro.it", "Gennaro", "Espostio", "password", "333-3333333");

        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with endpoints available at "
                + "%s%nHit Ctrl-C to stop it...", BASE_URI));
        System.in.read();
        server.stop();


    }
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }
}