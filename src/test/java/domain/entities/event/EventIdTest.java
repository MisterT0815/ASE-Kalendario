package domain.entities.event;

import kalendario.domain.entities.event.EventId;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class EventIdTest {

    @Test
    void zweiEventIdsSollenGleichSeinWennSieAusDemselbenIntegerStammen(){
        assertEquals(new EventId(1), new EventId(1));
    }

    @Test
    void zweiEventIdsSollenNichtGleichSeinWennSieAusVerschiedenenIntegernStammen(){
        assertNotEquals(new EventId(1), new EventId(2));
    }
}
