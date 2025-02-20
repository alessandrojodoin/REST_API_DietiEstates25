package it.unina.rest_api_dietiestates25.router;

import it.unina.rest_api_dietiestates25.HelloResource;
import it.unina.rest_api_dietiestates25.controller.AuthController;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AuthRouter {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response authenticate(JsonObject loginCredentials) {
        AuthController authController = new AuthController(HelloResource.getSessionFactory());
        String jwtToken = authController.authenticateUser(
                loginCredentials.getString("username"),
                loginCredentials.getString("password"));

        return Response
                .status(Response.Status.OK)
                .entity(jwtToken)
                .build();
    }

    @POST
    @Path("cliente")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response registerCliente(JsonObject userCredentials) {
        AuthController authController = new AuthController(HelloResource.getSessionFactory());
        authController.createCliente
                (userCredentials.getString("username"),
                userCredentials.getString("email"),
                userCredentials.getString("nome"),
                userCredentials.getString("cognome"),
                userCredentials.getString("password"),
                userCredentials.getString("numeroTelefonico"));
        return Response.ok().build();
    }

    @POST
    @Path("agente-immobiliare")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response registerAgenteImmobiliare(JsonObject userCredentials) {
        AuthController authController = new AuthController(HelloResource.getSessionFactory());
        authController.createAgenteImmobiliare(
                userCredentials.getString("username"),
                userCredentials.getString("email"),
                userCredentials.getString("nome"),
                userCredentials.getString("cognome"),
                userCredentials.getString("password"),
                userCredentials.getString("numeroTelefonico")
        );
        return Response.ok().build();
    }



}





