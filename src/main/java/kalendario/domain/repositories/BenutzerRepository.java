package kalendario.domain.repositories;

import kalendario.domain.entities.benutzer.Benutzer;
import kalendario.domain.entities.benutzer.BenutzerId;

import java.sql.SQLException;

public interface BenutzerRepository {

    boolean benutzerNameExistiert(String name) throws SQLException;
    BenutzerId neueId();
    void saveBenutzer(Benutzer benutzer) throws SaveException;
    boolean benutzerExistiert(String name, String passwort);
    BenutzerId getIdOfName(String name);
    void updatePasswortOf(BenutzerId benutzerId, String neuesPasswort) throws SaveException;
    void updateNameOf(BenutzerId benutzerId, String neuerName) throws SaveException;
    String getBenutzerNameOfId(BenutzerId benutzerId);
}
