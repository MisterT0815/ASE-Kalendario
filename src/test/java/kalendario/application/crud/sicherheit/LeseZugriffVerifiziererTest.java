package kalendario.application.crud.sicherheit;

import kalendario.application.session.KeinZugriffException;
import kalendario.application.session.Session;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.Event;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.HerkunftRepository;
import kalendario.domain.repositories.SerienRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LeseZugriffVerifiziererTest {

    EventRepository eventRepository = mock();
    SerienRepository serienRepository = mock();
    HerkunftRepository herkunftRepository = mock();
    Session session = mock();
    Event event = mock();
    EventId eventId = mock();
    Serie serie = mock();
    SerienId serienId = mock();
    BenutzerId benutzer = mock();
    BenutzerId besitzer = mock();
    Herkunft herkunft = mock();
    HerkunftId herkunftId = mock();
    LeseZugriffVerfizierer leseZugriffVerfizierer;

    @BeforeEach
    void init(){
        leseZugriffVerfizierer = new LeseZugriffVerfizierer(session, eventRepository, serienRepository, herkunftRepository);
        when(event.getHerkunftId()).thenReturn(herkunftId);
        when(herkunftRepository.getHerkunftWithId(herkunftId)).thenReturn(herkunft);
        when(herkunft.getBesitzerId()).thenReturn(besitzer);
    }

    @Test
    void verifiziereZugriffFuerSerieSollBasierendAufSichtbarkeitFuerAktuellenBenutzterVerifizierenMitSerie() throws KeinZugriffException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer));
        when(serie.getDefaultEvent()).thenReturn(eventId);
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        when(event.istSichtbarFuer(benutzer)).thenReturn(true);
        assertDoesNotThrow(() -> leseZugriffVerfizierer.verifiziereZugriffFuerSerie(serie));
        when(event.istSichtbarFuer(benutzer)).thenReturn(false);
        assertThrows(KeinZugriffException.class, () -> leseZugriffVerfizierer.verifiziereZugriffFuerSerie(serie));
    }

    @Test
    void verifiziereZugriffFuerSerieSollKeinAccessGebenWennKeinUserAngemeldetIstMitSerie(){
        when(session.getCurrentBenutzer()).thenReturn(Optional.empty());
        when(serie.getDefaultEvent()).thenReturn(eventId);
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        assertThrows(KeinZugriffException.class, () -> leseZugriffVerfizierer.verifiziereZugriffFuerSerie(serie));
    }

    @Test
    void verifiziereZugriffFuerSerieSollBasierendAufSichtbarkeitFuerAktuellenBenutzterVerifizierenMitSerienId() throws KeinZugriffException {
        when(serienRepository.getSerie(serienId)).thenReturn(serie);
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer));
        when(serie.getDefaultEvent()).thenReturn(eventId);
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        when(event.istSichtbarFuer(benutzer)).thenReturn(true);
        assertDoesNotThrow(() -> leseZugriffVerfizierer.verifiziereZugriffFuerSerie(serienId));
        when(event.istSichtbarFuer(benutzer)).thenReturn(false);
        assertThrows(KeinZugriffException.class, () -> leseZugriffVerfizierer.verifiziereZugriffFuerSerie(serienId));
    }

    @Test
    void verifiziereZugriffFuerSerieSollKeinAccessGebenWennKeinUserAngemeldetIstMitSerienId(){
        when(serienRepository.getSerie(serienId)).thenReturn(serie);
        when(session.getCurrentBenutzer()).thenReturn(Optional.empty());
        when(serie.getDefaultEvent()).thenReturn(eventId);
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        assertThrows(KeinZugriffException.class, () -> leseZugriffVerfizierer.verifiziereZugriffFuerSerie(serienId));
    }

    @Test
    void verifiziereZugriffFuerEventSollBasierendAufEventSichtbarkeitVerfizierenMitEvent() {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer));
        when(event.istSichtbarFuer(benutzer)).thenReturn(true);
        assertDoesNotThrow(() -> leseZugriffVerfizierer.verifiziereZugriffFuerEvent(event));
        when(event.istSichtbarFuer(benutzer)).thenReturn(false);
        assertThrows(KeinZugriffException.class, () -> leseZugriffVerfizierer.verifiziereZugriffFuerEvent(event));
    }

    @Test
    void verifiziereZugriffFuerEventSollKeinAccessGebenWennKeinBenutzerAngemeldetIstMitEvent(){
        when(session.getCurrentBenutzer()).thenReturn(Optional.empty());
        assertThrows(KeinZugriffException.class, () -> leseZugriffVerfizierer.verifiziereZugriffFuerEvent(event));
    }

    @Test
    void verifiziereZugriffFuerEventSollBasierendAufEventSichtbarkeitVerfizierenMitEventId() throws KeinZugriffException {
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer));
        when(event.istSichtbarFuer(benutzer)).thenReturn(true);
        assertDoesNotThrow(() -> leseZugriffVerfizierer.verifiziereZugriffFuerEvent(eventId));
        when(event.istSichtbarFuer(benutzer)).thenReturn(false);
        assertThrows(KeinZugriffException.class, () -> leseZugriffVerfizierer.verifiziereZugriffFuerEvent(eventId));
    }

    @Test
    void verifiziereZugriffFuerEventSollKeinAccessGebenWennKeinBenutzerAngemeldetIstMitEventId(){
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        when(session.getCurrentBenutzer()).thenReturn(Optional.empty());
        assertThrows(KeinZugriffException.class, () -> leseZugriffVerfizierer.verifiziereZugriffFuerEvent(eventId));
    }

    @Test
    void verifiziereZugriffFuerSerieSollKeinAccessGebenWennSerieNichtVerfuegbar(){
        when(serienRepository.getSerie(serienId)).thenReturn(null);
        assertThrows(KeinZugriffException.class, () -> leseZugriffVerfizierer.verifiziereZugriffFuerSerie(serienId));
    }

    @Test
    void verifiziereZugriffFuerEventSollKeinAccessGebenWennEventNichtVerfuegbar(){
        when(eventRepository.getEvent(eventId)).thenReturn(null);
        assertThrows(KeinZugriffException.class, () -> leseZugriffVerfizierer.verifiziereZugriffFuerEvent(eventId));
    }

    @Test
    void verifiziereZugriffFuerEventSollAccessGebenWennBenutzerBesitzerIstMitEvent(){
        when(herkunft.getBesitzerId()).thenReturn(benutzer);
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer));
        assertDoesNotThrow(() -> leseZugriffVerfizierer.verifiziereZugriffFuerEvent(event));
    }

    @Test
    void verifiziereZugriffFuerEventSollAccessGebenWennBenutzerBesitzerIstMitEventId(){
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        when(herkunft.getBesitzerId()).thenReturn(benutzer);
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer));
        assertDoesNotThrow(() -> leseZugriffVerfizierer.verifiziereZugriffFuerEvent(eventId));
    }

    @Test
    void verifiziereZugriffFuerSerieSollAccessGebenWennBenutzerBesitzerDesDefualtEventsIst(){
        when(serie.getDefaultEvent()).thenReturn(eventId);
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        when(herkunft.getBesitzerId()).thenReturn(benutzer);
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer));
        assertDoesNotThrow(() -> leseZugriffVerfizierer.verifiziereZugriffFuerSerie(serie));
    }

    @Test
    void verifiziereZugriffFuerSerieSollAccessGebenWennBenutzerBesitzerIstMitSerienId(){
        when(serienRepository.getSerie(serienId)).thenReturn(serie);
        when(serie.getDefaultEvent()).thenReturn(eventId);
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        when(herkunft.getBesitzerId()).thenReturn(benutzer);
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer));
        assertDoesNotThrow(() -> leseZugriffVerfizierer.verifiziereZugriffFuerSerie(serienId));
    }

    @Test
    void verifiziereZugriffFuerEventSollKeinenAccessGebenWennHerkunftDesEventsNichtExistiert(){
        when(herkunftRepository.getHerkunftWithId(herkunftId)).thenReturn(null);
        assertThrows(KeinZugriffException.class, () -> leseZugriffVerfizierer.verifiziereZugriffFuerEvent(event));
    }
}