package kalendario.application.crud.event;

import kalendario.application.crud.sicherheit.ExistiertNichtException;
import kalendario.application.crud.sicherheit.LeseZugriffVerfizierer;
import kalendario.application.session.KeinZugriffException;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class EventReadTest {

    EventRepository eventRepository = mock();
    EventId eventId = mock();
    Event event = mock();
    Aufgabe aufgabe = mock();
    GeplanteAufgabe geplanteAufgabe = mock();
    Termin termin = mock();
    SerienId serienId = mock();
    EventRead eventRead;
    LeseZugriffVerfizierer leseZugriffVerfizierer = mock();

    @BeforeEach
    void init(){
        eventRead = new EventRead(eventRepository, leseZugriffVerfizierer);
    }

    @Test
    void getEventSollEventMitIdVonRepositoryHolen() throws KeinZugriffException {
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        assertEquals(event, eventRead.getEvent(eventId).get());
    }

    @Test
    void getAufgabeSollAufgabeVonEventRepositoryZurueckgeben() throws KeinZugriffException {
        when(eventRepository.getEvent(eventId)).thenReturn(aufgabe);
        assertEquals(aufgabe, eventRead.getAufgabe(eventId).get());
    }

    @Test
    void getGeplanteAufgabeSollGeplanteAufgabeVonEventRepositoryZurueckgeben() throws KeinZugriffException {
        when(eventRepository.getEvent(eventId)).thenReturn(geplanteAufgabe);
        assertEquals(geplanteAufgabe, eventRead.getGeplanteAufgabe(eventId).get());
    }

    @Test
    void getTerminSollTerminVonEventRepositoryZurueckgeben() throws KeinZugriffException {
        when(eventRepository.getEvent(eventId)).thenReturn(termin);
        assertEquals(termin, eventRead.getTermin(eventId).get());
    }

    @Test
    void getAufgabeSollLeeresOptionalGebenWennRepositoryMitKeinerAufgabeAntwortet() throws KeinZugriffException {
        when(eventRepository.getEvent(eventId)).thenReturn(null);
        assertTrue(eventRead.getAufgabe(eventId).isEmpty());
        when(eventRepository.getEvent(eventId)).thenReturn(geplanteAufgabe);
        assertTrue(eventRead.getAufgabe(eventId).isEmpty());
    }

    @Test
    void getGeplanteAufgabeSollLeeresOptionalGebenWennRepositoryMitKeinerGeplanteAufgabeAntwortet() throws KeinZugriffException {
        when(eventRepository.getEvent(eventId)).thenReturn(null);
        assertTrue(eventRead.getGeplanteAufgabe(eventId).isEmpty());
        when(eventRepository.getEvent(eventId)).thenReturn(aufgabe);
        assertTrue(eventRead.getGeplanteAufgabe(eventId).isEmpty());
    }

    @Test
    void getTerminSollLeeresOptionalGebenWennRepositoryMitKeinemTerminAntwortet() throws KeinZugriffException {
        when(eventRepository.getEvent(eventId)).thenReturn(null);
        assertTrue(eventRead.getTermin(eventId).isEmpty());
        when(eventRepository.getEvent(eventId)).thenReturn(aufgabe);
        assertTrue(eventRead.getTermin(eventId).isEmpty());
    }

    @Test
    void getEventSollNoAccessExceptionWerfenWennEventNichtSichtbarFuerAktuellenBenutzerIst() throws KeinZugriffException {
        doThrow(KeinZugriffException.class).when(leseZugriffVerfizierer).verifiziereZugriffFuerEvent(event);
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        assertThrows(KeinZugriffException.class, () -> eventRead.getEvent(eventId));
    }

    @Test
    void getAufgabeSollNoAccessExceptionWerfenWennEventNichtSichtbarFuerAktuellenBenutzerIst() throws KeinZugriffException {
        doThrow(KeinZugriffException.class).when(leseZugriffVerfizierer).verifiziereZugriffFuerEvent(aufgabe);
        when(eventRepository.getEvent(eventId)).thenReturn(aufgabe);
        assertThrows(KeinZugriffException.class, () -> eventRead.getEvent(eventId));
    }

    @Test
    void getGeplanteAufgabeSollNoAccessExceptionWerfenWennEventNichtSichtbarFuerAktuellenBenutzerIst() throws KeinZugriffException {
        doThrow(KeinZugriffException.class).when(leseZugriffVerfizierer).verifiziereZugriffFuerEvent(geplanteAufgabe);
        when(eventRepository.getEvent(eventId)).thenReturn(geplanteAufgabe);
        assertThrows(KeinZugriffException.class, () -> eventRead.getEvent(eventId));
    }

    @Test
    void getTerminSollNoAccessExceptionWerfenWennEventNichtSichtbarFuerAktuellenBenutzerIst() throws KeinZugriffException {
        doThrow(KeinZugriffException.class).when(leseZugriffVerfizierer).verifiziereZugriffFuerEvent(termin);
        when(eventRepository.getEvent(eventId)).thenReturn(termin);
        assertThrows(KeinZugriffException.class, () -> eventRead.getEvent(eventId));
    }

    @Test
    void getEventsOfSerieSollEventsVonRepositoryZurueckgeben() throws KeinZugriffException, ExistiertNichtException {
        Event event1 = mock();
        Event event2 = mock();
        List<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        when(eventRepository.getEventsOfSerie(serienId)).thenReturn(events);
        List<Event> resultingEvents = eventRead.getEventsOfSerie(serienId);
        assertTrue(resultingEvents.contains(event1));
        assertTrue(resultingEvents.contains(event2));
    }

    @Test
    void getEventsOfSerieSollExceptionWerfenWennBenutzerKeinenAccessAufSerieHat() throws KeinZugriffException, ExistiertNichtException {
        doThrow(KeinZugriffException.class).when(leseZugriffVerfizierer).verifiziereZugriffFuerSerie(serienId);
        assertThrows(KeinZugriffException.class, () -> eventRead.getEventsOfSerie(serienId));
    }

    @Test
    void getEventsOfSerieSollNichtSichtbareEventsAussortieren() throws KeinZugriffException, ExistiertNichtException {
        Event event1 = mock();
        Event event2 = mock();
        List<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        when(eventRepository.getEventsOfSerie(serienId)).thenReturn(events);
        doThrow(KeinZugriffException.class).when(leseZugriffVerfizierer).verifiziereZugriffFuerEvent(event2);
        List<Event> returnedEvents = eventRead.getEventsOfSerie(serienId);
        assertTrue(returnedEvents.contains(event1));
        assertFalse(returnedEvents.contains(event2));
    }

}
