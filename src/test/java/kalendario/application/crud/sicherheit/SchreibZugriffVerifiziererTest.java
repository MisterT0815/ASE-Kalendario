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
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class SchreibZugriffVerifiziererTest {

    Session session = mock();
    EventRepository eventRepository = mock();
    SerienRepository serienRepository = mock();
    HerkunftRepository herkunftRepository = mock();
    Serie serie = mock();
    SerienId serienId = mock();
    Event event = mock();
    EventId eventId = mock();
    BenutzerId benutzer = mock();
    BenutzerId besitzer = mock();
    Herkunft herkunft = mock();
    HerkunftId herkunftId = mock();
    SchreibZugriffVerifizierer schreibZugriffVerifizierer;

    @BeforeEach
    void init(){
        schreibZugriffVerifizierer = new SchreibZugriffVerifizierer(session, eventRepository, serienRepository, herkunftRepository);
        when(serienRepository.getSerie(serienId)).thenReturn(serie);
        when(serie.getDefaultEvent()).thenReturn(eventId);
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        when(event.getHerkunftId()).thenReturn(herkunftId);
        when(herkunftRepository.getHerkunftWithId(herkunftId)).thenReturn(herkunft);
        when(herkunft.getBesitzerId()).thenReturn(besitzer);
    }

    @Test
    void verifiziereZugriffFuerSerieSollZugriffNurFuerBesitzerGeben(){
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(besitzer));
        assertDoesNotThrow(() -> schreibZugriffVerifizierer.verifiziereZugriffFuerSerie(serie));
        assertDoesNotThrow(() -> schreibZugriffVerifizierer.verifiziereZugriffFuerSerie(serienId));
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer));
        assertThrows(KeinZugriffException.class, () -> schreibZugriffVerifizierer.verifiziereZugriffFuerSerie(serie));
        assertThrows(KeinZugriffException.class, () -> schreibZugriffVerifizierer.verifiziereZugriffFuerSerie(serienId));
        when(session.getCurrentBenutzer()).thenReturn(Optional.empty());
        assertThrows(KeinZugriffException.class, () -> schreibZugriffVerifizierer.verifiziereZugriffFuerSerie(serie));
        assertThrows(KeinZugriffException.class, () -> schreibZugriffVerifizierer.verifiziereZugriffFuerSerie(serienId));
    }

    @Test
    void verifiziereZugriffFuerEventSollZugriffNurFuerBesitzerGeben(){
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(besitzer));
        assertDoesNotThrow(() -> schreibZugriffVerifizierer.verifiziereZugriffFuerEvent(event));
        assertDoesNotThrow(() -> schreibZugriffVerifizierer.verifiziereZugriffFuerEvent(eventId));
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer));
        assertThrows(KeinZugriffException.class, () -> schreibZugriffVerifizierer.verifiziereZugriffFuerEvent(event));
        assertThrows(KeinZugriffException.class, () -> schreibZugriffVerifizierer.verifiziereZugriffFuerEvent(eventId));
        when(session.getCurrentBenutzer()).thenReturn(Optional.empty());
        assertThrows(KeinZugriffException.class, () -> schreibZugriffVerifizierer.verifiziereZugriffFuerEvent(event));
        assertThrows(KeinZugriffException.class, () -> schreibZugriffVerifizierer.verifiziereZugriffFuerEvent(eventId));
    }

    @Test
    void verifiziereZugriffFuerSerieSollZugriffVerweigernWennEsSerieNichtGibt() {
        when(serienRepository.getSerie(serienId)).thenReturn(null);
        assertThrows(KeinZugriffException.class, () -> schreibZugriffVerifizierer.verifiziereZugriffFuerSerie(serienId));
    }

    @Test
    void verifiziereZugriffFuerSerieSollZugriffVerweigernWennEsDefaultEventDerSerieNichtGibt() {
        when(eventRepository.getEvent(eventId)).thenReturn(null);
        assertThrows(KeinZugriffException.class, () -> schreibZugriffVerifizierer.verifiziereZugriffFuerSerie(serie));
    }

    @Test
    void verifiziereZugriffFuerEventSollZugriffVerweigernWennEsEventNichtGibt() {
        when(eventRepository.getEvent(eventId)).thenReturn(null);
        assertThrows(KeinZugriffException.class, () -> schreibZugriffVerifizierer.verifiziereZugriffFuerEvent(eventId));
    }

    @Test
    void verifiziereZugriffFuerEventSollZugriffVerweigernWennEsHerkunftDesEventsNichtGibt() {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(besitzer));
        when(herkunftRepository.getHerkunftWithId(herkunftId)).thenReturn(null);
        assertThrows(KeinZugriffException.class, () -> schreibZugriffVerifizierer.verifiziereZugriffFuerEvent(eventId));
    }

}
