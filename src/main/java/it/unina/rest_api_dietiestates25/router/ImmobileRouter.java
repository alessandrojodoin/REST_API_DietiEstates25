package it.unina.rest_api_dietiestates25.router;

import it.unina.rest_api_dietiestates25.controller.ImmobileController;
import it.unina.rest_api_dietiestates25.controller.ListinoController;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/immobile")
public class ImmobileRouter {


    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getImmobile(JsonObject js_id){
        ListinoController listinoController= new ListinoController();
        int id= js_id.getInt("id");
        listinoController.getListino(id);
        //Json.createObjectBuilder().add();
        return Response
                .status(Response.Status.OK)
                .entity(listinoController)
                .build();
    }
}
