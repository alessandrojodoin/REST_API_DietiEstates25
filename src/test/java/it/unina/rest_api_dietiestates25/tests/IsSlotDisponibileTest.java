package it.unina.rest_api_dietiestates25.tests;
import it.unina.rest_api_dietiestates25.controller.VisiteController;
import org.hibernate.query.SelectionQuery;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import org.hibernate.Session;
import org.mockito.Mock;


public class IsSlotDisponibileTest {

    @Mock Session session;

    @Mock
    SelectionQuery<Long> queryMock;

    @Test
    void testAgenteIdNonValido() {

        VisiteController controller = new VisiteController();

        when(session.createSelectionQuery(anyString(), eq(Long.class)))
                .thenReturn(queryMock);

        when(queryMock.setParameter(anyString(), any()))
                .thenReturn(queryMock);

        when(queryMock.getSingleResult())
                .thenReturn(0L);

        boolean result = controller.isSlotDisponibile(-1, Instant.now(), session);

        assertTrue(result);
    }

    @Test
    void testCountZero() {

        VisiteController controller = new VisiteController();


        when(session.createSelectionQuery(anyString(), eq(Long.class)))
                .thenReturn(queryMock);

        when(queryMock.setParameter(anyString(), any()))
                .thenReturn(queryMock);

        when(queryMock.getSingleResult())
                .thenReturn(0L);

        boolean result = controller.isSlotDisponibile(1, Instant.now(), session);

        assertTrue(result);
    }

    @Test
    void testCountGreaterThanZero() {

        VisiteController controller = new VisiteController();


        when(session.createSelectionQuery(anyString(), eq(Long.class)))
                .thenReturn(queryMock);

        when(queryMock.setParameter(anyString(), any()))
                .thenReturn(queryMock);

        when(queryMock.getSingleResult())
                .thenReturn(3L);

        boolean result = controller.isSlotDisponibile(1, Instant.now(), session);

        assertFalse(result);
    }

    @Test
    void testQueryException() {

        VisiteController controller = new VisiteController();

        when(session.createSelectionQuery(anyString(), eq(Long.class)))
                .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class,
                () -> controller.isSlotDisponibile(1, Instant.now(), session));
    }

    @Test
    void testSessionNull() {

        VisiteController controller = new VisiteController();

        assertThrows(NullPointerException.class,
                () -> controller.isSlotDisponibile(1, Instant.now(), null));
    }

    @Test
    void testDataOraNull() {

        VisiteController controller = new VisiteController();

        assertThrows(NullPointerException.class,
                () -> controller.isSlotDisponibile(1, null, session));
    }
}
