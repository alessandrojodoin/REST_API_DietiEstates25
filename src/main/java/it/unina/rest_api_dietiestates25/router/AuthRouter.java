package it.unina.rest_api_dietiestates25.router;

import com.google.api.client.http.HttpTransport;
import it.unina.rest_api_dietiestates25.HelloResource;
import it.unina.rest_api_dietiestates25.controller.AuthController;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.util.Collections;

@Path("/auth")
public class AuthRouter {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response authenticate(JsonObject loginCredentials) {
        AuthController authController = new AuthController();
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
        AuthController authController = new AuthController();
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
    @Path("google-auth")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response authenticateGoogle(JsonObject googleIdToken) {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                // Specify the WEB_CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(System.getenv("GOOGLE_SIGNIN_CLIENT_ID")))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(WEB_CLIENT_ID_1, WEB_CLIENT_ID_2, WEB_CLIENT_ID_3))
                .build();

// (Receive idTokenString by HTTPS POST)

        GoogleIdToken idToken = null;
        try{
            idToken = verifier.verify(googleIdToken.getString("idTokenString"));
        }
        catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }

        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            System.out.println("Email: " + email);
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            // Use or store profile information
            // ...

        } else {
            System.out.println("Invalid ID token.");
        }
        return Response.ok().build();
    }

    @POST
    @Path("agente-immobiliare")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response registerAgenteImmobiliare(JsonObject userCredentials) {
        AuthController authController = new AuthController();
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


