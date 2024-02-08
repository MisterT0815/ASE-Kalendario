package domain.entities.event;

import kalendario.domain.entities.event.Event;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.event.Sichtbarkeit;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.serie.SerienId;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class EventTest {

    private class DefaultEventImplementation extends Event{
        public DefaultEventImplementation(EventId id, String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung) {
            super(id, titel, herkunft, sichtbarkeit, beschreibung);
        }

        public DefaultEventImplementation(EventId id, String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, SerienId serienId) {
            super(id, titel, herkunft, sichtbarkeit, beschreibung, serienId);
        }
    }

    EventId id = mock();
    String titel = "Titel";
    Herkunft herkunft = mock();
    Sichtbarkeit sichtbarkeit = mock();
    String beschreibung = "Beschreibung";
    SerienId serienId = mock();


    @Test
    void getSerienIdSollOptionalMitSerienIdZurueckgebenWennDieseExistiert(){
        DefaultEventImplementation event = new DefaultEventImplementation(id, titel, herkunft, sichtbarkeit, beschreibung, serienId);
        Optional<SerienId> actual = event.getSerienId();
        assertTrue(actual.isPresent());
        assertEquals(serienId, actual.get());
    }

    @Test
    void getSerienIdSollLeeresOptionalZurueckgebenWennKeineSerieExistiert(){
        DefaultEventImplementation event = new DefaultEventImplementation(id, titel, herkunft, sichtbarkeit, beschreibung);
        Optional<SerienId> actual = event.getSerienId();
        assertTrue(actual.isEmpty());
    }
}
