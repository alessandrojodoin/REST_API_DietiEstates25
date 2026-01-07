package it.unina.rest_api_dietiestates25.router;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.controller.AuthController;
import it.unina.rest_api_dietiestates25.controller.ImmobileController;
import it.unina.rest_api_dietiestates25.controller.ListinoController;
import it.unina.rest_api_dietiestates25.model.*;
import it.unina.rest_api_dietiestates25.router.filter.RequireAgenteImmobiliareAuthentication;
import jakarta.json.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Set;

@Path("/immobile")
public class ImmobileRouter {

    @Context
    private HttpHeaders headers;

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
                .add("provincia", listino.getImmobile().getProvincia())
                .add("fotoNumber", listino.getImmobile().getFotoNumber());


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
    @RequireAgenteImmobiliareAuthentication
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
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
                    immobileController.aggiungiIntegerTag(tag.getString("nome"), tag.getInt("value"), immobile);
                    break;
                case "Float":
                    JsonNumber num = tag.getJsonNumber("value");
                    float value = num.bigDecimalValue().floatValue();
                    immobileController.aggiungiFloatTag(tag.getString("nome"), value, immobile);
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




        AuthController authController = new AuthController();
        AgenteImmobiliare agenteImmobiliare = authController.getAgenteImmobiliare(headers.getHeaderString("username"));



        listinoController.createListino(immobile,
                listinoJson.getString("tipologiaContratto"),
                listinoJson.getInt("speseCondominiali"),
                listinoJson.getInt("prezzo"),
                agenteImmobiliare


        );
        return Response
                .status(Response.Status.OK)
                .entity(immobile.getId())
                .build();

    }



    @GET
    @Path("{immobileId}/image/{imageId}")
    @Produces("image/png")
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
    //@Consumes("image/png")
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

    @GET
    @Path("{immobileId}/imageIds")
    public Response getImageIds(@PathParam("immobileId") int immobileId){
        try{
            ImmobileController immobileController = new ImmobileController();
            Immobile immobile = immobileController.getImmobile(immobileId);
            Set<FotoImmobile>  foto= immobile.getFoto();

            ArrayList<Integer> imageIds= new ArrayList<Integer>();
            for (FotoImmobile f : foto) {
                imageIds.add(f.getId());
            }

            return Response.ok(imageIds).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }

    }



}
