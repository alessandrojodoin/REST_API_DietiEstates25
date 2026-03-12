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
class AuthenticateUserTest {
    @Mock Database database;
    @Mock Session session;
    @Mock
    Query<Utente> query;

    @InjectMocks
    AuthController authController;

    @Test
    void testUsernameNull(){
        String password = "mioSegreto";

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> authController.authenticateUser(null, password));

        assertEquals("Authentication failed", ex.getMessage());
    }

    @Test
    void testPasswordNull(){
        String username = "mioUsername";

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> authController.authenticateUser(username, null));

        assertEquals("Authentication failed", ex.getMessage());
    }


    @Test
    void testPasswordVuota(){
        String username = "mioUsername";

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> authController.authenticateUser(username, ""));

        assertEquals("Authentication failed", ex.getMessage());
    }

    @Test
    void testUsenameVuota(){
        String password = "mioSegreto";

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> authController.authenticateUser("", password));

        assertEquals("Authentication failed", ex.getMessage());
    }

    @Test
    void testUsernameInvalido() {

        String username = "utenteInesistente";
        String password = "password";

        when(database.getSession()).thenReturn(session);

        when(session.createSelectionQuery(anyString(), eq(Utente.class)))
                .thenReturn(query);

        when(query.setParameter(anyString(), any()))
                .thenReturn(query);

        //  Nessun utente trovato
        when(query.getSingleResultOrNull())
                .thenReturn(null);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> authController.authenticateUser(username, password));

        assertEquals("Authentication failed", ex.getMessage());
    }
    @Test
    void testPasswordInvalida() {

        String username = "utenteEsistente";
        String password = "passwordSbagliata";

        when(database.getSession()).thenReturn(session);

        when(session.createSelectionQuery(anyString(), eq(Utente.class)))
                .thenReturn(query);

        when(query.setParameter(anyString(), any()))
                .thenReturn(query);

        //  Utente trovato
        Utente utenteMock = mock(Utente.class);

        when(query.getSingleResultOrNull())
                .thenReturn(utenteMock);

        //  Password sbagliata
        when(utenteMock.verifyPassword(password))
                .thenReturn(false);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> authController.authenticateUser(username, password));

        assertEquals("Authentication failed", ex.getMessage());
    }

    @Test
    void testPasswordAndUsenameValidi(){

        String username = "mioUsername";
        String password = "miaPassword";

        when(database.getSession()).thenReturn(session);

        // mock della query
        when(session.createSelectionQuery(anyString(), eq(Utente.class)))
                .thenReturn(query);

        when(query.setParameter(anyString(), any()))
                .thenReturn(query);

        Utente utenteMock = mock(Utente.class);

        when(query.getSingleResultOrNull())
                .thenReturn(utenteMock);

        when(utenteMock.verifyPassword(password))
                .thenReturn(true);

        when(utenteMock.getUtenteTypeAsSting())
                .thenReturn("UTENTE");

        AuthController controller =
                new AuthController(database, Algorithm.HMAC256("test-jwt"));

        String jwt = controller.authenticateUser(username, password);

        assertNotNull(jwt);
    }
}
