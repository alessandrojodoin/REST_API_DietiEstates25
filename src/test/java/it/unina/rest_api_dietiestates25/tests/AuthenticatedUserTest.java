package it.unina.rest_api_dietiestates25.tests;
import com.auth0.jwt.algorithms.Algorithm;
import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.controller.AuthController;
import it.unina.rest_api_dietiestates25.model.Utente;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import org.hibernate.query.Query;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthenticatedUserTest {
    @Mock Database database;
    @Mock Session session;
    @Mock
    Query<Utente> query;

    @InjectMocks
    AuthController authController;

    @Test
    public void testUsernameNull(){
        String password = "mioSegreto";

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> authController.authenticateUser(null, password));

        assertEquals("Username or password null", ex.getMessage());
    }

    @Test
    public void testPasswordNull(){
        String username = "mioUsername";

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> authController.authenticateUser(username, null));

        assertEquals("Username or password null", ex.getMessage());
    }


    @Test
    public void testPasswordVuota(){
        String username = "mioUsername";

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> authController.authenticateUser(username, ""));

        assertEquals("Authentication failed", ex.getMessage());
    }

    @Test
    public void testUsenameVuota(){
        String password = "mioSegreto";

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> authController.authenticateUser("", password));

        assertEquals("Authentication failed", ex.getMessage());
    }


    @Test
    public void testUsernameInvalido(){
        String username = "utenteInesistente";
        String password = "password";

        when(database.getSession()).thenReturn(session);

        when(session.createSelectionQuery(anyString(), eq(Utente.class)))
                .thenReturn(query);

        when(query.setParameter(anyString(), any()))
                .thenReturn(query);

        //  Simuliamo che il DB NON trovi l'utente
        when(query.getSingleResultOrNull())
                .thenReturn(null);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> authController.authenticateUser(username, password));

        assertEquals("Authentication failed", ex.getMessage());
    }

    @Test
    public void testPasswordInvalida(){
        String username = "utenteEsistente";
        String password = "passwordSbagliata";

        when(database.getSession()).thenReturn(session);

        when(session.createSelectionQuery(anyString(), eq(Utente.class)))
                .thenReturn(query);

        when(query.setParameter(anyString(), any()))
                .thenReturn(query);

        //  Creiamo un utente mock
        Utente utenteMock = mock(Utente.class);

        when(query.getSingleResultOrNull())
                .thenReturn(utenteMock);

        // Password sbagliata
        when(utenteMock.verifyPassword(password))
                .thenReturn(false);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> authController.authenticateUser(username, password));

        assertEquals("Authentication failed", ex.getMessage());
    }

    @Test
    public void testPasswordAndUsenameValidi(){
        String username = "mioUsername";
        String password = "miaPassword";
        AuthController authController =
                new AuthController(Algorithm.HMAC256("test-jwt"));

        String jwt = authController.authenticateUser(username, password);

        assertNotNull(jwt);


    }
}
