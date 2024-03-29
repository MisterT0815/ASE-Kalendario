package kalendario.domain.entities.serie;

import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.serie.SerienId;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class SerienIdTest {

    @Test
    void zweiEventIdsSollenGleichSeinWennSieAusDemselbenIntegerStammen(){
        UUID id = UUID.randomUUID();
        assertEquals(new SerienId(id), new SerienId(id));
    }

    @Test
    void zweiEventIdsSollenNichtGleichSeinWennSieAusVerschiedenenIntegernStammen(){
        assertNotEquals(new SerienId(UUID.randomUUID()), new SerienId(UUID.randomUUID()));
    }
}
