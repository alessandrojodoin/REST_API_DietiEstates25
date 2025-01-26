package it.unina.rest_api_dietiestates25;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.util.Set;

@Entity
public class AgenteImmobiliare extends Utente{

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creatore")
    private Set<ListinoImmobile> immobiliGestiti;
}
