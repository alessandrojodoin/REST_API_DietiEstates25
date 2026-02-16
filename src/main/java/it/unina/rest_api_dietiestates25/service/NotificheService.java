package it.unina.rest_api_dietiestates25.service;

import it.unina.rest_api_dietiestates25.model.Visita;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificheService {

    private final EmailService emailService = new EmailService();
    private static final Logger logger = LoggerFactory.getLogger(NotificheService.class);

    public void nuovaPrenotazione(Visita visita, String emailAgente) {
        try {
            emailService.send(
                    emailAgente,
                    "Nuova visita prenotata",
                    "È stata prenotata una visita per il giorno: " + visita.getDataOra()
            );
        } catch (Exception e) {
            logger.info("Error: email");
        }
    }

    public void confermaVisita(Visita visita, String emailCliente) {
        try {
            emailService.send(
                    emailCliente,
                    "Visita confermata",
                    "La tua visita del " + visita.getDataOra() + " è stata confermata."
            );
        } catch (Exception e) {
            logger.info("Error: confirm email");
        }
    }

    public void rifiutaVisita(Visita visita, String emailCliente) {
        try {
            emailService.send(
                    emailCliente,
                    "Visita rifiutata",
                    "La tua visita del " + visita.getDataOra() + " è stata rifiutata."
            );
        } catch (Exception e) {
            logger.info("Error: delete email");
        }
    }
}
