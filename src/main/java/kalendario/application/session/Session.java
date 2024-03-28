package kalendario.application.session;

import kalendario.application.crud.benutzer.BenutzerCreation;
import kalendario.application.crud.benutzer.BenutzerNameExistiertException;
import kalendario.application.crud.benutzer.BenutzerRead;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.repositories.SaveException;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.Optional;

public class Session {

    private BenutzerId benutzer = null;
    private String benutzerName = null;
    BenutzerRead benutzerRead;
    BenutzerCreation benutzerCreation;

    public Session(BenutzerRead benutzerRead, BenutzerCreation benutzerCreation) {
        this.benutzerRead = benutzerRead;
        this.benutzerCreation = benutzerCreation;
    }

    public void login(String name, String password) throws LoginException {
        try {
            if(!benutzerRead.verifyBenutzer(name, password)){
                throw new LoginException("Fehler bei Login, checke Benutzername und Passwort");
            }
        } catch (SQLException e) {
            throw new LoginException("Fehler bei Login, checke Benutzername und Passwort");
        }
        benutzer = benutzerRead.getBenutzerIdOfName(name).orElseThrow();
        benutzerName = name;
    }

    public void signUp(String name, String password) throws SaveException, BenutzerNameExistiertException {
        benutzer = benutzerCreation.createBenutzer(name, password).getId();
        benutzerName = name;
    }

    public void logout(){
        benutzer = null;
        benutzerName = null;
    }

    public Optional<BenutzerId> getCurrentBenutzer(){
        return Optional.ofNullable(benutzer);
    }

    public Optional<String> getCurrentBenutzerName(){
        return Optional.ofNullable(benutzerName);
    }

}
