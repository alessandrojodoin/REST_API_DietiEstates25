package it.unina.rest_api_dietiestates25.router;

import it.unina.rest_api_dietiestates25.controller.OfferteController;
import it.unina.rest_api_dietiestates25.model.*;
import jakarta.json.Json;
import jakarta.json.JsonValue;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/offerte")
public class OffertaRouter {
/*
this.listino= listino;
        this.riepilogo= riepilogo;
        this.emailOfferente= emailOfferente;
        this.nome= nome;
        this.cognome= cognome;
        this.telefono= telefono;
        this.risultatoOfferta= risultatoOfferta;
        this.cifraInCentesimi= cifraInCentesimi;
        this.cifraContropropostaInCentesimi= cifraContropropostaInCentesimi;
        this.istanteCreazione= Instant.now();
 */

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
}
