package kalendario.application.crud.benutzer;

import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.repositories.BenutzerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class BenutzerReadTest {

    BenutzerRepository benutzerRepository = mock();
    String name = "name";
    String password = "password";
    BenutzerId benutzerId = mock();
    BenutzerRead benutzerRead;

    @BeforeEach
    void init(){
        benutzerRead = new BenutzerRead(benutzerRepository);
    }

    @Test
    void verifyBenutzerSollBenutzerExistiertAnBenutzerRepositoryDelegieren(){
        when(benutzerRepository.benutzerExistiert(name, password)).thenReturn(true);
        assertTrue(benutzerRead.verifyBenutzer(name, password));
        when(benutzerRepository.benutzerExistiert(name, password)).thenReturn(false);
        assertFalse(benutzerRead.verifyBenutzer(name, password));
    }

    @Test
    void getBenutzerIdOfNameSollAnBenutzerRepositoryDelegieren(){
        when(benutzerRepository.getIdOfName(name)).thenReturn(benutzerId);
        assertEquals(benutzerId, benutzerRead.getBenutzerIdOfName(name).get());
        when(benutzerRepository.getIdOfName(name)).thenReturn(null);
        assertTrue(benutzerRead.getBenutzerIdOfName(name).isEmpty());
    }

    @Test
    void getBenutzerNameOfIdSollAnBenutzerRepositoryDelegieren(){
        when(benutzerRepository.getBenutzerNameOfId(benutzerId)).thenReturn(name);
        assertEquals(name, benutzerRead.getBenutzerNameOfId(benutzerId).get());
        when(benutzerRepository.getBenutzerNameOfId(benutzerId)).thenReturn(null);
        assertTrue(benutzerRead.getBenutzerNameOfId(benutzerId).isEmpty());
    }

    @Test
    void benutzerExistiertSollPerIdOfNameExistenzUeberpruefe(){
        when(benutzerRepository.getIdOfName(name)).thenReturn(benutzerId);
        assertTrue(benutzerRead.benutzerExistiert(name));
        when(benutzerRepository.getIdOfName(name)).thenReturn(null);
        assertFalse(benutzerRead.benutzerExistiert(name));
    }

}
