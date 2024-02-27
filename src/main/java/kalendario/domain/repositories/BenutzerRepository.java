package kalendario.domain.repositories;

import kalendario.domain.entities.benutzer.Benutzer;
import kalendario.domain.entities.benutzer.BenutzerId;

public interface BenutzerRepository {

    boolean benutzerNameExistiert(String name);
    BenutzerId neueId();
    void saveBenutzer(Benutzer benutzer) throws SaveException;
    boolean benutzerExistiert(String name, String passwordHashed);
}
