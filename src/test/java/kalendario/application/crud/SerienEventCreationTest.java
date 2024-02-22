package kalendario.application.crud;

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
    }

    @Test
    void createEventSollAufgabeSpeichernWennDeadlineUndGetanMitgegeben() throws SaveException {
        Date deadline = mock();
        Event event = serienEventCreation.createEvent(titel, herkunft, sichtbarkeit, beschreibung, deadline,false, serie);
        assertInstanceOf(Aufgabe.class, event);
        verify(eventRepository).saveAufgabe((Aufgabe) event);
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

}
