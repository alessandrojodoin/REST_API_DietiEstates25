package it.unina.rest_api_dietiestates25.router;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AuthRouter {

    @POST
    @Path("cliente")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response registerCliente(MultivaluedMap<String, String> userCredentials) {
        System.out.println(userCredentials);
        return Response.ok().build();
    }
}
