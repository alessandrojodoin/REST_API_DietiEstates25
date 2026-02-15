package it.unina.rest_api_dietiestates25.router;

import it.unina.rest_api_dietiestates25.controller.VisiteController;
import it.unina.rest_api_dietiestates25.model.*;
import it.unina.rest_api_dietiestates25.router.filter.RequireAgenteImmobiliareAuthentication;
import it.unina.rest_api_dietiestates25.router.filter.RequireClienteAuthentication;
import jakarta.json.JsonArrayBuilder;
import jakarta.ws.rs.Path;


import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.controller.AuthController;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;



@Path("/prenotazioni")
public class VisiteRouter {
    @Context
    private ContainerRequestContext ctx;

    private final Database database = Database.getInstance();

    /* ------------------- CLIENTE ------------------- */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RequireClienteAuthentication
    public Response creaPrenotazione(JsonObject jsonPrenotazione) {
        database.openSession();
        Session session = database.getSession();
        Transaction tx = session.beginTransaction();

        try {
            VisiteController visitaController = new VisiteController();
            AuthController authController = new AuthController();

            // Recupera il cliente autenticato
            String username = (String) ctx.getProperty("username");
            Cliente cliente = authController.getCliente(username);

            int immobileId = jsonPrenotazione.getInt("immobileId");
            String dataOraStr = jsonPrenotazione.getString("dataOra"); // es. "2026-02-14T15:30"
            LocalDateTime dataOra = LocalDateTime.parse(dataOraStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);



            // Crea la visita
            Visita visita = visitaController.prenota(cliente, immobileId, dataOra);

            // TODO: invio email all'agente (puoi usare JavaMail o un service esterno)
            // EmailService.sendEmail(agenteEmail, "Nuova visita prenotata", ...);

            tx.commit();
            database.closeSession();

            JsonObject responseJson = Json.createObjectBuilder()
                    .add("message", "Visita prenotata con successo")
                    .add("visitaId", visita.getId())
                    .build();

            return Response.status(Response.Status.CREATED)
                    .entity(responseJson)
                    .build();

        } catch (Exception e) {
            tx.commit();
            database.closeSession();
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Json.createObjectBuilder()
                            .add("error", e.getMessage())
                            .build())
                    .build();
        }
    }


    @GET
    @Path("/mie")
    @Produces(MediaType.APPLICATION_JSON)
    @RequireClienteAuthentication
    public Response getMieVisite() {
        database.openSession();
        Session session = database.getSession();
        Transaction tx = session.beginTransaction();

        try {
            VisiteController visiteController = new VisiteController();
            AuthController authController = new AuthController();

            String username = (String) ctx.getProperty("username");
            Cliente cliente = authController.getCliente(username);

            List<Visita> visite = visiteController.getVisitePerCliente(cliente.getId());

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (Visita v : visite) {
                arrayBuilder.add(Json.createObjectBuilder()
                        .add("id", v.getId())
                        .add("clienteId", v.getClienteId())
                        .add("immobileId", v.getImmobileId())
                        .add("agenteId", v.getAgenteId())
                        .add("dataOra", v.getDataOra().toString())
                        .add("stato", v.getStato().toString())
                        .add("creataIl", v.getCreataIl().toString())
                );
            }

            tx.commit();
            database.closeSession();

            return Response.ok(arrayBuilder.build()).build();
        } catch (Exception e) {
            tx.rollback();
            database.closeSession();
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Json.createObjectBuilder().add("error", e.getMessage()).build())
                    .build();
        }
    }

    /* ------------------- AGENTE ------------------- */


    @GET
    @Path("/agente")
    @Produces(MediaType.APPLICATION_JSON)
    @RequireAgenteImmobiliareAuthentication
    public Response getVisiteAgente() {
        database.openSession();
        Session session = database.getSession();
        Transaction tx = session.beginTransaction();

        try {
            VisiteController visiteController = new VisiteController();
            AuthController authController = new AuthController();

            String username = (String) ctx.getProperty("username");
            AgenteImmobiliare agente = authController.getAgenteImmobiliare(username);

            List<Visita> visite = visiteController.getVisitePerAgente(agente.getId());

            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (Visita v : visite) {
                arrayBuilder.add(Json.createObjectBuilder()
                        .add("id", v.getId())
                        .add("immobileId", v.getImmobileId())
                        .add("clienteId", v.getClienteId())
                        .add("dataOra", v.getDataOra().toString())
                        .add("stato", v.getStato().toString())
                        .add("creataIl", v.getCreataIl().toString())
                );
            }

            tx.commit();
            database.closeSession();

            return Response.ok(arrayBuilder.build()).build();
        } catch (Exception e) {
            tx.rollback();
            database.closeSession();
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Json.createObjectBuilder().add("error", e.getMessage()).build())
                    .build();
        }
    }


    @PUT
    @Path("{id}/conferma")
    @RequireAgenteImmobiliareAuthentication
    public Response confermaVisita(@PathParam("id") int visitaId) {
        database.openSession();
        Session session = database.getSession();
        Transaction tx = session.beginTransaction();

        try {
            VisiteController visiteController = new VisiteController();

            visiteController.conferma(visitaId);

            tx.commit();
            database.closeSession();

            return Response.ok(Json.createObjectBuilder().add("message", "Visita confermata").build()).build();
        } catch (Exception e) {
            tx.rollback();
            database.closeSession();
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Json.createObjectBuilder().add("error", e.getMessage()).build())
                    .build();
        }
    }


    @PUT
    @Path("{id}/rifiuta")
    @RequireAgenteImmobiliareAuthentication
    public Response rifiutaVisita(@PathParam("id") int visitaId) {
        database.openSession();
        Session session = database.getSession();
        Transaction tx = session.beginTransaction();

        try {
            VisiteController visiteController = new VisiteController();

            visiteController.rifiuta(visitaId);

            tx.commit();
            database.closeSession();

            return Response.ok(Json.createObjectBuilder().add("message", "Visita rifiutata").build()).build();
        } catch (Exception e) {
            tx.rollback();
            database.closeSession();
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Json.createObjectBuilder().add("error", e.getMessage()).build())
                    .build();
        }
    }


}
