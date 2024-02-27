package kalendario.application.session;


import kalendario.application.crud.benutzer.BenutzerCreation;
import kalendario.application.crud.benutzer.BenutzerRead;
import kalendario.domain.entities.benutzer.Benutzer;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.repositories.SaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.security.auth.login.LoginException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SessionTest {

    BenutzerCreation benutzerCreation = mock();
    BenutzerRead benutzerRead = mock();
    Benutzer benutzer = mock();
    BenutzerId benutzerId = mock();
    String name = "name";
    String passwort = "passwort";
    Session session;

    @BeforeEach
    void init(){
        session = new Session(benutzerRead, benutzerCreation);
    }

    @Test
    void loginSollBenutzerVonBenutzerReadLesenUndBenutzerIdSetzenFallsErfolgreich() throws LoginException {
        when(benutzerRead.verifyBenutzer(name, passwort)).thenReturn(true);
        when(benutzerRead.getBenutzerIdOfName(name)).thenReturn(Optional.ofNullable(benutzerId));
        session.login(name, passwort);
        assertEquals(benutzerId, session.getCurrentBenutzer().get());
    }

    @Test
    void loginSollExceptionWerfenWennBenutzerNichtExistiert() {
        when(benutzerRead.verifyBenutzer(name, passwort)).thenReturn(false);
        when(benutzerRead.getBenutzerIdOfName(name)).thenReturn(Optional.empty());
        assertThrows(LoginException.class, () -> session.login(name, passwort));
    }

    @Test
    void signUpSollBenutzerErstellenUndBenutzerAufAktuellenStellen() throws SaveException, BenutzerCreation.BenutzerNameExistiertException {
        when(benutzerCreation.createBenutzer(name, passwort)).thenReturn(benutzer);
        when(benutzer.getId()).thenReturn(benutzerId);
        session.signUp(name, passwort);
        assertEquals(benutzerId, session.getCurrentBenutzer().get());
    }

    @Test
    void logoutSollAktuellenBenutzerEntfernen() throws LoginException {
        when(benutzerRead.verifyBenutzer(name, passwort)).thenReturn(true);
        when(benutzerRead.getBenutzerIdOfName(name)).thenReturn(Optional.ofNullable(benutzerId));
        session.login(name, passwort);
        assertEquals(benutzerId, session.getCurrentBenutzer().get());
        session.logout();
        assertTrue(session.getCurrentBenutzer().isEmpty());
    }



}

