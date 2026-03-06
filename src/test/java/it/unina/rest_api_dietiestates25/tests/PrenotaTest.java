package it.unina.rest_api_dietiestates25.tests;
import it.unina.rest_api_dietiestates25.controller.VisiteController;
import it.unina.rest_api_dietiestates25.model.AgenteImmobiliare;
import it.unina.rest_api_dietiestates25.model.Cliente;
import it.unina.rest_api_dietiestates25.model.ListinoImmobile;
import it.unina.rest_api_dietiestates25.model.Visita;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
public class PrenotaTest {

    VisiteController visiteController = spy(new VisiteController());

    @Test
    public void testListinoIsNull() {
        Cliente clienteMock = mock(Cliente.class);

        assertThrows(IllegalArgumentException.class,
                () -> visiteController.prenota(clienteMock, null, Instant.now(), "VISITA"));
    }

    @Test
    public void testDataOltreDueSettimane() {

        Cliente clienteMock = mock(Cliente.class);
        ListinoImmobile listinoMock = mock(ListinoImmobile.class);
        AgenteImmobiliare agenteMock = mock(AgenteImmobiliare.class);

        when(listinoMock.getCreatore()).thenReturn(agenteMock);
        when(agenteMock.getId()).thenReturn(1);

        Instant data = Instant.now().plus(Duration.ofDays(20));

        assertThrows(IllegalArgumentException.class,
                () -> visiteController.prenota(clienteMock, listinoMock, data, "VISITA"));
    }

    @Test
    public void testOrarioNonValido() {

        Cliente clienteMock = mock(Cliente.class);
        ListinoImmobile listinoMock = mock(ListinoImmobile.class);
        AgenteImmobiliare agenteMock = mock(AgenteImmobiliare.class);

        when(listinoMock.getCreatore()).thenReturn(agenteMock);
        when(agenteMock.getId()).thenReturn(1);

        Instant data = LocalDateTime
                .now()
                .plusDays(1)
                .withHour(22)
                .atZone(ZoneId.of("Europe/Rome"))
                .toInstant();

        assertThrows(IllegalArgumentException.class,
                () -> visiteController.prenota(clienteMock, listinoMock, data, "VISITA"));
    }

    @Test
    public void testSlotOccupato() {

        Cliente clienteMock = mock(Cliente.class);
        ListinoImmobile listinoMock = mock(ListinoImmobile.class);
        AgenteImmobiliare agenteMock = mock(AgenteImmobiliare.class);

        when(listinoMock.getCreatore()).thenReturn(agenteMock);
        when(agenteMock.getId()).thenReturn(1);

        Instant dataValida = LocalDateTime
                .now()
                .plusDays(1)
                .withHour(10)
                .atZone(ZoneId.of("Europe/Rome"))
                .toInstant();

        doReturn(false)
                .when(visiteController)
                .isSlotDisponibile(anyInt(), any(), any());

        assertThrows(IllegalArgumentException.class,
                () -> visiteController.prenota(clienteMock, listinoMock, dataValida, "VISITA"));
    }

    @Test
    public void testTuttoValido() {

        Cliente clienteMock = mock(Cliente.class);
        ListinoImmobile listinoMock = mock(ListinoImmobile.class);
        AgenteImmobiliare agenteMock = mock(AgenteImmobiliare.class);

        when(listinoMock.getCreatore()).thenReturn(agenteMock);
        when(agenteMock.getId()).thenReturn(1);

        Instant dataValida = LocalDateTime
                .now()
                .plusDays(1)
                .withHour(10)
                .atZone(ZoneId.of("Europe/Rome"))
                .toInstant();

        doReturn(true)
                .when(visiteController)
                .isSlotDisponibile(anyInt(), any(), any());

        Visita visita = visiteController.prenota(clienteMock, listinoMock, dataValida, "VISITA");

        assertNotNull(visita);
    }
}