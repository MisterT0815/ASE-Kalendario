package kalendario.domain.entities.event;

import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.PrivateSichtbarkeit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class PrivateSichtbarkeitTest {

    BenutzerId benutzer1 = mock();
    BenutzerId benutzer2 = mock();
    BenutzerId benutzer3 = mock();
    Set<BenutzerId> benutzerSet;
    PrivateSichtbarkeit privateSichtbarkeit;

    @BeforeEach
    void initialize(){
        benutzerSet = new HashSet<>();
        benutzerSet.add(benutzer1);
        benutzerSet.add(benutzer2);
        privateSichtbarkeit = new PrivateSichtbarkeit(benutzerSet);
    }


    @Test
    void istSichtbarFuerSollTrueZurueckgebenFuerAlleErlaubtenBenutzer(){
        assertTrue(privateSichtbarkeit.istSichtbarFuer(benutzer1));
        assertTrue(privateSichtbarkeit.istSichtbarFuer(benutzer2));
    }

    @Test
    void istSichtbarFuerSollFalseZurueckgebenFuerAlleUnerlaubtenBenutzer(){
        assertFalse(privateSichtbarkeit.istSichtbarFuer(benutzer3));
    }

    @Test
    void machSichtbarFuerSollBenutzerAlsErlaubtenBenutzerHinzufuegen(){
        assertTrue(privateSichtbarkeit.machSichtbarFuer(benutzer3));
        assertTrue(privateSichtbarkeit.istSichtbarFuer(benutzer3));
    }

    @Test
    void machSichtbarFuerSollFalseZurueckgebenWennBenutzerBereitsErlaubt(){
        assertFalse(privateSichtbarkeit.machSichtbarFuer(benutzer1));
    }

    @Test
    void entferneSichtbarkeitFuerSollBenutzerAusErlaubtenEntfernen(){
        assertTrue(privateSichtbarkeit.entferneSichtbarkeitFuer(benutzer2));
        assertFalse(privateSichtbarkeit.istSichtbarFuer(benutzer2));
    }

    @Test
    void entferneSichtbarkeitFuerSollFalseZurueckgebenWennBenutzerbereitsUnerlaubt(){
        assertFalse(privateSichtbarkeit.entferneSichtbarkeitFuer(benutzer3));
    }

}
