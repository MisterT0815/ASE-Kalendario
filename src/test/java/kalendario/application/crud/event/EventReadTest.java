package kalendario.application.crud.event;

import kalendario.application.crud.serie.SerieRead;
import kalendario.application.session.NoAccessException;
import kalendario.application.session.Session;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class EventReadTest {

    EventRepository eventRepository = mock();
    Session session = mock();
    SerieRead serieRead = mock();
    EventId eventId = mock();
    Event event = mock();
    Aufgabe aufgabe = mock();
    GeplanteAufgabe geplanteAufgabe = mock();
    Termin termin = mock();
    SerienId serienId = mock();
    List<Event> events = mock();
    EventRead eventRead;
    BenutzerId benutzerId = mock();

    @BeforeEach
    void init(){
        eventRead = new EventRead(eventRepository, session, serieRead);
    }

    @Test
    void getEventSollEventMitIdVonRepositoryHolen() throws NoAccessException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        when(event.istSichtbarFuer(benutzerId)).thenReturn(true);
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        assertEquals(event, eventRead.getEvent(eventId).get());
    }

    @Test
    void getAufgabeSollAufgabeVonEventRepositoryZurueckgeben() throws NoAccessException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        when(aufgabe.istSichtbarFuer(benutzerId)).thenReturn(true);
        when(eventRepository.getEvent(eventId)).thenReturn(aufgabe);
        assertEquals(aufgabe, eventRead.getAufgabe(eventId).get());
    }

    @Test
    void getGeplanteAufgabeSollGeplanteAufgabeVonEventRepositoryZurueckgeben() throws NoAccessException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        when(geplanteAufgabe.istSichtbarFuer(benutzerId)).thenReturn(true);
        when(eventRepository.getEvent(eventId)).thenReturn(geplanteAufgabe);
        assertEquals(geplanteAufgabe, eventRead.getGeplanteAufgabe(eventId).get());
    }

    @Test
    void getTerminSollTerminVonEventRepositoryZurueckgeben() throws NoAccessException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        when(termin.istSichtbarFuer(benutzerId)).thenReturn(true);
        when(eventRepository.getEvent(eventId)).thenReturn(termin);
        assertEquals(termin, eventRead.getTermin(eventId).get());
    }

    @Test
    void getAufgabeSollLeeresOptionalGebenWennRepositoryMitKeinerAufgabeAntwortet() throws NoAccessException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        when(geplanteAufgabe.istSichtbarFuer(benutzerId)).thenReturn(true);
        when(eventRepository.getEvent(eventId)).thenReturn(null);
        assertTrue(eventRead.getAufgabe(eventId).isEmpty());
        when(eventRepository.getEvent(eventId)).thenReturn(geplanteAufgabe);
        assertTrue(eventRead.getAufgabe(eventId).isEmpty());
    }

    @Test
    void getGeplanteAufgabeSollLeeresOptionalGebenWennRepositoryMitKeinerGeplanteAufgabeAntwortet() throws NoAccessException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        when(aufgabe.istSichtbarFuer(benutzerId)).thenReturn(true);
        when(eventRepository.getEvent(eventId)).thenReturn(null);
        assertTrue(eventRead.getGeplanteAufgabe(eventId).isEmpty());
        when(eventRepository.getEvent(eventId)).thenReturn(aufgabe);
        assertTrue(eventRead.getGeplanteAufgabe(eventId).isEmpty());
    }

    @Test
    void getTerminSollLeeresOptionalGebenWennRepositoryMitKeinemTerminAntwortet() throws NoAccessException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        when(aufgabe.istSichtbarFuer(benutzerId)).thenReturn(true);
        when(eventRepository.getEvent(eventId)).thenReturn(null);
        assertTrue(eventRead.getTermin(eventId).isEmpty());
        when(eventRepository.getEvent(eventId)).thenReturn(aufgabe);
        assertTrue(eventRead.getTermin(eventId).isEmpty());
    }

    @Test
    void getEventSollNoAccessExceptionWerfenWennEventNichtSichtbarFuerAktuellenBenutzerIst(){
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        when(event.istSichtbarFuer(benutzerId)).thenReturn(false);
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        assertThrows(NoAccessException.class, () -> eventRead.getEvent(eventId));
    }

    @Test
    void getAufgabeSollNoAccessExceptionWerfenWennEventNichtSichtbarFuerAktuellenBenutzerIst(){
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        when(aufgabe.istSichtbarFuer(benutzerId)).thenReturn(false);
        when(eventRepository.getEvent(eventId)).thenReturn(aufgabe);
        assertThrows(NoAccessException.class, () -> eventRead.getEvent(eventId));
    }

    @Test
    void getGeplanteAufgabeSollNoAccessExceptionWerfenWennEventNichtSichtbarFuerAktuellenBenutzerIst(){
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        when(geplanteAufgabe.istSichtbarFuer(benutzerId)).thenReturn(false);
        when(eventRepository.getEvent(eventId)).thenReturn(geplanteAufgabe);
        assertThrows(NoAccessException.class, () -> eventRead.getEvent(eventId));
    }

    @Test
    void getTerminSollNoAccessExceptionWerfenWennEventNichtSichtbarFuerAktuellenBenutzerIst(){
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        when(termin.istSichtbarFuer(benutzerId)).thenReturn(false);
        when(eventRepository.getEvent(eventId)).thenReturn(termin);
        assertThrows(NoAccessException.class, () -> eventRead.getEvent(eventId));
    }

    @Test
    void getEventsOfSerieSollEventsVonRepositoryZurueckgeben() throws NoAccessException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        when(event.istSichtbarFuer(benutzerId)).thenReturn(true);
        when(eventRepository.getEventsOfSerie(serienId)).thenReturn(events);
        assertEquals(events, eventRead.getEventsOfSerie(serienId));
    }

    @Test
    void getEventsOfSerieSollExceptionWerfenWennBenutzerKeinenAccessAufSerieHat() throws NoAccessException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        when(serieRead.getSerie(serienId)).thenThrow(NoAccessException.class);
        assertThrows(NoAccessException.class, () -> eventRead.getEventsOfSerie(serienId));
    }

    @Test
    void getEventsOfSerieSollNichtSichtbareEventsAussortieren() throws NoAccessException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        Event event1 = mock();
        Event event2 = mock();
        List<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        when(eventRepository.getEventsOfSerie(serienId)).thenReturn(events);
        when(event1.istSichtbarFuer(benutzerId)).thenReturn(true);
        when(event2.istSichtbarFuer(benutzerId)).thenReturn(false);
        List<Event> returnedEvents = eventRead.getEventsOfSerie(serienId);
        assertTrue(returnedEvents.contains(event1));
        assertFalse(returnedEvents.contains(event2));
    }

}
