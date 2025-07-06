package it.unina.rest_api_dietiestates25;

import it.unina.rest_api_dietiestates25.router.filter.CORSFilter;
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


import javax.xml.crypto.Data;
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
        rc.register(new CORSFilter());
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }



    public static void main(final String[] args) throws IOException {

        System.out.println("Hello world");

        Database.getInstance();


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