package kalendario.application.crud.sicherheit;

import kalendario.application.session.KeinZugriffException;
import kalendario.application.session.Session;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.Event;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.SerienRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ZugriffVerifiziererTest {

    EventRepository eventRepository = mock();
    SerienRepository serienRepository = mock();
    Session session = mock();
    Event event = mock();
    EventId eventId = mock();
    Serie serie = mock();
    SerienId serienId = mock();
    BenutzerId benutzer = mock();
    ZugriffVerfizierer zugriffVerfizierer;

    @BeforeEach
    void init(){
        zugriffVerfizierer = new ZugriffVerfizierer(session, eventRepository, serienRepository);
    }

    @Test
    void verifiziereZugriffFuerSerieSollBasierendAufSichtbarkeitFuerAktuellenBenutzterVerifizierenMitSerie() throws KeinZugriffException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer));
        when(serie.getDefaultEvent()).thenReturn(eventId);
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        when(event.istSichtbarFuer(benutzer)).thenReturn(true);
        zugriffVerfizierer.verifiziereZugriffFuerSerie(serie);
        when(event.istSichtbarFuer(benutzer)).thenReturn(false);
        assertThrows(KeinZugriffException.class, () -> zugriffVerfizierer.verifiziereZugriffFuerSerie(serie));
    }

    @Test
    void verifiziereZugriffFuerSerieSollKeinAccessGebenWennKeinUserAngemeldetIstMitSerie(){
        when(session.getCurrentBenutzer()).thenReturn(Optional.empty());
        when(serie.getDefaultEvent()).thenReturn(eventId);
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        assertThrows(KeinZugriffException.class, () -> zugriffVerfizierer.verifiziereZugriffFuerSerie(serie));
    }

    @Test
    void verifiziereZugriffFuerSerieSollBasierendAufSichtbarkeitFuerAktuellenBenutzterVerifizierenMitSerienId() throws KeinZugriffException {
        when(serienRepository.getSerie(serienId)).thenReturn(serie);
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer));
        when(serie.getDefaultEvent()).thenReturn(eventId);
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        when(event.istSichtbarFuer(benutzer)).thenReturn(true);
        zugriffVerfizierer.verifiziereZugriffFuerSerie(serie);
        when(event.istSichtbarFuer(benutzer)).thenReturn(false);
        assertThrows(KeinZugriffException.class, () -> zugriffVerfizierer.verifiziereZugriffFuerSerie(serie));
    }

    @Test
    void verifiziereZugriffFuerSerieSollKeinAccessGebenWennKeinUserAngemeldetIstMitSerienId(){
        when(serienRepository.getSerie(serienId)).thenReturn(serie);
        when(session.getCurrentBenutzer()).thenReturn(Optional.empty());
        when(serie.getDefaultEvent()).thenReturn(eventId);
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        assertThrows(KeinZugriffException.class, () -> zugriffVerfizierer.verifiziereZugriffFuerSerie(serie));
    }

    @Test
    void verifiziereZugriffFuerEventSollBasierendAufEventSichtbarkeitVerfizierenMitEvent() throws KeinZugriffException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer));
        when(event.istSichtbarFuer(benutzer)).thenReturn(true);
        zugriffVerfizierer.verifiziereZugriffFuerEvent(event);
        when(event.istSichtbarFuer(benutzer)).thenReturn(false);
        assertThrows(KeinZugriffException.class, () -> zugriffVerfizierer.verifiziereZugriffFuerEvent(event));
    }

    @Test
    void verifiziereZugriffFuerEventSollKeinAccessGebenWennKeinBenutzerAngemeldetIstMitEvent(){
        when(session.getCurrentBenutzer()).thenReturn(Optional.empty());
        assertThrows(KeinZugriffException.class, () -> zugriffVerfizierer.verifiziereZugriffFuerEvent(event));
    }

    @Test
    void verifiziereZugriffFuerEventSollBasierendAufEventSichtbarkeitVerfizierenMitEventId() throws KeinZugriffException {
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer));
        when(event.istSichtbarFuer(benutzer)).thenReturn(true);
        zugriffVerfizierer.verifiziereZugriffFuerEvent(event);
        when(event.istSichtbarFuer(benutzer)).thenReturn(false);
        assertThrows(KeinZugriffException.class, () -> zugriffVerfizierer.verifiziereZugriffFuerEvent(event));
    }

    @Test
    void verifiziereZugriffFuerEventSollKeinAccessGebenWennKeinBenutzerAngemeldetIstMitEventId(){
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        when(session.getCurrentBenutzer()).thenReturn(Optional.empty());
        assertThrows(KeinZugriffException.class, () -> zugriffVerfizierer.verifiziereZugriffFuerEvent(event));
    }

    @Test
    void verifiziereZugriffFuerSerieSollKeinAccessGebenWennSerieNichtVerfuegbar(){
        when(serienRepository.getSerie(serienId)).thenReturn(null);
        assertThrows(KeinZugriffException.class, () -> zugriffVerfizierer.verifiziereZugriffFuerSerie(serienId));
    }

    @Test
    void verifiziereZugriffFuerEventSollKeinAccessGebenWennEventNichtVerfuegbar(){
        when(eventRepository.getEvent(eventId)).thenReturn(null);
        assertThrows(KeinZugriffException.class, () -> zugriffVerfizierer.verifiziereZugriffFuerEvent(eventId));
    }
}