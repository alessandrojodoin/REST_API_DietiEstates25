package it.unina.rest_api_dietiestates25.router;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.controller.AuthController;
import it.unina.rest_api_dietiestates25.model.Utente;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;

@Path("/auth")
public class AuthRouter {

    private final Database database = Database.getInstance();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response authenticate(JsonObject loginCredentials) {

        database.openSession();
        Session session = database.getSession();

        Transaction tx = session.beginTransaction();

        AuthController authController = new AuthController();
        String jwtToken;

        try{

            jwtToken= authController.authenticateUser(
                    loginCredentials.getString("username"),
                    loginCredentials.getString("password")
            );
        }catch(Exception e){
            tx.commit();
            database.closeSession();

            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Error 401: not authorized.")
                    .build();
        }





        tx.commit();
        database.closeSession();

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

        database.openSession();
        Session session = database.getSession();

        Transaction tx = session.beginTransaction();

        AuthController authController = new AuthController();
        authController.createCliente
                (userCredentials.getString("username"),
                userCredentials.getString("email"),
                userCredentials.getString("nome"),
                userCredentials.getString("cognome"),
                userCredentials.getString("password"),
                userCredentials.getString("numeroTelefonico"));

        tx.commit();
        database.closeSession();
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
        database.openSession();
        Session session = database.getSession();

        Transaction tx = session.beginTransaction();

        AuthController authController = new AuthController();
        authController.createAgenteImmobiliare(
                userCredentials.getString("username"),
                userCredentials.getString("email"),
                userCredentials.getString("nome"),
                userCredentials.getString("cognome"),
                userCredentials.getString("password"),
                userCredentials.getString("numeroTelefonico"),
                userCredentials.getString("agenziaImmobiliare")
        );
        tx.commit();
        database.closeSession();

        return Response.ok().build();
    }

    @GET
    @Path("users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response userInformations(@QueryParam("username") String username) {
        database.openSession();
        Session session = database.getSession();

        Transaction tx = session.beginTransaction();


        if (username == null || username.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Username mancante")
                    .build();
        }

        AuthController authController = new AuthController();
        Utente utente = authController.getUtente(username);

        if (utente == null) {
            tx.commit();
            database.closeSession();
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Utente non trovato")
                    .build();
        }

        JsonObject jsonResponse = Json.createObjectBuilder()
                .add("username", utente.getUsername())
                .add("email", utente.getEmail())
                .add("nome", utente.getNome())
                .add("cognome", utente.getCognome())
                .add("numeroTelefonico", utente.getNumeroTelefonico())
                .add("agenziaImmobiliare", utente.getAgenziaImmobiliare())
                .build();

        tx.commit();
        database.closeSession();
        return Response.ok(jsonResponse).build();
    }


    @POST
    @Path("amministratore")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAmministratore(JsonObject userCredentials) {
        database.openSession();
        Session session = database.getSession();

        Transaction tx = session.beginTransaction();


        AuthController authController = new AuthController();
        authController.createAmministratore(
                userCredentials.getString("username"),
                "mail",
                "nome",
                "cognome",
                "admin",
                "numeroTelefonico",
                userCredentials.getString("agenziaImmobiliare")
        );


        tx.commit();
        database.closeSession();
        return Response.ok().build();
    }


    @POST
    @Path("accountSupporto")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAmministratoreSupporto(JsonObject userCredentials) {
        database.openSession();
        Session session = database.getSession();

        Transaction tx = session.beginTransaction();


        AuthController authController = new AuthController();
        authController.createAmministratoreSupporto(
                userCredentials.getString("username"),
                userCredentials.getString("email"),
                userCredentials.getString("nome"),
                userCredentials.getString("cognome"),
                userCredentials.getString("password"),
                userCredentials.getString("numeroTelefonico"),
                userCredentials.getString("agenziaImmobiliare")
        );

        tx.commit();
        database.closeSession();
        return Response.ok().build();
    }



    @PUT
    @Path("amministratore/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modificaCredenzialiAmministratore(JsonObject userCredentials, @PathParam("username") String amministratoreUsername) {
        database.openSession();
        Session session = database.getSession();

        Transaction tx = session.beginTransaction();


        AuthController authController = new AuthController();
        authController.modificaAmministratore(
                amministratoreUsername,
                userCredentials.getString("new_username"),
                userCredentials.getString("password")
        );


        tx.commit();
        database.closeSession();
        return Response.ok().build();
    }


}


