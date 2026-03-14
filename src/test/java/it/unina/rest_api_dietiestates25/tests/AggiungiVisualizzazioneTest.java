package it.unina.rest_api_dietiestates25.tests;

import it.unina.rest_api_dietiestates25.controller.AuthController;
import it.unina.rest_api_dietiestates25.controller.ListinoController;
import it.unina.rest_api_dietiestates25.model.Cliente;
import it.unina.rest_api_dietiestates25.model.ListinoImmobile;
import it.unina.rest_api_dietiestates25.model.RiepilogoAttivita;
import it.unina.rest_api_dietiestates25.model.VisualizzazioneImmobile;
import it.unina.rest_api_dietiestates25.Database;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AggiungiVisualizzazioneTest {

    @Mock
    private Database databaseMock;
    @Mock
    private AuthController authControllerMock;
    @Mock
    private Session sessionMock;
    @Mock
    private Cliente clienteMock;
    @Mock
    private RiepilogoAttivita riepilogoMock;
    @Mock
    private ListinoImmobile listinoMock;
    @Mock
    private Query<ListinoImmobile> queryMock;
    @InjectMocks
    private ListinoController listinoController;

    @BeforeEach
    void setup() {
        lenient().when(databaseMock.getSession()).thenReturn(sessionMock);
        lenient().when(clienteMock.getRiepilogo()).thenReturn(riepilogoMock);
        lenient().when(authControllerMock.getCliente(anyString())).thenReturn(clienteMock);

        lenient().when(sessionMock.createSelectionQuery(anyString(), eq(ListinoImmobile.class)))
                .thenReturn(queryMock);
        lenient().when(queryMock.setParameter(anyString(), any())).thenReturn(queryMock);
        lenient().when(queryMock.getSingleResultOrNull()).thenReturn(listinoMock);

        lenient().when(riepilogoMock.findVisualizzazione(listinoMock)).thenReturn(null);
    }

    @Test
    void testUsernameNull() {
        assertThrows(IllegalArgumentException.class,
                () -> listinoController.aggiungiVisualizzazione(1, null));
    }

    @Test
    void testUsernameStringaVuota() {
        assertThrows(IllegalArgumentException.class,
                () -> listinoController.aggiungiVisualizzazione(1, ""));
    }

    @Test
    void testUtenteNonTrovato() {
        when(authControllerMock.getCliente("utenteInesistente")).thenReturn(null);
        assertThrows(IllegalArgumentException.class,
                () -> listinoController.aggiungiVisualizzazione(1, "utenteInesistente"));
    }

    @Test
    void testListinoIdLessThanZeroOrZero() {
        assertThrows(IllegalArgumentException.class,
                () -> listinoController.aggiungiVisualizzazione(0, "luigi"));
    }

    @Test
    void testListinoIdInvalido() {
        when(queryMock.getSingleResultOrNull()).thenReturn(null); // listino non trovato
        assertThrows(IllegalArgumentException.class,
                () -> listinoController.aggiungiVisualizzazione(999, "luigi"));
    }

    @Test
    void testTuttoValido() {
        // Chiamo il metodo reale, ora getListino restituisce listinoMock
        listinoController.aggiungiVisualizzazione(1, "utenteValido");

        // Verifico le interazioni con la session
        verify(sessionMock).persist(any(VisualizzazioneImmobile.class));
        verify(sessionMock).merge(clienteMock);
        verify(sessionMock).merge(riepilogoMock);
        verify(sessionMock).persist(listinoMock);
        verify(listinoMock).setNumeroVisualizzazioni(anyInt());
    }
}