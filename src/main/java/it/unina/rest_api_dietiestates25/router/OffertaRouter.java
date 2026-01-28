package it.unina.rest_api_dietiestates25.router;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.controller.AuthController;
import it.unina.rest_api_dietiestates25.controller.ImmobileController;
import it.unina.rest_api_dietiestates25.controller.ListinoController;
import it.unina.rest_api_dietiestates25.controller.OfferteController;
import it.unina.rest_api_dietiestates25.model.*;
import it.unina.rest_api_dietiestates25.router.filter.RequireAgenteImmobiliareAuthentication;
import it.unina.rest_api_dietiestates25.router.filter.RequireClienteAuthentication;
import jakarta.json.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Path("/offerte")
public class OffertaRouter {
    @Context
    private ContainerRequestContext ctx;
    private final Database database = Database.getInstance();
    @Context
    private HttpHeaders headers;

    @GET
    @Path("{offerteId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOfferte(@PathParam("offerteId") int offerteId){
        database.openSession();
        Session session= database.getSession();

        Transaction tx = session.beginTransaction();

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
        tx.commit();
        database.closeSession();
        return Response
                .status(Response.Status.OK)
                .entity(jsonResponse)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOfferte(@QueryParam("username") String username, @QueryParam("immobileId") int immobileId){
        database.openSession();
        Session session= database.getSession();

        Transaction tx = session.beginTransaction();

        OfferteController offertaController= new OfferteController();
        AuthController authController= new AuthController();
        List<Offerta> offerte;

        if(username != null){
            int clienteId= authController.getCliente(username).getId();
            offerte = offertaController.getOffertePerCliente(clienteId);

        }else{

            offerte = offertaController.getOffertePerImmobile(immobileId);

        }

        JsonArrayBuilder offerteJsonArrayBuilder = Json.createArrayBuilder();

        for(Offerta offerta: offerte){

            JsonObject offertaJson = Json.createObjectBuilder()
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
                    .add("dataOfferta", offerta.getIstanteCreazione().toString())
                    .build();

            offerteJsonArrayBuilder.add(offertaJson);

        }
        tx.commit();
        database.closeSession();
        return Response
                .status(Response.Status.OK)
                .entity(offerteJsonArrayBuilder.build())
                .build();
    }


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


    @POST
    @Path("/{offertaId}/accettata")
    @Consumes(MediaType.APPLICATION_JSON)
    @RequireAgenteImmobiliareAuthentication
    public Response offertaAccettata(@PathParam("offertaId") int offertaId){
        database.openSession();
        Session session= database.getSession();

        Transaction tx = session.beginTransaction();

        OfferteController offerteController= new OfferteController();

        offerteController.setOffertaAccettata(offerteController.getOfferta(offertaId));


        tx.commit();
        database.closeSession();
        return Response
                .status(Response.Status.OK)
                .build();
    }

    @POST
    @Path("/{offertaId}/rifiutata")
    @Consumes(MediaType.APPLICATION_JSON)
    @RequireAgenteImmobiliareAuthentication
    public Response offertaRifiutata(@PathParam("offertaId") int offertaId){
        database.openSession();
        Session session= database.getSession();

        Transaction tx = session.beginTransaction();

        OfferteController offerteController= new OfferteController();

        offerteController.setOffertaRifiutata(offerteController.getOfferta(offertaId));


        tx.commit();
        database.closeSession();
        return Response
                .status(Response.Status.OK)
                .build();
    }

    @POST
    @Path("/{offertaId}/controproposta")
    @Consumes(MediaType.APPLICATION_JSON)
    @RequireAgenteImmobiliareAuthentication
    public Response offertaControproposta(@PathParam("offertaId") int offertaId, JsonObject valoreOfferta ){
        database.openSession();
        Session session= database.getSession();

        Transaction tx = session.beginTransaction();

        OfferteController offerteController= new OfferteController();
        Offerta offerta= offerteController.getOfferta(offertaId);

        offerteController.createControOfferta(offerta, valoreOfferta.getInt("controproposta"));

        tx.commit();
        database.closeSession();
        return Response
                .status(Response.Status.OK)
                .build();
    }
}
