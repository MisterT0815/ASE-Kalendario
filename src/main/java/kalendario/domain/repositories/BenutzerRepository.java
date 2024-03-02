package kalendario.domain.repositories;

import kalendario.domain.entities.benutzer.Benutzer;
import kalendario.domain.entities.benutzer.BenutzerId;

public interface BenutzerRepository {

    boolean benutzerNameExistiert(String name);
    BenutzerId neueId();
    void saveBenutzer(Benutzer benutzer) throws SaveException;
    boolean benutzerExistiert(String name, String passwort);
    BenutzerId getIdOfName(String name);
    void updatePasswortOf(BenutzerId benutzerId, String neuesPasswort) throws SaveException;
    void updateNameOf(BenutzerId benutzerId, String neuerName) throws SaveException;
}
