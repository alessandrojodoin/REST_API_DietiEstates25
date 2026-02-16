package it.unina.rest_api_dietiestates25.router.filter;


import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.AgenteImmobiliare;
import it.unina.rest_api_dietiestates25.model.Utente;
import it.unina.rest_api_dietiestates25.service.NotificheService;
import jakarta.annotation.Priority;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import it.unina.rest_api_dietiestates25.controller.AuthController;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Provider
@RequireAgenteImmobiliareAuthentication
@Priority(Priorities.AUTHENTICATION)
public class AgenteImmobiliareAuthenticationFilter implements ContainerRequestFilter {
    private final Database database = Database.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(AgenteImmobiliareAuthenticationFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        logger.info("Filtering request");
        logger.info("Method: " + containerRequestContext.getMethod());

        database.openSession();
        Session session = database.getSession();

        Transaction tx = session.beginTransaction();


        // Get the HTTP Authorization header from the request
        String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        logger.info("Authorization header: " + authorizationHeader);

        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            logger.info("Invalid authorizationHeader : " + authorizationHeader);
            tx.commit();
            database.closeSession();
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        String token = authorizationHeader.substring("Bearer".length()).trim();
        AuthController authController = new AuthController();
        Utente utente = authController.getUtente(AuthController.getUsernameClaim(token));

        if( AuthController.validateToken(token) && utente instanceof AgenteImmobiliare){
            logger.info("Token is valid: " + token);
            containerRequestContext.setProperty(
                    "username",
                    AuthController.getUsernameClaim(token)
            );
            tx.commit();
            database.closeSession();

        } else {
            logger.info("Token is NOT valid: " + token);
            tx.commit();
            database.closeSession();
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
