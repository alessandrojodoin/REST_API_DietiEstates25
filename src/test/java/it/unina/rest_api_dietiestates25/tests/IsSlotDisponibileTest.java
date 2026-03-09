package it.unina.rest_api_dietiestates25.tests;
import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.controller.AuthController;
import it.unina.rest_api_dietiestates25.controller.VisiteController;
import it.unina.rest_api_dietiestates25.model.AgenteImmobiliare;
import it.unina.rest_api_dietiestates25.model.Utente;
import org.hibernate.query.SelectionQuery;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import org.hibernate.Session;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IsSlotDisponibileTest {

    @Mock
    Database database;
    @Mock Session session;

    @Mock
    SelectionQuery<Long> queryMock;


    @Mock
    AuthController authController;

    @InjectMocks
    VisiteController controller;

    @Test
    void testAgenteIdLessThanZero() {
        Instant now = Instant.now();

        when(database.getSession()).thenReturn(session);

        lenient().when(session.createSelectionQuery(anyString(), eq(Long.class)))
                .thenReturn(queryMock);

        lenient().when(queryMock.setParameter(anyString(), any()))
                .thenReturn(queryMock);

        lenient().when(queryMock.getSingleResult())
                .thenReturn(0L);

        assertThrows(IllegalArgumentException.class,
                () -> controller.isSlotDisponibile(0, now));

    }

    @Test
    void testAgenteIdNonValido() {

        int agenteId = 1;
        Instant now = Instant.now();

        lenient().when(database.getSession()).thenReturn(session);

        lenient().when(session.createSelectionQuery(anyString(), eq(Long.class)))
                .thenReturn(queryMock);

        lenient().when(queryMock.setParameter(anyString(), any()))
                .thenReturn(queryMock);

        lenient().when(queryMock.getSingleResult())
                .thenReturn(0L);

        try (MockedStatic<Database> mockedDb = mockStatic(Database.class);
             MockedConstruction<AuthController> mockedAuth =
                     mockConstruction(AuthController.class,
                             (mock, context) -> {
                                 when(mock.getUtente(anyInt()))
                                         .thenReturn(null);
                             })) {

            mockedDb.when(Database::getInstance).thenReturn(database);

            assertThrows(IllegalArgumentException.class,
                    () -> controller.isSlotDisponibile(agenteId, now));
        }



    }
    @Test
    void testSlotDisponibile() {
        when(database.getSession()).thenReturn(session);

        when(session.createSelectionQuery(anyString(), eq(Long.class)))
                .thenReturn(queryMock);

        when(queryMock.setParameter(anyString(), any()))
                .thenReturn(queryMock);

        when(queryMock.getSingleResult())
                .thenReturn(0L);


        try (MockedStatic<Database> mockedDb = mockStatic(Database.class);
             MockedConstruction<AuthController> mockedAuth =
                     mockConstruction(AuthController.class,
                             (mock, context) -> {
                                 when(mock.getUtente(anyInt()))
                                         .thenReturn(mock(AgenteImmobiliare.class));
                             })) {

            mockedDb.when(Database::getInstance).thenReturn(database);

            boolean result = controller.isSlotDisponibile(1, Instant.now());

            assertTrue(result);
        }




    }

    @Test
    void testSlotOccupato() {
        when(database.getSession()).thenReturn(session);

        when(session.createSelectionQuery(anyString(), eq(Long.class)))
                .thenReturn(queryMock);

        when(queryMock.setParameter(anyString(), any()))
                .thenReturn(queryMock);

        when(queryMock.getSingleResult())
                .thenReturn(3L);


        try (MockedStatic<Database> mockedDb = mockStatic(Database.class);
             MockedConstruction<AuthController> mockedAuth =
                     mockConstruction(AuthController.class,
                             (mock, context) -> {
                                 when(mock.getUtente(anyInt()))
                                         .thenReturn(mock(AgenteImmobiliare.class));
                             })) {

            mockedDb.when(Database::getInstance).thenReturn(database);

            boolean result = controller.isSlotDisponibile(1, Instant.now());

            assertFalse(result);
        }

    }

    @Test
    void testDataOraNull() {

        assertThrows(NullPointerException.class,
                () -> controller.isSlotDisponibile(1, null));
    }
}
