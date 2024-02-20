package domain.entities.serie;

import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.serie.SerienId;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class SerienIdTest {

    @Test
    void zweiEventIdsSollenGleichSeinWennSieAusDemselbenIntegerStammen(){
        assertEquals(new SerienId(1), new SerienId(1));
    }

    @Test
    void zweiEventIdsSollenNichtGleichSeinWennSieAusVerschiedenenIntegernStammen(){
        assertNotEquals(new SerienId(1), new SerienId(2));
    }
}
