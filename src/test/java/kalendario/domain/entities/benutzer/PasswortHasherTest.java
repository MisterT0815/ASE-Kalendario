package kalendario.domain.entities.benutzer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PasswortHasherTest {

    PasswortHasher passwortHasher;

    @BeforeEach
    void init() throws NoSuchAlgorithmException {
        passwortHasher = new PasswortHasher();
    }

    @Test
    void selberHashBeiGleichenStrings(){
        String passwort = "Passwort";
        String passwortHashed = passwortHasher.hashPasswort(passwort);
        assertEquals(passwortHashed, passwortHasher.hashPasswort(passwort));
    }

    @Test
    void verschiedeneHashesBeiVerschiedenenStrings(){
        String passwort1 = "Passwort";
        String passwort2 = "passwort";
        assertNotEquals(passwortHasher.hashPasswort(passwort1), passwortHasher.hashPasswort(passwort2));
    }
}
