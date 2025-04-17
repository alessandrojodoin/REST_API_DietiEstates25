package it.unina.rest_api_dietiestates25.router;

import it.unina.rest_api_dietiestates25.controller.AuthController;
import it.unina.rest_api_dietiestates25.controller.ImmobileController;
import it.unina.rest_api_dietiestates25.controller.ListinoController;
import it.unina.rest_api_dietiestates25.model.*;
import jakarta.json.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Path("/immobile")
public class ImmobileRouter {


    @GET
    @Path("{immobileId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getImmobile(@PathParam("immobileId") int immobileId){

        ListinoController listinoController= new ListinoController();
        ListinoImmobile listino = listinoController.getListino(immobileId);

        JsonObjectBuilder immobileJsonBuilder = Json.createObjectBuilder()
                .add("id", listino.getImmobile().getId())
                .add("tipoImmobile", listino.getImmobile().getTipoImmobile())
                .add("longitudine", listino.getImmobile().getLongitudine())
                .add("latitudine", listino.getImmobile().getLatitudine())
                .add("indirizzo", listino.getImmobile().getIndirizzo())
                .add("citta", listino.getImmobile().getCitta())
                .add("provincia", listino.getImmobile().getProvincia());

        JsonArrayBuilder tagJsonArrayBuilder = Json.createArrayBuilder();

        for(Tag tag: listino.getImmobile().getTags()){

            JsonObject tagJson = Json.createObjectBuilder()
                    .add("nome", tag.getNome())
                    .add("valore", tag.getValore().toString())
                    .add("type", tag.getValueType())
                    .build();

            tagJsonArrayBuilder.add(tagJson);

        }

        JsonArrayBuilder fotoJsonArrayBuilder = Json.createArrayBuilder();

        for(FotoImmobile fotoImmobile: listino.getImmobile().getFoto()){


            tagJsonArrayBuilder.add(fotoImmobile.getId());
        }

        immobileJsonBuilder
                .add("tags", tagJsonArrayBuilder.build())
                .add("foto", fotoJsonArrayBuilder.build());

        JsonObject jsonResponse = Json.createObjectBuilder()
                .add("id", listino.getId())
                .add("numeroVisualizzazioni", listino.getNumeroVisualizzazioni())
                .add("tipologiaContratto" , listino.getTipologiaContratto())
                .add("prezzo", listino.getPrezzo())
                .add("speseCondominiali", listino.getSpeseCondominiali())
                .add("agenteImmobiliareId", listino.getCreatore().getId())
                .add("isVenduto", listino.isVenduto())
                .add("istanteCreazione", listino.getIstanteCreazione().toEpochMilli())
                .add("immobile", immobileJsonBuilder.build())

                .build();
        return Response
                .status(Response.Status.OK)
                .entity(jsonResponse)
                .build();

    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postImmobile(JsonObject listinoJson){

        ImmobileController immobileController = new ImmobileController();

        JsonObject immobileJson = listinoJson.getJsonObject("immobile");

        Immobile immobile = immobileController.createImmobile(immobileJson.getString("tipoImmobile"),
                immobileJson.getString("latitudine"),
                immobileJson.getString("longitudine"),
                immobileJson.getString("indirizzo"),
                immobileJson.getString("citta"),
                immobileJson.getString("provincia"));

        for(JsonValue tagJsonValue: immobileJson.getJsonArray("tags")) {
            JsonObject tag = tagJsonValue.asJsonObject();
            switch (tag.getString("type")) {
                case "NoValue":
                    immobileController.aggiungiGenericTag(tag.getString("nome"), immobile);
                    break;
                case "Integer":
                    immobileController.aggiungiIntegerTag(tag.getString("nome"), Integer.parseInt(tag.getString("value")), immobile);
                    break;
                case "Float":
                    immobileController.aggiungiFloatTag(tag.getString("nome"), Float.parseFloat(tag.getString("value")), immobile);
                    break;
                case "String":
                    immobileController.aggiungiStringTag(tag.getString("nome"), tag.getString("value"), immobile);
                    break;
                case "Boolean":
                    immobileController.aggiungiCheckboxTag(tag.getString("nome"), Boolean.parseBoolean(tag.getString("value")), immobile);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo tag non valido");
            }
        }
        ListinoController listinoController = new ListinoController();

        //Agente Immobiliare da sostituire
        listinoController.createListino(immobile,
                listinoJson.getString("tipologiaContratto"),
                Integer.parseInt(listinoJson.getString("speseCondominiali")),
                Integer.parseInt(listinoJson.getString("prezzo")),
                new AgenteImmobiliare()

        );
        return Response.ok().build();

    }



    @GET
    @Path("{immobileId}/image/{imageId}")
    @Produces("image/jpg")
    public Response getImage(@PathParam("imageId") int imageId, @PathParam("immobileId") int immobileId) {



        try{
            ImmobileController immobileController = new ImmobileController();

            BufferedImage image = immobileController.getImage(immobileId, imageId).getImage();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArrayOutputStream);
            byte[] imageData = byteArrayOutputStream.toByteArray();


            return Response.ok(imageData).build();
        }
        catch(Exception e){
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @Path("{immobileId}/image")
    @Consumes("image/jpg")
    public Response postImage(byte [] image, @PathParam("immobileId") int immobileId) {

        try{
            ImmobileController immobileController = new ImmobileController();


            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(image);
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            Immobile immobile = immobileController.getImmobile(immobileId);
            immobileController.addImage(immobile, bufferedImage);


            return Response.ok().build();
        }
        catch(Exception e){
            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }
}
