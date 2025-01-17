package it.unina.rest_api_dietiestates25;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;


@Path("/hello-world")
public class HelloResource {
    public static void main(final String[] args){
        System.out.println("Hello World");
    }
    @GET
    @Produces("text/plain")
    public String hello() {
        return "Hello, World!";
    }
}