package domain.entities.event;

import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.PublicSichtbarkeit;
import kalendario.domain.entities.event.Sichtbarkeit;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class PublicSichtbarkeitTest {

    BenutzerId benutzer = mock();

    @Test
    void istSichtbarSollImmerTrueZurueckgeben(){
        Sichtbarkeit sichtbarkeit = new PublicSichtbarkeit();
        assertTrue(sichtbarkeit.istSichtbarFuer(benutzer));
    }
}
