package kalendario.application.session;

import kalendario.application.crud.benutzer.BenutzerCreation;
import kalendario.application.crud.benutzer.BenutzerRead;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.repositories.SaveException;

import javax.security.auth.login.LoginException;
import java.util.Optional;

public class Session {

    private BenutzerId benutzer = null;
    BenutzerRead benutzerRead;
    BenutzerCreation benutzerCreation;

    public Session(BenutzerRead benutzerRead, BenutzerCreation benutzerCreation) {
        this.benutzerRead = benutzerRead;
        this.benutzerCreation = benutzerCreation;
    }

    public void login(String name, String password) throws LoginException {
        if(!benutzerRead.verifyBenutzer(name, password)){
            throw new LoginException("Fehler bei Login, checke Benutzername und Passwort");
        }
        benutzer = benutzerRead.getBenutzerIdOfName(name).orElseThrow();
    }

    public void signUp(String name, String password) throws SaveException, BenutzerCreation.BenutzerNameExistiertException {
        benutzer = benutzerCreation.createBenutzer(name, password).getId();
    }

    public void logout(){
        benutzer = null;
    }

    public Optional<BenutzerId> getCurrentBenutzer(){
        return Optional.ofNullable(benutzer);
    }

}
