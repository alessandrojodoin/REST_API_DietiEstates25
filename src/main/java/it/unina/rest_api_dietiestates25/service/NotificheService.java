package it.unina.rest_api_dietiestates25.service;

import it.unina.rest_api_dietiestates25.model.Visita;
import jakarta.ws.rs.core.Response;

public class NotificheService {

    private final EmailService emailService = new EmailService();

    public void nuovaPrenotazione(Visita visita, String emailAgente) {
        try {
            emailService.send(
                    emailAgente,
                    "Nuova visita prenotata",
                    "È stata prenotata una visita per il giorno: " + visita.getDataOra()
            );
        } catch (Exception e) {
            System.out.println("Error: email");
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
            System.out.println("Error: confirm email");
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
            System.out.println("Error: delete email");
        }
    }
}
