package it.unina.rest_api_dietiestates25.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.model.*;

import it.unina.rest_api_dietiestates25.service.NotificheService;
import org.hibernate.Session;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AuthController {

    private static final String ISSUER = "rest_api_dietiestates25";
    private final Database database = Database.getInstance();
    private static final Algorithm algorithm = Algorithm.HMAC256(System.getenv("JWT_SECRET"));
    private static final Logger logger = LoggerFactory.getLogger(NotificheService.class);

    public static boolean validateToken(String token){

        final JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build();
        try {
            //Accetta pure se l'utente non esist e piu'
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            logger.info(e.getMessage());
            return false;
        }
    }


    public static String getUsernameClaim(String token) {
        try {

            final JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getClaim("username").asString();
        } catch (JWTVerificationException e) {
            logger.info(e.getMessage());
            return null;
        }
    }






    public void createCliente(String username, String email, String nome, String cognome, String password, String numeroTelefonico) {
        Session session = database.getSession();
        Cliente cliente = new Cliente(username, email, nome, cognome, password, numeroTelefonico);
        session.persist(cliente);

    }

    public void createAmministratore(String username, String email, String nome, String cognome, String password, String numeroTelefonico, String agenziaImmobiliare){

        Session session = database.getSession();
        AmministratoreAgenzia amministratore= new AmministratoreAgenzia(username, email, nome, cognome, password, numeroTelefonico, agenziaImmobiliare);
        session.persist(amministratore);
    }

    public void createAmministratoreSupporto(String username, String email, String nome, String cognome, String password, String numeroTelefonico, String agenziaImmobiliare){

        Session session = database.getSession();
        AccountSupporto amministratoreSupporto= new AccountSupporto(username, email, nome, cognome, password, numeroTelefonico, agenziaImmobiliare);
        session.persist(amministratoreSupporto);
    }

    public void createAgenteImmobiliare(String username, String email, String nome, String cognome, String password, String numeroTelefonico, String agenziaImmobiliare) {

        Session session = database.getSession();
        AgenteImmobiliare agenteImmobiliare = new AgenteImmobiliare(username, email, nome, cognome, password, numeroTelefonico, agenziaImmobiliare);
        session.persist(agenteImmobiliare);

    }

    public AgenteImmobiliare getAgenteImmobiliare(String username){
        Session session = database.getSession();
        return session.createSelectionQuery("from AgenteImmobiliare where username like :username", AgenteImmobiliare.class)
                        .setParameter("username", username)
                        .getSingleResultOrNull();
    }
    public Cliente getCliente(String username){
        Session session = database.getSession();
        return session.createSelectionQuery("from Cliente where username like :username", Cliente.class)
                .setParameter("username", username)
                .getSingleResultOrNull();
    }

    public AmministratoreAgenzia getAmministratore(String username){
        Session session = database.getSession();
        return session.createSelectionQuery("from AmministratoreAgenzia where username like :username", AmministratoreAgenzia.class)
                .setParameter("username", username)
                .getSingleResultOrNull();
    }

    public Utente getUtente(String username){
        Session session = database.getSession();
        return session.createSelectionQuery("from Utente where username like :username", Utente.class)
                .setParameter("username", username)
                .getSingleResultOrNull();
    }

    public Utente getUtente(int userId){
        Session session = database.getSession();
        return session.createSelectionQuery("from Utente where id = :id", Utente.class)
                .setParameter("id", userId)
                .getSingleResultOrNull();
    }

    public String authenticateUser(String username, String password) throws IllegalArgumentException {
        Session session = database.getSession();

        Utente utente =
                session.createSelectionQuery("from Utente where username like :username", Utente.class)
                .setParameter("username", username)
                .getSingleResultOrNull();
        if (utente == null || (!utente.verifyPassword(password))) {
            throw new IllegalArgumentException("Authentication failed");
        }

        boolean googleLinked = false;

        if (utente instanceof AgenteImmobiliare) {
            AgenteImmobiliare agente = (AgenteImmobiliare) utente;
            googleLinked = agente.isGoogleLinked();
        }

        return createJWT(username, utente.getUtenteTypeAsSting(), googleLinked, TimeUnit.DAYS.toMillis(1));
    }

    private String createJWT(String username, String userType,  boolean googleLinked, long ttlMillis) {



        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim("username", username)
                .withClaim("userType", userType)
                .withClaim("googleLinked", googleLinked)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + ttlMillis))
                .withJWTId(UUID.randomUUID().toString())
                .sign(algorithm);


    }

    public String createJwtWithGoogleLinked(String username, String userType, long ttlMillis) {
        return createJWT(username, userType, true, ttlMillis);
    }



    public void modificaAmministratore(String oldUsername, String newUsername, String password){

        Session session = database.getSession();

        AmministratoreAgenzia amministratore= this.getAmministratore(oldUsername);
        amministratore.setUsername(newUsername);
        amministratore.setPassword(password);

        session.merge(amministratore);
    }



}
