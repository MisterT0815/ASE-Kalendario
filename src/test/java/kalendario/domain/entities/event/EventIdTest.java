package kalendario.domain.entities.event;

import kalendario.domain.entities.event.EventId;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class EventIdTest {

    @Test
    void zweiEventIdsSollenGleichSeinWennSieAusDemselbenIntegerStammen(){
        UUID id = UUID.randomUUID();
        assertEquals(new EventId(id), new EventId(id));
    }

    @Test
    void zweiEventIdsSollenNichtGleichSeinWennSieAusVerschiedenenIntegernStammen(){
        assertNotEquals(new EventId(UUID.randomUUID()), new EventId(UUID.randomUUID()));
    }
}
