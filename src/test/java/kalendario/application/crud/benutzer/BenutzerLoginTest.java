package kalendario.application.crud.benutzer;

import kalendario.domain.repositories.BenutzerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class BenutzerLoginTest {

    BenutzerRepository benutzerRepository = mock();
    String name = "name";
    String password = "password";
    BenutzerLogin benutzerLogin;

    @BeforeEach
    void init(){
        benutzerLogin = new BenutzerLogin(benutzerRepository);
    }

    @Test
    void verifyBenutzerSollBenutzerExistiertAnBenutzerRepositoryDelegieren(){
        when(benutzerRepository.benutzerExistiert(name, password)).thenReturn(true);
        assertTrue(benutzerLogin.verifyBenutzer(name, password));
        when(benutzerRepository.benutzerExistiert(name, password)).thenReturn(false);
        assertFalse(benutzerLogin.verifyBenutzer(name, password));
    }

}
