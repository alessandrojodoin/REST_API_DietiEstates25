package it.unina.rest_api_dietiestates25.router.filter;


import jakarta.annotation.Priority;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import it.unina.rest_api_dietiestates25.controller.AuthController;

import java.io.IOException;

@Provider
@RequireAgenteImmobiliareAuthentication
@Priority(Priorities.AUTHENTICATION)
public class AgenteImmobiliareAuthenticationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        System.out.println("Filtering request");


        // Get the HTTP Authorization header from the request
        String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        System.out.println("Authorization header: " + authorizationHeader);

        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("Invalid authorizationHeader : " + authorizationHeader);
            throw new NotAuthorizedException("Authorization header must be provided");
        }

        String token = authorizationHeader.substring("Bearer".length()).trim();

        if( AuthController.validateToken(token) ){
            System.out.println("Token is valid: " + token);
            containerRequestContext.getHeaders().add("username", AuthController.getUsernameClaim(token));

        } else {
            System.out.println("Token is NOT valid: " + token);
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
