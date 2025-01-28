package it.unina.rest_api_dietiestates25.model;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;


import java.util.Set;

@Entity
public class AgenteImmobiliare extends Utente {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creatore")
    private Set<ListinoImmobile> immobiliGestiti;
}
