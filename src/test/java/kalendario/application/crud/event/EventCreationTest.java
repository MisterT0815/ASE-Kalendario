package kalendario.application.crud.event;

import kalendario.application.crud.benutzer.BenutzerCreation;
import kalendario.application.crud.herkunft.HerkunftRead;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.value_objects.Zeitraum;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class EventCreationTest {

    String titel = "Titel";
    HerkunftId herkunftId = mock();
    Sichtbarkeit sichtbarkeit = mock();
    String beschreibung = "Beschreibung";
    EventRepository eventRepository = mock();
    EventCreation eventCreation;
    HerkunftRead herkunftRead = mock();
    Herkunft herkunft = mock();

    @BeforeEach
    void init() {
        eventCreation = new EventCreation(eventRepository, herkunftRead);
    }

    @Test
    void createEventSollTerminSpeichernWennZeitraumMitgegeben() throws SaveException {
        Zeitraum zeitraum = mock();
        Event event = eventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum);
        assertInstanceOf(Termin.class, event);
        verify(eventRepository).saveTermin((Termin) event);
        assertEquals(titel, event.getTitel());
        assertEquals(herkunftId, event.getHerkunftId());
        assertEquals(beschreibung, event.getBeschreibung());
        assertEquals(zeitraum, ((Termin) event).getZeitraum());
    }

    @Test
    void createEventSollAufgabeSpeichernWennDeadlineUndGetanMitgegeben() throws SaveException {
        Date deadline = mock();
        Event event = eventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline, false);
        assertInstanceOf(Aufgabe.class, event);
        verify(eventRepository).saveAufgabe((Aufgabe) event);
        assertEquals(titel, event.getTitel());
        assertEquals(herkunftId, event.getHerkunftId());
        assertEquals(beschreibung, event.getBeschreibung());
        assertEquals(deadline, ((Aufgabe) event).getDeadline());
        assertFalse(((Aufgabe) event).istGetan());
    }

    @Test
    void createEventSollGetanVonAufBesitzerSpeichernWennGetanWahrMitgegebenBeiAufgabe() throws SaveException {
        Date deadline = mock();
        BenutzerId besitzerId = mock();
        when(herkunftRead.getHerkunft(herkunftId)).thenReturn(Optional.of(herkunft));
        when(herkunft.getBesitzerId()).thenReturn(besitzerId);
        Event event = eventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline, true);
        assertInstanceOf(Aufgabe.class, event);
        Aufgabe aufgabe = (Aufgabe) event;
        assertTrue(aufgabe.istGetan());
        assertEquals(besitzerId, aufgabe.wurdeGemachtVon().get());
        verify(eventRepository).saveAufgabe(aufgabe);
    }

    @Test
    void createEventSollGeplanteAufgabeSpeichernWennZeitraumUndGeplantMitgegeben() throws SaveException {
        Zeitraum zeitraum = mock();
        Event event = eventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, false);
        assertInstanceOf(GeplanteAufgabe.class, event);
        verify(eventRepository).saveGeplanteAufgabe((GeplanteAufgabe) event);
        assertEquals(titel, event.getTitel());
        assertEquals(herkunftId, event.getHerkunftId());
        assertEquals(beschreibung, event.getBeschreibung());
        assertEquals(zeitraum, ((GeplanteAufgabe) event).getZeitraum());
        assertFalse(((GeplanteAufgabe) event).istGetan());
    }

    @Test
    void createEventSollGetanVonAufBesitzerSpeichernWennGetanWahrMitgegebenBeiGeplanteAufgabe() throws SaveException {
        Zeitraum zeitraum = mock();
        BenutzerId besitzerId = mock();
        when(herkunftRead.getHerkunft(herkunftId)).thenReturn(Optional.of(herkunft));
        when(herkunft.getBesitzerId()).thenReturn(besitzerId);
        Event event = eventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, true);
        assertInstanceOf(GeplanteAufgabe.class, event);
        GeplanteAufgabe geplanteAufgabe = (GeplanteAufgabe) event;
        assertTrue(geplanteAufgabe.istGetan());
        assertEquals(besitzerId, geplanteAufgabe.wurdeGemachtVon().get());
        verify(eventRepository).saveGeplanteAufgabe(geplanteAufgabe);
    }

    @Test
    void createEventSollSaveExceptionWerfenWennSpeichernInEventRepositoryUnerfolgreichWar() throws SaveException {
        Zeitraum zeitraum = mock();
        doThrow(SaveException.class).when(eventRepository).saveTermin(any(Termin.class));
        assertThrows(SaveException.class, () -> eventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum));
    }

    @Test
    void createEventSollEventEindeutigeIdVonRepositoryGeben() throws SaveException {
        Zeitraum zeitraum = mock();
        EventId id = mock();
        when(eventRepository.neueId()).thenReturn(id);
        Event event = eventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum);
        assertEquals(id, event.getId());
    }

    @Test
    void createAufgabeSollSaveExceptionWerfenWennHerkunftUngueltig(){
        Date deadline = mock();
        BenutzerId besitzerId = mock();
        when(herkunftRead.getHerkunft(herkunftId)).thenReturn(Optional.empty());
        assertThrows(SaveException.class, () -> eventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline, true));
    }

}
