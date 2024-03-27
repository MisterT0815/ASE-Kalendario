package kalendario.application.crud.benutzer;

import kalendario.application.session.KeinZugriffException;
import kalendario.application.session.Session;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.repositories.BenutzerRepository;
import kalendario.domain.repositories.SaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class BenutzerUpdateTest {

    BenutzerRepository benutzerRepository = mock();
    Session session = mock();
    BenutzerId benutzerId = mock();
    String name = "name";
    String neuerName = "neuer Name";
    String passwort = "passwort";

    String neuesPasswort = "neu";

    BenutzerUpdate benutzerUpdate;

    @BeforeEach
    void init(){
        benutzerUpdate = new BenutzerUpdate(benutzerRepository, session);
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        when(session.getCurrentBenutzerName()).thenReturn(Optional.of(name));
        when(benutzerRepository.benutzerExistiert(name, passwort)).thenReturn(true);
    }

    @Test
    void updatePasswortSollPasswortDesAktuellenNutzersAendern() throws SaveException, KeinZugriffException {
        benutzerUpdate.updatePasswort(passwort, neuesPasswort);
        verify(benutzerRepository).updatePasswortOf(benutzerId, neuesPasswort);
    }

    @Test
    void updatePasswortSollFehlschlagenWennAltesPasswortFalsch() {
        assertThrows(KeinZugriffException.class, () -> benutzerUpdate.updatePasswort(neuesPasswort, neuesPasswort));
    }

    @Test
    void updatePasswortSollFehlschagenWennKeinBenutzerAngemeldetIst(){
        when(session.getCurrentBenutzer()).thenReturn(Optional.empty());
        assertThrows(KeinZugriffException.class, () -> benutzerUpdate.updatePasswort(passwort, neuesPasswort));
    }

    @Test
    void updateNameSollPasswortAktualisierenUndBenutzerDannAusloggen() throws SaveException, KeinZugriffException, BenutzerNameExistiertException {
        benutzerUpdate.updateName(neuerName);
        verify(benutzerRepository).updateNameOf(benutzerId, neuerName);
        verify(session).logout();
    }

    @Test
    void updateNameSollVerfuegbarkeitDesNamensTesten() throws SaveException, SQLException {
        when(benutzerRepository.benutzerNameExistiert(neuerName)).thenReturn(true);
        assertThrows(BenutzerNameExistiertException.class, () -> benutzerUpdate.updateName(neuerName));
        verify(session, never()).logout();
        verify(benutzerRepository, never()).updateNameOf(any(), any());
    }

    @Test
    void updateNameSollFehlschagenWennKeinBenutzerAngemeldetIst(){
        when(session.getCurrentBenutzer()).thenReturn(Optional.empty());
        assertThrows(KeinZugriffException.class, () -> benutzerUpdate.updateName(neuerName));
    }

}
