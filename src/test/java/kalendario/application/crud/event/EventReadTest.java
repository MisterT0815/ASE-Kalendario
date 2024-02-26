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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void getAufgabeSollAufgabeVonEventRepositoryZurueckgeben() throws NotAvailableException {
        when(eventRepository.getEvent(eventId)).thenReturn(aufgabe);
        assertEquals(aufgabe, eventRead.getAufgabe(eventId));
    }

    @Test
    void getGeplanteAufgabeSollGeplanteAufgabeVonEventRepositoryZurueckgeben() throws NotAvailableException {
        when(eventRepository.getEvent(eventId)).thenReturn(geplanteAufgabe);
        assertEquals(geplanteAufgabe, eventRead.getGeplanteAufgabe(eventId));
    }

    @Test
    void getTerminSollTerminvonEventRepositoryZurueckgeben() throws NotAvailableException {
        when(eventRepository.getEvent(eventId)).thenReturn(termin);
        assertEquals(termin, eventRead.getTermin(eventId));
    }

    @Test
    void getAufgabeSollExceptionWerfenWennRepositoryMitKeinerAufgabeAntwortet(){
        when(eventRepository.getEvent(eventId)).thenReturn(null);
        assertThrows(NotAvailableException.class, () -> eventRead.getAufgabe(eventId));
        when(eventRepository.getEvent(eventId)).thenReturn(geplanteAufgabe);
        assertThrows(NotAvailableException.class, () -> eventRead.getAufgabe(eventId));
    }

    @Test
    void getGeplanteAufgabeSollExceptionWerfenWennRepositoryMitKeinerGeplanteAufgabeAntwortet(){
        when(eventRepository.getEvent(eventId)).thenReturn(null);
        assertThrows(NotAvailableException.class, () -> eventRead.getGeplanteAufgabe(eventId));
        when(eventRepository.getEvent(eventId)).thenReturn(aufgabe);
        assertThrows(NotAvailableException.class, () -> eventRead.getGeplanteAufgabe(eventId));
    }

    @Test
    void getTerminSollExceptionWerfenWennRepositoryMitKeinemTerminAntwortet(){
        when(eventRepository.getEvent(eventId)).thenReturn(null);
        assertThrows(NotAvailableException.class, () -> eventRead.getTermin(eventId));
        when(eventRepository.getEvent(eventId)).thenReturn(aufgabe);
        assertThrows(NotAvailableException.class, () -> eventRead.getTermin(eventId));
    }

    @Test
    void getEventsOfSerieSollEventsVonRepositoryZurueckgeben(){
        when(eventRepository.getEventsOfSerie(serienId)).thenReturn(events);
        assertEquals(events, eventRepository.getEventsOfSerie(serienId));
    }



}
