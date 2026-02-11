package it.unina.rest_api_dietiestates25.router;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.controller.*;
import it.unina.rest_api_dietiestates25.model.*;
import it.unina.rest_api_dietiestates25.router.filter.RequireAgenteImmobiliareAuthentication;
import it.unina.rest_api_dietiestates25.router.filter.RequireClienteAuthentication;
import jakarta.json.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Path("/immobile")
public class ImmobileRouter {

    @Context
    private HttpHeaders headers;

    @Context
    private ContainerRequestContext ctx;

    private final Database database = Database.getInstance();




    @GET
    @Path("{immobileId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getImmobile(@PathParam("immobileId") int immobileId){

        database.openSession();
        Session session = database.getSession();

        Transaction tx = session.beginTransaction();

        ListinoController listinoController= new ListinoController();
        ListinoImmobile listino = listinoController.getListino(immobileId);

        JsonObject indirizzoJson = Json.createObjectBuilder()
                .add("via", listino.getImmobile().getVia())
                .add("citta", listino.getImmobile().getCitta())
                .add("provincia", listino.getImmobile().getProvincia())
                .build();

        JsonObjectBuilder immobileJsonBuilder = Json.createObjectBuilder()
                .add("id", listino.getImmobile().getId())
                .add("tipoImmobile", listino.getImmobile().getTipoImmobile())
                .add("longitudine", listino.getImmobile().getLongitudine())
                .add("latitudine", listino.getImmobile().getLatitudine())
                .add("fotoNumber", listino.getImmobile().getFotoNumber())
                .add("indirizzo", indirizzoJson);


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

        immobileJsonBuilder
                .add("tags", tagJsonArrayBuilder.build())
                .add("foto", fotoJsonArrayBuilder.build());

        JsonObject jsonResponse = Json.createObjectBuilder()
                .add("nome", listino.getNome())
                .add("descrizione", listino.getDescrizione())
                .add("id", listino.getId())
                .add("numeroVisualizzazioni", listino.getNumeroVisualizzazioni())
                .add("tipologiaContratto" , listino.getTipologiaContratto())
                .add("prezzo", listino.getPrezzo())
                .add("speseCondominiali", listino.getSpeseCondominiali())
                .add("agenteImmobiliareId", listino.getCreatore().getId())
                .add("creatore", listino.getCreatore().getUsername())
                .add("isVenduto", listino.isVenduto())
                .add("esisteOffertaAccettata", listinoController.esisteOffertaAccettata(listino))
                .add("istanteCreazione", listino.getIstanteCreazione().toEpochMilli())
                .add("immobile", immobileJsonBuilder.build())

                .build();

        tx.commit();
        database.closeSession();

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

        database.openSession();
        Session session = database.getSession();

        Transaction tx = session.beginTransaction();


        ImmobileController immobileController = new ImmobileController();

        JsonObject immobileJson = listinoJson.getJsonObject("immobile");

        Immobile immobile = immobileController.createImmobile(immobileJson.getString("tipoImmobile"),
                immobileJson.getString("latitudine"),
                immobileJson.getString("longitudine"),
                immobileJson.getString("via"),
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
        GeolocationAPIFactory geoFactory= new GeolocationAPIFactory();
        GeolocationAPI geoAPI = geoFactory.getGeolocationAPI();
        try {
            geoAPI.addNearbyServiceTags(immobile);
        }catch(Exception exception){
            System.out.println("An error occurred: " + exception);
        }

        ListinoController listinoController = new ListinoController();


        AuthController authController = new AuthController();


        String username = (String) ctx.getProperty("username");

        AgenteImmobiliare agenteImmobiliare =
                authController.getAgenteImmobiliare(username);



        listinoController.createListino(immobile,
                listinoJson.getString("nome"),
                listinoJson.getString("descrizione"),
                listinoJson.getString("tipologiaContratto"),
                listinoJson.getInt("speseCondominiali"),
                listinoJson.getInt("prezzo"),
                agenteImmobiliare


        );


        tx.commit();
        database.closeSession();

        return Response
                .status(Response.Status.OK)
                .entity(immobile.getId())
                .build();

    }



    @GET
    @Path("{immobileId}/image/{imageId}")
    @Produces("image/png")
    public Response getImage(@PathParam("imageId") int imageId, @PathParam("immobileId") int immobileId) {

        database.openSession();
        Session session = database.getSession();

        Transaction tx = session.beginTransaction();

        try{
            ImmobileController immobileController = new ImmobileController();

            BufferedImage image = immobileController.getImage(immobileId, imageId).getImage();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArrayOutputStream);
            byte[] imageData = byteArrayOutputStream.toByteArray();

            tx.commit();
            database.closeSession();



            return Response.ok(imageData).build();
        }
        catch(Exception e){

            tx.commit();
            database.closeSession();


            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @POST
    @RequireAgenteImmobiliareAuthentication
    @Path("{immobileId}/image")
    //@Consumes("image/png")
    public Response postImage(byte [] image, @PathParam("immobileId") int immobileId) {

        database.openSession();
        Session session = database.getSession();

        Transaction tx = session.beginTransaction();

        try{
            ImmobileController immobileController = new ImmobileController();

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(image);
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            Immobile immobile = immobileController.getImmobile(immobileId);
            immobileController.addImage(immobile, bufferedImage);


            tx.commit();
            database.closeSession();

            return Response.ok().build();
        }
        catch(Exception e){
            tx.commit();
            database.closeSession();

            e.printStackTrace();
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("{immobileId}/imageIds")
    public Response getImageIds(@PathParam("immobileId") int immobileId){

        database.openSession();
        Session session = database.getSession();

        Transaction tx = session.beginTransaction();
        try{
            ImmobileController immobileController = new ImmobileController();
            Immobile immobile = immobileController.getImmobile(immobileId);
            Set<FotoImmobile>  foto= immobile.getFoto();

            ArrayList<Integer> imageIds= new ArrayList<Integer>();
            for (FotoImmobile f : foto) {
                imageIds.add(f.getId());
            }
            tx.commit();
            database.closeSession();


            return Response.ok(imageIds).build();

        } catch (Exception e) {
            tx.commit();
            database.closeSession();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }

    }

@GET
@RequireAgenteImmobiliareAuthentication
@Produces(MediaType.APPLICATION_JSON)
public Response getImmobili(@QueryParam("agenteImmobiliare") String agenteUsername, @QueryParam("cliente") String clienteUsername, @QueryParam("filters") boolean filters,
                            @QueryParam("minPrice") int minPrice, @QueryParam("maxPrice") int maxPrice, @QueryParam("propertyType") String propertyType, @QueryParam("bathrooms") int bathrooms,
                            @QueryParam("bedrooms") int bedrooms, @QueryParam("areaSize") int areaSize, @QueryParam("extraFeatures") boolean extraFeatures,
                            @QueryParam("energyClass") String energyClass, @QueryParam("citta") String citta, @QueryParam("Terrazzo") boolean Terrazzo,
                            @QueryParam("Balcone") boolean Balcone, @QueryParam("Ascensore") boolean Ascensore, @QueryParam("Garage") boolean Garage,
                            @QueryParam("Giardino") boolean Giardino, @QueryParam("PostoAuto") boolean PostoAuto, @QueryParam("AccessoDisabili") boolean AccessoDisabili
                            ){

    database.openSession();
    Session session = database.getSession();

    Transaction tx = session.beginTransaction();

    ListinoController listinoController= new ListinoController();
    List<ListinoImmobile> immobili;

        if(agenteUsername != null){

            AuthController authController= new AuthController();
            AgenteImmobiliare agente= authController.getAgenteImmobiliare(agenteUsername);

             immobili= listinoController.getImmobileListPerAgente(agente.getId());
        }else if(clienteUsername != null){
            AuthController authController= new AuthController();
            Cliente cliente= authController.getCliente(clienteUsername);

            immobili= listinoController.getImmobileListPerCliente(cliente.getId());
        }
        else{
            immobili= listinoController.getImmobileList();
        }

    JsonArrayBuilder immobiliJsonArrayBuilder = Json.createArrayBuilder();

    for(ListinoImmobile listino: immobili){

        JsonObject indirizzoJson = Json.createObjectBuilder()
                .add("via", listino.getImmobile().getVia())
                .add("citta", listino.getImmobile().getCitta())
                .add("provincia", listino.getImmobile().getProvincia())
                .build();

        JsonObjectBuilder immobileJsonBuilder = Json.createObjectBuilder()
                .add("id", listino.getImmobile().getId())
                .add("tipoImmobile", listino.getImmobile().getTipoImmobile())
                .add("longitudine", listino.getImmobile().getLongitudine())
                .add("latitudine", listino.getImmobile().getLatitudine())
                .add("fotoNumber", listino.getImmobile().getFotoNumber())
                .add("indirizzo", indirizzoJson);


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

        immobileJsonBuilder
                .add("tags", tagJsonArrayBuilder.build())
                .add("foto", fotoJsonArrayBuilder.build());

        JsonObject jsonResponse = Json.createObjectBuilder()
                .add("nome", listino.getNome())
                .add("descrizione", listino.getDescrizione())
                .add("id", listino.getId())
                .add("numeroVisualizzazioni", listino.getNumeroVisualizzazioni())
                .add("tipologiaContratto" , listino.getTipologiaContratto())
                .add("prezzo", listino.getPrezzo())
                .add("speseCondominiali", listino.getSpeseCondominiali())
                .add("agenteImmobiliareId", listino.getCreatore().getId())
                .add("isVenduto", listino.isVenduto())
                .add("esisteOffertaAccettata", listinoController.esisteOffertaAccettata(listino))
                .add("istanteCreazione", listino.getIstanteCreazione().toEpochMilli())
                .add("immobile", immobileJsonBuilder.build())

                .build();


        immobiliJsonArrayBuilder.add(jsonResponse);

    }


    tx.commit();
    database.closeSession();
    return Response.ok()
            .entity(immobiliJsonArrayBuilder.build())
            .build();

}

    @POST
    @Path("/visualizzazione")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @RequireClienteAuthentication
    public Response postImmobiliVisualizzati(JsonObject listinoJson){

        database.openSession();
        Session session = database.getSession();

        Transaction tx = session.beginTransaction();

        int listinoId= listinoJson.getInt("id");
        String username = (String) ctx.getProperty("username");

        ListinoController listinoController= new ListinoController();
        listinoController.aggiungiVisualizzazione(listinoId, username);

        tx.commit();
        database.closeSession();

        return Response
                .status(Response.Status.OK)
                .entity(listinoId)
                .build();
    }


    @POST
    @Path("{immobileId}/venduto")
    @Produces(MediaType.TEXT_PLAIN)
    @RequireClienteAuthentication
    public Response setImmobileToVenduto(@PathParam("immobileId") int immobileId){
        database.openSession();
        Session session = database.getSession();
        Transaction tx = session.beginTransaction();

        ListinoController listinoController= new ListinoController();


        listinoController.setImmobileToVenduto(immobileId);

        tx.commit();
        database.closeSession();

        return  Response.ok()
                .entity(immobileId)
                .build();
    }


}
