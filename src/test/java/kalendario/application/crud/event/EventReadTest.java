package kalendario.application.crud.event;

import kalendario.application.crud.exception.NotAvailableException;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Not;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class EventReadTest {

    EventRepository eventRepository = mock();
    EventId eventId = mock();
    Event event = mock();
    Aufgabe aufgabe = mock();
    GeplanteAufgabe geplanteAufgabe = mock();
    Termin termin = mock();
    SerienId serienId = mock();
    List<Event> events = mock();
    EventRead eventRead;

    @BeforeEach
    void init(){
        eventRead = new EventRead(eventRepository);
    }

    @Test
    void getEventSollEventMitIdVonRepositoryHolen(){
        when(eventRepository.getEvent(eventId)).thenReturn(event);
        assertEquals(event, eventRead.getEvent(eventId));
    }

    @Test
    void getAufgabeSollAufgabeVonEventRepositoryZurueckgeben() {
        when(eventRepository.getEvent(eventId)).thenReturn(aufgabe);
        assertEquals(aufgabe, eventRead.getAufgabe(eventId).get());
    }

    @Test
    void getGeplanteAufgabeSollGeplanteAufgabeVonEventRepositoryZurueckgeben() {
        when(eventRepository.getEvent(eventId)).thenReturn(geplanteAufgabe);
        assertEquals(geplanteAufgabe, eventRead.getGeplanteAufgabe(eventId).get());
    }

    @Test
    void getTerminSollTerminvonEventRepositoryZurueckgeben() {
        when(eventRepository.getEvent(eventId)).thenReturn(termin);
        assertEquals(termin, eventRead.getTermin(eventId).get());
    }

    @Test
    void getAufgabeSollExceptionWerfenWennRepositoryMitKeinerAufgabeAntwortet(){
        when(eventRepository.getEvent(eventId)).thenReturn(null);
        assertTrue(eventRead.getAufgabe(eventId).isEmpty());
        when(eventRepository.getEvent(eventId)).thenReturn(geplanteAufgabe);
        assertTrue(eventRead.getAufgabe(eventId).isEmpty());
    }

    @Test
    void getGeplanteAufgabeSollExceptionWerfenWennRepositoryMitKeinerGeplanteAufgabeAntwortet(){
        when(eventRepository.getEvent(eventId)).thenReturn(null);
        assertTrue(eventRead.getGeplanteAufgabe(eventId).isEmpty());
        when(eventRepository.getEvent(eventId)).thenReturn(aufgabe);
        assertTrue(eventRead.getGeplanteAufgabe(eventId).isEmpty());
    }

    @Test
    void getTerminSollExceptionWerfenWennRepositoryMitKeinemTerminAntwortet(){
        when(eventRepository.getEvent(eventId)).thenReturn(null);
        assertTrue(eventRead.getTermin(eventId).isEmpty());
        when(eventRepository.getEvent(eventId)).thenReturn(aufgabe);
        assertTrue(eventRead.getTermin(eventId).isEmpty());
    }

    @Test
    void getEventsOfSerieSollEventsVonRepositoryZurueckgeben(){
        when(eventRepository.getEventsOfSerie(serienId)).thenReturn(events);
        assertEquals(events, eventRepository.getEventsOfSerie(serienId));
    }



}
