package it.unina.rest_api_dietiestates25.router;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.controller.AuthController;
import it.unina.rest_api_dietiestates25.controller.ImmobileController;
import it.unina.rest_api_dietiestates25.controller.ListinoController;
import it.unina.rest_api_dietiestates25.controller.OfferteController;
import it.unina.rest_api_dietiestates25.model.*;
import it.unina.rest_api_dietiestates25.router.filter.RequireClienteAuthentication;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Path("/offerte")
public class OffertaRouter {
    @Context
    private ContainerRequestContext ctx;
    private final Database database = Database.getInstance();

    @GET
    @Path("{offerteId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOfferte(@PathParam("offerteId") int offerteId){
        OfferteController offertaController= new OfferteController();
        Offerta offerta = offertaController.getOfferta(offerteId);
/*
        if (risultato != null) {
            offertaJsonBuilder.add("risultatoOfferta", risultato.toString());
        } else {
            offertaJsonBuilder.add("risultatoOfferta", JsonValue.NULL);
        }
*/
        JsonObject jsonResponse = Json.createObjectBuilder()
                .add("id", offerta.getId())
                .add("emailOfferente", offerta.getEmailOfferente())
                .add("nome", offerta.getNome())
                .add("cognome", offerta.getCognome())
                .add("telefono", offerta.getTelefono())
                .add("cifraInCentesimi", offerta.getCifraInCentesimi())
                .add("cifraContropropostaInCentesimi", offerta.getCifraContropropostaInCentesimi())
                .add("idListino", offerta.getListino().getId())
                .add("idRiepilogo", offerta.getRiepilogo().getId())
                .add("risultatoOfferta", offerta.getRisultatoOfferta().toString())
                .build();
        return Response
                .status(Response.Status.OK)
                .entity(jsonResponse)
                .build();
    }

    @Context
    private HttpHeaders headers;
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RequireClienteAuthentication
    public Response creaOfferta(JsonObject jsonOfferta) {
        database.openSession();
        Session session= database.getSession();

        Transaction tx = session.beginTransaction();

        OfferteController offerteController= new OfferteController();
        //ImmobileController immobileController= new ImmobileController();
        AuthController authController = new AuthController();
        ListinoController listinoController= new ListinoController();

        int listinoId = jsonOfferta.getInt("immobileId", -1);
        ListinoImmobile listinoImmobile= listinoController.getListino(listinoId);

        String username = (String) ctx.getProperty("username");
        Cliente cliente= authController.getCliente(username);



        Offerta offerta= offerteController.createOfferta(
                listinoImmobile,
                cliente.getRiepilogo(),
                jsonOfferta.getString("email"),
                jsonOfferta.getString("nome"),
                jsonOfferta.getString("cognome"),
                jsonOfferta.getString("numeroTelefonico"),
                jsonOfferta.getInt("offerPrice")
        );

        tx.commit();
        database.closeSession();
            return Response.status(Response.Status.CREATED).entity(Json.createObjectBuilder()
                    .add("message", "Offerta creata con successo")
                    .add("id", offerta.getId())
                    .build()).build();

    }

}
