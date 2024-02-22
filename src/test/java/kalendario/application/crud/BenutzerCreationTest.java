package kalendario.application.crud;

import kalendario.domain.entities.benutzer.Benutzer;
import kalendario.domain.repositories.BenutzerRepository;
import kalendario.domain.repositories.SaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class BenutzerCreationTest {

    String name = "Name";
    String passwordHashed = "PW";
    BenutzerRepository benutzerRepository = mock();
    BenutzerCreation benutzerCreation;

    @BeforeEach
    void init(){
        benutzerCreation = new BenutzerCreation(benutzerRepository);
    }

    @Test
    void createBenutzerSollBenutzerRepositoryZumSaveAufrufen() throws SaveException, BenutzerCreation.BenutzerNameExistiertException {
        Benutzer benutzer = benutzerCreation.createBenutzer(name, passwordHashed);
        verify(benutzerRepository).saveBenutzer(benutzer);
    }

    @Test
    void createBenutzerSollExceptionWerfenWennBenutzerMitSelbenNameBereitsExistiert(){
        when(benutzerRepository.benutzerNameExistiert(name)).thenReturn(true);
        assertThrows(BenutzerCreation.BenutzerNameExistiertException.class, () -> benutzerCreation.createBenutzer(name, passwordHashed));
    }

    @Test
    void createBenutzerSollExceptionWerfenWennSpeicherungInRepositoryFehlschlaegt() throws SaveException {
        doThrow(SaveException.class).when(benutzerRepository).saveBenutzer(any());
        assertThrows(SaveException.class, () -> benutzerCreation.createBenutzer(name, passwordHashed));
    }


}
