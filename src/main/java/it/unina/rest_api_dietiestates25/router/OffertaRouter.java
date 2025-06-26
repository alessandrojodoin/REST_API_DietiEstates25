package it.unina.rest_api_dietiestates25.router;

import it.unina.rest_api_dietiestates25.controller.OfferteController;
import it.unina.rest_api_dietiestates25.model.*;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.*;
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

        JsonObjectBuilder offertaJsonBuilder = Json.createObjectBuilder()
                .add("id", offerta.getId())
                .add("emailOfferente", offerta.getEmailOfferente())
                .add("nome", offerta.getNome())
                .add("cognome", offerta.getCognome())
                .add("telefono", offerta.getTelefono())
                .add("cifraInCentesimi", offerta.getCifraInCentesimi())
                .add("cifraContropropostaInCentesimi", offerta.getCifraContropropostaInCentesimi());

        ListinoImmobile listino= offerta.getListino();
        JsonObject listinoJson = Json.createObjectBuilder()
                .add("id", listino.getId())
                .add("tipologiaContratto", listino.getTipologiaContratto())
                .add("prezzo", listino.getPrezzo())
                .add("isVenduto", listino.isVenduto())
                .build();

        RiepilogoAttivita riepilogo = offerta.getRiepilogo();
        JsonObject riepilogoJson = Json.createObjectBuilder()
                .add("id", riepilogo.getId())
                .build();


        offertaJsonBuilder
                .add("listino", listinoJson)
                .add("riepilogo", riepilogoJson);

        RisultatoOfferta risultato = offerta.getRisultatoOfferta();

        if (risultato != null) {
            offertaJsonBuilder.add("risultatoOfferta", risultato.toString());
        } else {
            offertaJsonBuilder.add("risultatoOfferta", JsonValue.NULL);
        }

        JsonObject jsonResponse = Json.createObjectBuilder()    //listino, risultato offerta, riepilogo ??
                .add("id", offerta.getId())
                .add("emailOfferente", offerta.getEmailOfferente())
                .add("nome", offerta.getNome())
                .add("cognome", offerta.getCognome())
                .add("telefono", offerta.getTelefono())
                .add("cifraInCentesimi", offerta.getCifraInCentesimi())
                .add("cifraContropropostaInCentesimi", offerta.getCifraContropropostaInCentesimi())
                .build();
        return Response
                .status(Response.Status.OK)
                .entity(jsonResponse)
                .build();


    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
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
            if (listinoId > 0) {
                ListinoImmobile listino = new ListinoImmobile();
                listino.setId(listinoId);
                offerta.setListino(listino);
            }

            if (riepilogoId > 0) {
                RiepilogoAttivita riepilogo = new RiepilogoAttivita();
                riepilogo.setId(riepilogoId);
                offerta.setRiepilogo(riepilogo);
            }

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
