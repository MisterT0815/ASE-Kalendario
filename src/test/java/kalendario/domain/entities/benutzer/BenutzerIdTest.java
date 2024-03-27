package kalendario.domain.entities.benutzer;

import kalendario.domain.entities.benutzer.BenutzerId;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class BenutzerIdTest {

    @Test
    void zweiEventIdsSollenGleichSeinWennSieAusDemselbenIntegerStammen(){
        UUID id = UUID.randomUUID();
        String idString = id.toString();
        assertEquals(new BenutzerId(UUID.fromString(idString)), new BenutzerId(UUID.fromString(idString)));
    }

    @Test
    void zweiEventIdsSollenNichtGleichSeinWennSieAusVerschiedenenIntegernStammen(){
        assertNotEquals(new BenutzerId(UUID.randomUUID()), new BenutzerId(UUID.randomUUID()));
    }
}
