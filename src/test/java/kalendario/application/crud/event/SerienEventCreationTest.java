package kalendario.application.crud.event;

import kalendario.application.crud.benutzer.BenutzerCreation;
import kalendario.application.crud.event.SerienEventCreation;
import kalendario.application.crud.herkunft.HerkunftRead;
import kalendario.application.crud.sicherheit.SchreibZugriffVerifizierer;
import kalendario.application.session.KeinZugriffException;
import kalendario.application.session.Session;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.value_objects.Zeitraum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class SerienEventCreationTest {


    String titel = "Titel";
    HerkunftId herkunftId = mock();
    Sichtbarkeit sichtbarkeit = mock();
    String beschreibung = "Beschreibung";
    EventRepository eventRepository = mock();
    SerienId serie = mock();
    SerienEventCreation serienEventCreation;
    Session session = mock();
    SchreibZugriffVerifizierer schreibZugriffVerifizierer = mock();

    @BeforeEach
    void init() {
        serienEventCreation = new SerienEventCreation(eventRepository, session, schreibZugriffVerifizierer);
    }

    @Test
    void createEventSollTerminSpeichernWennZeitraumMitgegeben() throws SaveException, KeinZugriffException {
        Zeitraum zeitraum = mock();
        Event event = serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, serie);
        assertInstanceOf(Termin.class, event);
        verify(eventRepository).saveTermin((Termin) event);
        Termin termin = (Termin) event;
        assertEquals(titel, termin.getTitel());
        assertEquals(herkunftId, termin.getHerkunftId());
        assertEquals(beschreibung, termin.getBeschreibung());
        assertEquals(zeitraum, ((Termin) termin).getZeitraum());
        assertEquals(serie, termin.getSerienId().get());
    }

    @Test
    void createEventSollAufgabeSpeichernWennDeadlineUndGetanMitgegeben() throws SaveException, KeinZugriffException {
        Date deadline = mock();
        Event event = serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline,false, serie);
        assertInstanceOf(Aufgabe.class, event);
        verify(eventRepository).saveAufgabe((Aufgabe) event);
        assertEquals(titel, event.getTitel());
        assertEquals(herkunftId, event.getHerkunftId());
        assertEquals(beschreibung, event.getBeschreibung());
        assertEquals(deadline, ((Aufgabe) event).getDeadline());
        assertFalse(((Aufgabe) event).istGetan());
        assertEquals(serie, event.getSerienId().get());

    }

    @Test
    void createEventSollGetanVonAufBesitzerSpeichernWennGetanWahrMitgegebenBeiAufgabe() throws SaveException, KeinZugriffException {
        Date deadline = mock();
        BenutzerId besitzerId = mock();
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(besitzerId));
        Event event = serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline, true, serie);
        assertInstanceOf(Aufgabe.class, event);
        Aufgabe aufgabe = (Aufgabe) event;
        assertTrue(aufgabe.istGetan());
        assertEquals(besitzerId, aufgabe.wurdeGemachtVon().get());
        verify(eventRepository).saveAufgabe(aufgabe);
    }

    @Test
    void createEventSollGeplanteAufgabeSpeichernWennZeitraumUndGeplantMitgegeben() throws SaveException, KeinZugriffException {
        Zeitraum zeitraum = mock();
        Event event = serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, false, serie);
        assertInstanceOf(GeplanteAufgabe.class, event);
        verify(eventRepository).saveGeplanteAufgabe((GeplanteAufgabe) event);
        assertEquals(titel, event.getTitel());
        assertEquals(herkunftId, event.getHerkunftId());
        assertEquals(beschreibung, event.getBeschreibung());
        assertEquals(zeitraum, ((GeplanteAufgabe) event).getZeitraum());
        assertFalse(((GeplanteAufgabe) event).istGetan());
        assertEquals(serie, event.getSerienId().get());
    }

    @Test
    void createEventSollGetanVonAufBesitzerSpeichernWennGetanWahrMitgegebenBeiGeplanteAufgabe() throws SaveException, KeinZugriffException {
        Zeitraum zeitraum = mock();
        BenutzerId besitzerId = mock();
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(besitzerId));
        Event event = serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, true, serie);
        assertInstanceOf(GeplanteAufgabe.class, event);
        GeplanteAufgabe geplanteAufgabe = (GeplanteAufgabe) event;
        assertTrue(geplanteAufgabe.istGetan());
        assertEquals(besitzerId, geplanteAufgabe.wurdeGemachtVon().get());
        verify(eventRepository).saveGeplanteAufgabe(geplanteAufgabe);
    }

    @Test
    void createEventSollEventEindeutigeIdVonRepositoryGeben() throws SaveException, KeinZugriffException {
        EventId id = mock();
        Zeitraum zeitraum = mock();
        when(eventRepository.neueId()).thenReturn(id);
        Event event = serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, serie);
        assertEquals(id, event.getId());
    }

    @Test
    void createEventSollSaveExceptionWerfenWennKeinSchreibzugriffAufHerkunftExistiert() throws KeinZugriffException {
        Date deadline = mock();
        Zeitraum zeitraum = mock();
        doThrow(KeinZugriffException.class).when(schreibZugriffVerifizierer).verifiziereZugriffFuerHerkunft(herkunftId);
        assertThrows(KeinZugriffException.class, () -> serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, serie));
        assertThrows(KeinZugriffException.class, () -> serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline, false, serie));
        assertThrows(KeinZugriffException.class, () -> serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, false, serie));
        verifyNoInteractions(eventRepository);
    }

}
