package it.unina.rest_api_dietiestates25.router;

import it.unina.rest_api_dietiestates25.controller.AuthController;
import it.unina.rest_api_dietiestates25.controller.OfferteController;
import it.unina.rest_api_dietiestates25.model.*;
import it.unina.rest_api_dietiestates25.router.filter.RequireClienteAuthentication;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/offerte")
public class OffertaRouter {

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
        try {
            Offerta offerta = new Offerta();
            offerta.setEmailOfferente(jsonOfferta.getString("emailOfferente", null));
            offerta.setNome(jsonOfferta.getString("nome", null));
            offerta.setCognome(jsonOfferta.getString("cognome", null));
            offerta.setTelefono(jsonOfferta.getString("telefono", null));
            offerta.setCifraInCentesimi(jsonOfferta.getInt("cifraInCentesimi", 0));
            offerta.setCifraContropropostaInCentesimi(jsonOfferta.getInt("cifraContropropostaInCentesimi", 0));

            int listinoId = jsonOfferta.getInt("listinoId", -1);
            int riepilogoId = jsonOfferta.getInt("riepilogoId", -1);

            // Recupera i riferimenti se necessario (dipende dalla logica interna)
            ListinoImmobile listino = new ListinoImmobile();
            listino.setId(listinoId);
            offerta.setListino(listino);


            AuthController authController= new AuthController();
            RiepilogoAttivita riepilogo= authController.getCliente(headers.getHeaderString("username")).getRiepilogo();
            riepilogo.setId(riepilogoId);
            offerta.setRiepilogo(riepilogo);


            OfferteController controller = new OfferteController();
            controller.createOfferta(offerta.getListino(), offerta.getRiepilogo(), offerta.getEmailOfferente(),
                    offerta.getNome(), offerta.getCognome(), offerta.getTelefono(), offerta.getCifraInCentesimi());

            return Response.status(Response.Status.CREATED).entity(Json.createObjectBuilder()
                    .add("message", "Offerta creata con successo")
                    .add("id", offerta.getId())
                    .build()).build();

        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Json.createObjectBuilder()
                            .add("error", "Errore nella creazione dell'offerta")
                            .add("details", e.getMessage())
                            .build())
                    .build();
        }
    }

}
