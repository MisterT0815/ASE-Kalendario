package kalendario.application.crud.benutzer;

import kalendario.application.session.KeinZugriffException;
import kalendario.application.session.Session;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.repositories.BenutzerRepository;
import kalendario.domain.repositories.SaveException;

import java.sql.SQLException;

public class BenutzerUpdate {

    BenutzerRepository benutzerRepository;
    Session session;


    public BenutzerUpdate(BenutzerRepository benutzerRepository, Session session) {
        this.benutzerRepository = benutzerRepository;
        this.session = session;
    }

    public void updatePasswort(String passwortAlt, String passwortNeu) throws KeinZugriffException, SaveException {
        BenutzerId benutzerId = session.getCurrentBenutzer().orElseThrow(KeinZugriffException::new);
        String name = session.getCurrentBenutzerName().orElseThrow(KeinZugriffException::new);
        try {
            if(!benutzerRepository.benutzerExistiert(name, passwortAlt)){
                throw new KeinZugriffException();
            }
        } catch (SQLException e) {
            throw new KeinZugriffException();
        }
        benutzerRepository.updatePasswortOf(benutzerId, passwortNeu);
        session.logout();
    }

    public void updateName(String neuerName) throws BenutzerNameExistiertException, SaveException, KeinZugriffException {
        BenutzerId benutzer = session.getCurrentBenutzer().orElseThrow(KeinZugriffException::new);
        try {
            if(benutzerRepository.benutzerNameExistiert(neuerName)){
                throw new BenutzerNameExistiertException();
            }
        } catch (SQLException e) {
            throw new SaveException(e);
        }
        benutzerRepository.updateNameOf(benutzer, neuerName);
        session.logout();
    }
}
