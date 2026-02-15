package it.unina.rest_api_dietiestates25.router;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.controller.*;
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

import java.util.ArrayList;
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
        OffertaUtente offertaUtente = offertaController.getOffertaUtente(offerteId);
/*
        if (risultato != null) {
            offertaJsonBuilder.add("risultatoOfferta", risultato.toString());
        } else {
            offertaJsonBuilder.add("risultatoOfferta", JsonValue.NULL);
        }
*/
        JsonObject jsonResponse = Json.createObjectBuilder()
                .add("id", offertaUtente.getId())
                .add("emailOfferente", offertaUtente.getEmailOfferente())
                .add("nome", offertaUtente.getNome())
                .add("cognome", offertaUtente.getCognome())
                .add("telefono", offertaUtente.getTelefono())
                .add("cifraInCentesimi", offertaUtente.getCifraInCentesimi())
                .add("cifraContropropostaInCentesimi", offertaUtente.getCifraContropropostaInCentesimi())
                .add("idListino", offertaUtente.getListino().getId())
                .add("idRiepilogo", offertaUtente.getRiepilogo().getId())
                .add("risultatoOfferta", offertaUtente.getRisultatoOfferta().toString())
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
    public Response getOfferte(@QueryParam("username") String username, @QueryParam("immobileId") int immobileId,
                               @QueryParam("offerteEsterne") boolean offerteEsterne){
        database.openSession();
        Session session= database.getSession();

        Transaction tx = session.beginTransaction();

        OfferteController offertaController= new OfferteController();
        AuthController authController= new AuthController();
        List<OffertaUtente> offerte;
        List<Offerta> offertaList = new ArrayList<Offerta>();

        if(username != null){
            int clienteId= authController.getCliente(username).getId();
            offerte = offertaController.getOffertePerCliente(clienteId);


        }else{

            offerte = offertaController.getOffertePerImmobile(immobileId);
            if(offerteEsterne){
                OfferteEsterneController offertaEsternaController= new OfferteEsterneController();
                offertaList = offertaEsternaController.getOffertePerImmobile(immobileId);
            }

        }

        JsonArrayBuilder offerteJsonArrayBuilder = Json.createArrayBuilder();

        for(OffertaUtente offertaUtente : offerte){

            JsonObject offertaJson = Json.createObjectBuilder()
                    .add("id", offertaUtente.getId())
                    .add("emailOfferente", offertaUtente.getEmailOfferente())
                    .add("nome", offertaUtente.getNome())
                    .add("cognome", offertaUtente.getCognome())
                    .add("telefono", offertaUtente.getTelefono())
                    .add("cifraInCentesimi", offertaUtente.getCifraInCentesimi())
                    .add("cifraContropropostaInCentesimi", offertaUtente.getCifraContropropostaInCentesimi())
                    .add("idListino", offertaUtente.getListino().getId())
                    .add("idRiepilogo", offertaUtente.getRiepilogo().getId())
                    .add("risultatoOfferta", offertaUtente.getRisultatoOfferta().toString())
                    .add("dataOfferta", offertaUtente.getIstanteCreazione().toString())
                    .add("offertaType", "offertaUtente")
                    .build();

            offerteJsonArrayBuilder.add(offertaJson);

        }
        if(offerteEsterne){
            for(Offerta off: offertaList){

                JsonObject offertaJson = Json.createObjectBuilder()
                        .add("id", off.getId())
                        .add("emailOfferente", off.getEmailOfferente())
                        .add("nome", off.getNome())
                        .add("cognome", off.getCognome())
                        .add("telefono", off.getTelefono())
                        .add("cifraInCentesimi", off.getCifraInCentesimi())
                        .add("idListino", off.getListino().getId())
                        .add("risultatoOfferta", off.getRisultatoOfferta().toString())
                        .add("dataOfferta", off.getIstanteCreazione().toString())
                        .add("offertaType", "offertaEsterna")
                        .build();

                offerteJsonArrayBuilder.add(offertaJson);
            }
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



        OffertaUtente offertaUtente = offerteController.createOfferta(
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
                    .add("id", offertaUtente.getId())
                    .build()).build();

    }

    @POST
    @Path("/offerteEsterne")
    @Consumes(MediaType.APPLICATION_JSON)
    @RequireAgenteImmobiliareAuthentication
    public Response creaOffertaEsterna(JsonObject jsonOfferta) {
        database.openSession();
        Session session= database.getSession();

        Transaction tx = session.beginTransaction();

        OfferteEsterneController offerteController= new OfferteEsterneController();

        ListinoController listinoController= new ListinoController();

        int listinoId = jsonOfferta.getInt("immobileId", -1);
        ListinoImmobile listinoImmobile= listinoController.getListino(listinoId);


        Offerta offerta= offerteController.createOfferteEsterne(
                listinoImmobile,
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
    @Path("/{offertaId}/revisionata")
    @Consumes(MediaType.APPLICATION_JSON)
    @RequireAgenteImmobiliareAuthentication
    public Response offertaAnnullaAccettazione(@PathParam("offertaId") int offertaId){
        database.openSession();
        Session session= database.getSession();

        Transaction tx = session.beginTransaction();

        OfferteController offerteController= new OfferteController();

        offerteController.annullaAccettazione(offerteController.getOfferta(offertaId));


        tx.commit();
        database.closeSession();
        return Response
                .status(Response.Status.OK)
                .build();
    }

    @POST
    @Path("/{offertaId}/rifiutata")
    @Consumes(MediaType.APPLICATION_JSON)
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
    public Response offertaControproposta(@PathParam("offertaId") int offertaId, JsonObject valoreOfferta ){
        database.openSession();
        Session session= database.getSession();

        Transaction tx = session.beginTransaction();

        OfferteController offerteController= new OfferteController();
        OffertaUtente offertaUtente = offerteController.getOffertaUtente(offertaId);

        offerteController.createControOfferta(offertaUtente, valoreOfferta.getInt("controproposta"));

        tx.commit();
        database.closeSession();
        return Response
                .status(Response.Status.OK)
                .build();
    }


    @DELETE
    @Path("/{offertaId}")
    public Response deleteOfferta(@PathParam("offertaId") int offertaId){
        database.openSession();
        Session session= database.getSession();

        Transaction tx = session.beginTransaction();

        OfferteController offerteController= new OfferteController();
        offerteController.deleteOfferta(offertaId);

        tx.commit();
        database.closeSession();
        return Response
                .status(Response.Status.OK)
                .build();
    }


}
