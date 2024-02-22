package kalendario.application.crud;

import kalendario.domain.entities.benutzer.Benutzer;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.value_objects.Zeitraum;
import org.junit.jupiter.api.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class EventCreationTest {

    String titel = "Titel";
    Herkunft herkunft = mock();
    Sichtbarkeit sichtbarkeit = mock();
    String beschreibung = "Beschreibung";
    EventRepository eventRepository = mock();
    EventCreation eventCreation;

    @BeforeEach
    void init() {
        eventCreation = new EventCreation(eventRepository);
    }

    @Test
    void createEventSollTerminSpeichernWennZeitraumMitgegeben() throws SaveException {
        Zeitraum zeitraum = mock();
        Event event = eventCreation.createEvent(titel, herkunft, sichtbarkeit, beschreibung, zeitraum);
        assertInstanceOf(Termin.class, event);
        verify(eventRepository).saveTermin((Termin) event);
    }

    @Test
    void createEventSollAufgabeSpeichernWennDeadlineUndGetanMitgegeben() throws SaveException {
        Date deadline = mock();
        Event event = eventCreation.createEvent(titel, herkunft, sichtbarkeit, beschreibung, deadline, false);
        assertInstanceOf(Aufgabe.class, event);
        verify(eventRepository).saveAufgabe((Aufgabe) event);
    }

    @Test
    void createEventSollGetanVonAufBesitzerSpeichernWennGetanWahrMitgegebenBeiAufgabe() throws SaveException {
        Date deadline = mock();
        BenutzerId besitzerId = mock();
        when(herkunft.getBesitzerId()).thenReturn(besitzerId);
        Event event = eventCreation.createEvent(titel, herkunft, sichtbarkeit, beschreibung, deadline, true);
        assertInstanceOf(Aufgabe.class, event);
        Aufgabe aufgabe = (Aufgabe) event;
        assertTrue(aufgabe.istGetan());
        assertEquals(besitzerId, aufgabe.wurdeGemachtVon().get());
        verify(eventRepository).saveAufgabe(aufgabe);
    }

    @Test
    void createEventSollGeplanteAufgabeSpeichernWennZeitraumUndGeplantMitgegeben() throws SaveException {
        Zeitraum zeitraum = mock();
        Event event = eventCreation.createEvent(titel, herkunft, sichtbarkeit, beschreibung, zeitraum, false);
        assertInstanceOf(GeplanteAufgabe.class, event);
        verify(eventRepository).saveGeplanteAufgabe((GeplanteAufgabe) event);
    }

    @Test
    void createEventSollGetanVonAufBesitzerSpeichernWennGetanWahrMitgegebenBeiGeplanteAufgabe() throws SaveException {
        Zeitraum zeitraum = mock();
        BenutzerId besitzerId = mock();
        when(herkunft.getBesitzerId()).thenReturn(besitzerId);
        Event event = eventCreation.createEvent(titel, herkunft, sichtbarkeit, beschreibung, zeitraum, true);
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
        assertThrows(SaveException.class, () -> eventCreation.createEvent(titel, herkunft, sichtbarkeit, beschreibung, zeitraum));
    }

    @Test
    void createEventSollBenutzerEindeutigeIdVonRepositoryGeben() throws SaveException, BenutzerCreation.BenutzerNameExistiertException {
        Zeitraum zeitraum = mock();
        EventId id = mock();
        when(eventRepository.neueId()).thenReturn(id);
        Event event = eventCreation.createEvent(titel, herkunft, sichtbarkeit, beschreibung, zeitraum);
        assertEquals(id, event.getId());
    }

}
