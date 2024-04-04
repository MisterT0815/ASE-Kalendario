package kalendario.domain.entities.event;

import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.entities.serie.SerienId;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class EventTest {

    private class DefaultEventImplementation extends Event{
        public DefaultEventImplementation(EventId id, String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung) {
            super(id, titel, herkunft, sichtbarkeit, beschreibung);
        }

        public DefaultEventImplementation(EventId id, String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, SerienId serienId) {
            super(id, titel, herkunft, sichtbarkeit, beschreibung, serienId);
        }

        @Override
        public void pushByDuration(Duration duration) {

        }
    }

    EventId id = mock();
    String titel = "Titel";
    HerkunftId herkunft = mock();
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

    @Test
    void istSichtbarFuerSollAnSichtbarkeitDelegieren(){
        DefaultEventImplementation event = new DefaultEventImplementation(id, titel, herkunft, sichtbarkeit, beschreibung);
        BenutzerId benutzerId = mock();
        event.istSichtbarFuer(benutzerId);
        verify(sichtbarkeit).istSichtbarFuer(benutzerId);
    }


}
