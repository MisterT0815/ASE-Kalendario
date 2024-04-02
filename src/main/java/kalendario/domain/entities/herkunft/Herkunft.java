package kalendario.domain.entities.herkunft;


import kalendario.domain.entities.benutzer.BenutzerId;

public interface Herkunft {
    BenutzerId getBesitzerId();
    HerkunftId getId();
}
