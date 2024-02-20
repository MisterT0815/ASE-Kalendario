package domain.entities.benutzer;

import kalendario.domain.entities.benutzer.BenutzerId;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class BenutzerIdTest {

    @Test
    void zweiEventIdsSollenGleichSeinWennSieAusDemselbenIntegerStammen(){
        assertEquals(new BenutzerId(1), new BenutzerId(1));
    }

    @Test
    void zweiEventIdsSollenNichtGleichSeinWennSieAusVerschiedenenIntegernStammen(){
        assertNotEquals(new BenutzerId(1), new BenutzerId(2));
    }
}
