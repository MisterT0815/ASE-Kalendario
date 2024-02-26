package kalendario.application.crud.serie;

import kalendario.application.crud.benutzer.BenutzerCreation;
import kalendario.application.crud.event.SerienEventCreation;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.value_objects.Zeitraum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class SerienEventCreationTest {


    String titel = "Titel";
    Herkunft herkunft = mock();
    Sichtbarkeit sichtbarkeit = mock();
    String beschreibung = "Beschreibung";
    EventRepository eventRepository = mock();
    SerienId serie = mock();
    SerienEventCreation serienEventCreation;

    @BeforeEach
    void init() {
        serienEventCreation = new SerienEventCreation(eventRepository);
    }

    @Test
    void createEventSollTerminSpeichernWennZeitraumMitgegeben() throws SaveException {
        Zeitraum zeitraum = mock();
        Event event = serienEventCreation.createEvent(titel, herkunft, sichtbarkeit, beschreibung, zeitraum, serie);
        assertInstanceOf(Termin.class, event);
        verify(eventRepository).saveTermin((Termin) event);
        Termin termin = (Termin) event;
        assertEquals(titel, termin.getTitel());
        assertEquals(herkunft, termin.getHerkunft());
        assertEquals(beschreibung, termin.getBeschreibung());
        assertEquals(zeitraum, ((Termin) termin).getZeitraum());
        //Workaround, da Sichtbarkeit nicht per getter erreichbar
        BenutzerId benutzerId = mock();
        BenutzerId besitzerId = mock();
        when(herkunft.getBesitzerId()).thenReturn(besitzerId);
        termin.istSichtbarFuer(benutzerId);
        verify(sichtbarkeit).istSichtbarFuer(benutzerId);
        assertEquals(serie, termin.getSerienId().get());
    }

    @Test
    void createEventSollAufgabeSpeichernWennDeadlineUndGetanMitgegeben() throws SaveException {
        Date deadline = mock();
        Event event = serienEventCreation.createEvent(titel, herkunft, sichtbarkeit, beschreibung, deadline,false, serie);
        assertInstanceOf(Aufgabe.class, event);
        verify(eventRepository).saveAufgabe((Aufgabe) event);
        assertEquals(titel, event.getTitel());
        assertEquals(herkunft, event.getHerkunft());
        assertEquals(beschreibung, event.getBeschreibung());
        assertEquals(deadline, ((Aufgabe) event).getDeadline());
        assertFalse(((Aufgabe) event).istGetan());
        //Workaround, da Sichtbarkeit nicht per getter erreichbar
        BenutzerId benutzerId = mock();
        BenutzerId besitzerId = mock();
        when(herkunft.getBesitzerId()).thenReturn(besitzerId);
        event.istSichtbarFuer(benutzerId);
        verify(sichtbarkeit).istSichtbarFuer(benutzerId);
        assertEquals(serie, event.getSerienId().get());

    }

    @Test
    void createEventSollGetanVonAufBesitzerSpeichernWennGetanWahrMitgegebenBeiAufgabe() throws SaveException {
        Date deadline = mock();
        BenutzerId besitzerId = mock();
        when(herkunft.getBesitzerId()).thenReturn(besitzerId);
        Event event = serienEventCreation.createEvent(titel, herkunft, sichtbarkeit, beschreibung, deadline, true, serie);
        assertInstanceOf(Aufgabe.class, event);
        Aufgabe aufgabe = (Aufgabe) event;
        assertTrue(aufgabe.istGetan());
        assertEquals(besitzerId, aufgabe.wurdeGemachtVon().get());
        verify(eventRepository).saveAufgabe(aufgabe);
    }

    @Test
    void createEventSollGeplanteAufgabeSpeichernWennZeitraumUndGeplantMitgegeben() throws SaveException {
        Zeitraum zeitraum = mock();
        Event event = serienEventCreation.createEvent(titel, herkunft, sichtbarkeit, beschreibung, zeitraum, false, serie);
        assertInstanceOf(GeplanteAufgabe.class, event);
        verify(eventRepository).saveGeplanteAufgabe((GeplanteAufgabe) event);
        assertEquals(titel, event.getTitel());
        assertEquals(herkunft, event.getHerkunft());
        assertEquals(beschreibung, event.getBeschreibung());
        assertEquals(zeitraum, ((GeplanteAufgabe) event).getZeitraum());
        assertFalse(((GeplanteAufgabe) event).istGetan());
        //Workaround, da Sichtbarkeit nicht per getter erreichbar
        BenutzerId benutzerId = mock();
        BenutzerId besitzerId = mock();
        when(herkunft.getBesitzerId()).thenReturn(besitzerId);
        event.istSichtbarFuer(benutzerId);
        verify(sichtbarkeit).istSichtbarFuer(benutzerId);
        assertEquals(serie, event.getSerienId().get());
    }

    @Test
    void createEventSollGetanVonAufBesitzerSpeichernWennGetanWahrMitgegebenBeiGeplanteAufgabe() throws SaveException {
        Zeitraum zeitraum = mock();
        BenutzerId besitzerId = mock();
        when(herkunft.getBesitzerId()).thenReturn(besitzerId);
        Event event = serienEventCreation.createEvent(titel, herkunft, sichtbarkeit, beschreibung, zeitraum, true, serie);
        assertInstanceOf(GeplanteAufgabe.class, event);
        GeplanteAufgabe geplanteAufgabe = (GeplanteAufgabe) event;
        assertTrue(geplanteAufgabe.istGetan());
        assertEquals(besitzerId, geplanteAufgabe.wurdeGemachtVon().get());
        verify(eventRepository).saveGeplanteAufgabe(geplanteAufgabe);
    }

    @Test
    void createEventSollEventEindeutigeIdVonRepositoryGeben() throws SaveException, BenutzerCreation.BenutzerNameExistiertException {
        EventId id = mock();
        Zeitraum zeitraum = mock();
        when(eventRepository.neueId()).thenReturn(id);
        Event event = serienEventCreation.createEvent(titel, herkunft, sichtbarkeit, beschreibung, zeitraum, serie);
        assertEquals(id, event.getId());
    }

}
