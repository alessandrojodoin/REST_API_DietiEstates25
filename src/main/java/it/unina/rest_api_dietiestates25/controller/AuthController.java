package it.unina.rest_api_dietiestates25.controller;

import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.AgenteImmobiliare;
import it.unina.rest_api_dietiestates25.model.Cliente;
import it.unina.rest_api_dietiestates25.model.Utente;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.xml.crypto.Data;
import java.io.StringReader;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AuthController {

    private final SessionFactory sessionFactory = Database.getInstance().getSessionFactory();

    private static final String ISSUER = "rest_api_dietiestates25";
    private static final Algorithm algorithm = Algorithm.HMAC256(System.getenv("JWT_SECRET"));
    private static final JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer(ISSUER)
            .build();

    //WIP
    public void createCliente(String username, String email, String nome, String cognome, String password, String numeroTelefonico) {
        sessionFactory.inTransaction(session -> {
            Cliente cliente = new Cliente(username, email, nome, cognome, password, numeroTelefonico);
            session.persist(cliente);
        });
    }

    public String authenticateUser(String username, String password) throws IllegalArgumentException {
        Session session = sessionFactory.openSession();
        Utente utente =
                session.createSelectionQuery("from Utente where username like :username", Utente.class)
                .setParameter("username", username)
                .getSingleResultOrNull();
        if (utente == null || (!utente.verifyPassword(password))) {
            throw new IllegalArgumentException("Authentication failed");
        }
        session.close();

        return createJWT(username, TimeUnit.DAYS.toMillis(1));



    }

    public void createAgenteImmobiliare(String username, String email, String nome, String cognome, String password, String numeroTelefonico) {
        sessionFactory.inTransaction(session -> {
            AgenteImmobiliare agenteImmobiliare = new AgenteImmobiliare(username, email, nome, cognome, password, numeroTelefonico);
            session.persist(agenteImmobiliare);
        });
    }



    private String createJWT(String username, long ttlMillis) {

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
