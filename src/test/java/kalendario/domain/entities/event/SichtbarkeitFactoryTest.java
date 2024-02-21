package kalendario.domain.entities.event;

import kalendario.domain.entities.benutzer.BenutzerId;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class SichtbarkeitFactoryTest {

    @Test
    void fuerAlleSollPublicSichtbarkeitZurueckgeben(){
        assertInstanceOf(PublicSichtbarkeit.class, SichtbarkeitFactory.fuerAlle());
    }

    @Test
    void nurFuerSollPrivateSichtbarkeitMitSetZurueckgeben(){
        Set<BenutzerId> benutzerSet = mock();
        Sichtbarkeit sichtbarkeit = SichtbarkeitFactory.nurFuer(benutzerSet);
        assertInstanceOf(PrivateSichtbarkeit.class, sichtbarkeit);
        PrivateSichtbarkeit privateSichtbarkeit = (PrivateSichtbarkeit) sichtbarkeit;
        assertEquals(benutzerSet, privateSichtbarkeit.sichtbarFuer);
    }

}
