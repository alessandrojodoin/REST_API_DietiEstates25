package it.unina.rest_api_dietiestates25.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.AgenteImmobiliare;
import it.unina.rest_api_dietiestates25.model.AmministratoreAgenzia;
import it.unina.rest_api_dietiestates25.model.Cliente;
import it.unina.rest_api_dietiestates25.model.Utente;

import org.hibernate.Session;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AuthController {


    private final Session session = Database.getInstance().getSession();

    public static boolean validateToken(String token){

        final String ISSUER = "rest_api_dietiestates25";
        final Algorithm algorithm = Algorithm.HMAC256(System.getenv("JWT_SECRET"));
        final JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public static String getUsernameClaim(String token) {
        try {

            final String ISSUER = "rest_api_dietiestates25";
            final Algorithm algorithm = Algorithm.HMAC256(System.getenv("JWT_SECRET"));
            final JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getClaim("username").asString();
        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }





    //WIP
    public void createCliente(String username, String email, String nome, String cognome, String password, String numeroTelefonico) {

        session.beginTransaction();
        Cliente cliente = new Cliente(username, email, nome, cognome, password, numeroTelefonico);
        session.persist(cliente);
        session.getTransaction().commit();

    }

    public void createAmministratore(String username, String email, String nome, String cognome, String password, String numeroTelefonico){

        session.beginTransaction();
        AmministratoreAgenzia amministratore= new AmministratoreAgenzia(username, email, nome, cognome, password, numeroTelefonico);
        session.persist(amministratore);
        session.getTransaction().commit();

    }


    public void createAgenteImmobiliare(String username, String email, String nome, String cognome, String password, String numeroTelefonico) {

        session.beginTransaction();
        AgenteImmobiliare agenteImmobiliare = new AgenteImmobiliare(username, email, nome, cognome, password, numeroTelefonico);
        session.persist(agenteImmobiliare);
        session.getTransaction().commit();
    }

    public AgenteImmobiliare getAgenteImmobiliare(String username){
        return session.createSelectionQuery("from AgenteImmobiliare where username like :username", AgenteImmobiliare.class)
                        .setParameter("username", username)
                        .getSingleResultOrNull();
    }


    public String authenticateUser(String username, String password) throws IllegalArgumentException {

        Utente utente =
                session.createSelectionQuery("from Utente where username like :username", Utente.class)
                .setParameter("username", username)
                .getSingleResultOrNull();
        if (utente == null || (!utente.verifyPassword(password))) {
            throw new IllegalArgumentException("Authentication failed");
        }

        return createJWT(username, TimeUnit.DAYS.toMillis(1));
    }





    private String createJWT(String username, long ttlMillis) {

        final String ISSUER = "rest_api_dietiestates25";
        final Algorithm algorithm = Algorithm.HMAC256(System.getenv("JWT_SECRET"));


        String token = JWT.create()
                .withIssuer(ISSUER)
                .withClaim("username", username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + ttlMillis))
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);

        return token;
    }


}
