package it.unina.rest_api_dietiestates25.tests;
import it.unina.rest_api_dietiestates25.controller.VisiteController;
import it.unina.rest_api_dietiestates25.model.AgenteImmobiliare;
import it.unina.rest_api_dietiestates25.model.Cliente;
import it.unina.rest_api_dietiestates25.model.ListinoImmobile;
import it.unina.rest_api_dietiestates25.model.Visita;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@ExtendWith(MockitoExtension.class)
public class PrenotaTest {

    @InjectMocks
    VisiteController visiteController;

    @Test
    void testListinoIsNull() {
        Cliente clienteMock = mock(Cliente.class);
        Instant now = Instant.now();

        assertThrows(IllegalArgumentException.class,
                () -> visiteController.prenota(clienteMock, null, now, "In Presenza"));
    }
    @Test
    void testDataOltreDueSettimane() {

        Cliente clienteMock = mock(Cliente.class);
        ListinoImmobile listinoMock = mock(ListinoImmobile.class);
        AgenteImmobiliare agenteMock = mock(AgenteImmobiliare.class);

        when(listinoMock.getCreatore()).thenReturn(agenteMock);
        when(agenteMock.getId()).thenReturn(1);

        Instant data = Instant.now().plus(Duration.ofDays(15));

        assertThrows(IllegalArgumentException.class,
                () -> visiteController.prenota(clienteMock, listinoMock, data, "In Videochiamata"));
    }

    @Test
    void testOrarioNonValido() {

        Cliente clienteMock = mock(Cliente.class);
        ListinoImmobile listinoMock = mock(ListinoImmobile.class);
        AgenteImmobiliare agenteMock = mock(AgenteImmobiliare.class);

        when(listinoMock.getCreatore()).thenReturn(agenteMock);
        when(agenteMock.getId()).thenReturn(1);

        Instant data = LocalDateTime
                .now()
                .plusDays(1)
                .withHour(20)
                .atZone(ZoneId.of("Europe/Rome"))
                .toInstant();

        assertThrows(IllegalArgumentException.class,
                () -> visiteController.prenota(clienteMock, listinoMock, data, "In Videochiamata"));
    }

    @Test
    void testSlotOccupato() {

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
                .isSlotDisponibile(anyInt(), any());

        assertThrows(IllegalArgumentException.class,
                () -> visiteController.prenota(clienteMock, listinoMock, dataValida, "In Presenza"));
    }

    @Test
    void testTuttoValido() {

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
                .isSlotDisponibile(anyInt(), any());

        Visita visita = visiteController.prenota(clienteMock, listinoMock, dataValida, "In Presenza");

        assertNotNull(visita);
    }
}