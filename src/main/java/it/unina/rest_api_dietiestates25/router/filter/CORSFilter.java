package it.unina.rest_api_dietiestates25.router.filter;



import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;


    @Provider
    public class CORSFilter implements ContainerResponseFilter {

        @Override
        public void filter(ContainerRequestContext request,
                           ContainerResponseContext response) throws IOException {
            response.getHeaders().add("Access-Control-Allow-Origin", "*");
            response.getHeaders().add("Access-Control-Allow-Headers",
                    "CSRF-Token, X-Requested-By, Authorization, Content-Type");
            response.getHeaders().add("Access-Control-Allow-Credentials", "true");
            response.getHeaders().add("Access-Control-Allow-Methods",
                    "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        }
}
