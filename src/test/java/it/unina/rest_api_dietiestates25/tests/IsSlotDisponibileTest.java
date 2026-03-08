package it.unina.rest_api_dietiestates25.tests;
import it.unina.rest_api_dietiestates25.Database;
import it.unina.rest_api_dietiestates25.controller.VisiteController;
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
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class IsSlotDisponibileTest {

    @Mock
    Database database;
    @Mock Session session;

    @Mock
    SelectionQuery<Long> queryMock;

    @InjectMocks
    VisiteController controller;

    @Test
    void testAgenteIdLessThanZero() {

        when(database.getSession()).thenReturn(session);

        when(session.createSelectionQuery(anyString(), eq(Long.class)))
                .thenReturn(queryMock);

        when(queryMock.setParameter(anyString(), any()))
                .thenReturn(queryMock);

        when(queryMock.getSingleResult())
                .thenReturn(0L);


        assertThrows(IllegalArgumentException.class,
                () -> controller.isSlotDisponibile(0, Instant.now()));

    }

    @Test
    void testAgenteIdNonValido() {

        int agenteId = 1;

        when(database.getSession()).thenReturn(session);

        when(session.createSelectionQuery(anyString(), eq(Long.class)))
                .thenReturn(queryMock);

        when(queryMock.setParameter(anyString(), any()))
                .thenReturn(queryMock);

        when(queryMock.getSingleResult())
                .thenReturn(0L);

        assertThrows(IllegalArgumentException.class,
                () -> controller.isSlotDisponibile(agenteId, Instant.now()));

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

        boolean result = controller.isSlotDisponibile(1, Instant.now());

        assertTrue(result);
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

        boolean result = controller.isSlotDisponibile(1, Instant.now());

        assertFalse(result);
    }

    @Test
    void testDataOraNull() {

        assertThrows(NullPointerException.class,
                () -> controller.isSlotDisponibile(1, null));
    }
}
