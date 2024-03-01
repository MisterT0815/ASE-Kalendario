package kalendario.application.crud.event;

import kalendario.application.crud.serie.SerieRead;
import kalendario.application.crud.serie.SerieUpdate;
import kalendario.application.crud.sicherheit.SchreibZugriffVerifizierer;
import kalendario.application.session.KeinZugriffException;
import kalendario.application.session.Session;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.repositories.SerienRepository;
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
    Date originalerZeitpunktInWiederholung = mock();
    EventRepository eventRepository = mock();
    SerieRead serieRead = mock();
    SerieUpdate serieUpdate = mock();
    EventRead eventRead = mock();
    SerienId serienId = mock();
    Serie serie = mock();
    EventId defaultEventId = mock();
    Event defaultEvent = mock();
    Date deadline = mock();
    Zeitraum zeitraum = mock();
    SerienEventCreation serienEventCreation;
    Session session = mock();
    SchreibZugriffVerifizierer schreibZugriffVerifizierer = mock();

    @BeforeEach
    void init() throws KeinZugriffException {
        serienEventCreation = new SerienEventCreation(eventRepository, serieRead, serieUpdate, eventRead, session, schreibZugriffVerifizierer);
        when(serieRead.getSerie(serienId)).thenReturn(Optional.ofNullable(serie));
        when(serie.getDefaultEvent()).thenReturn(defaultEventId);
        when(eventRead.getEvent(defaultEventId)).thenReturn(Optional.of(defaultEvent));
        when(defaultEvent.getHerkunftId()).thenReturn(herkunftId);
    }

    @Test
    void createEventSollTerminSpeichernWennZeitraumMitgegeben() throws SaveException, KeinZugriffException {
        Zeitraum zeitraum = mock();
        Event event = serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, serienId, originalerZeitpunktInWiederholung);
        assertInstanceOf(Termin.class, event);
        verify(eventRepository).saveTermin((Termin) event);
        Termin termin = (Termin) event;
        assertEquals(titel, termin.getTitel());
        assertEquals(herkunftId, termin.getHerkunftId());
        assertEquals(beschreibung, termin.getBeschreibung());
        assertEquals(zeitraum, ((Termin) termin).getZeitraum());
        assertEquals(serienId, termin.getSerienId().get());
    }

    @Test
    void createEventSollAufgabeSpeichernWennDeadlineUndGetanMitgegeben() throws SaveException, KeinZugriffException {
        Date deadline = mock();
        Event event = serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline,false, serienId, originalerZeitpunktInWiederholung);
        assertInstanceOf(Aufgabe.class, event);
        verify(eventRepository).saveAufgabe((Aufgabe) event);
        assertEquals(titel, event.getTitel());
        assertEquals(herkunftId, event.getHerkunftId());
        assertEquals(beschreibung, event.getBeschreibung());
        assertEquals(deadline, ((Aufgabe) event).getDeadline());
        assertFalse(((Aufgabe) event).istGetan());
        assertEquals(serienId, event.getSerienId().get());

    }

    @Test
    void createEventSollGetanVonAufBesitzerSpeichernWennGetanWahrMitgegebenBeiAufgabe() throws SaveException, KeinZugriffException {
        Date deadline = mock();
        BenutzerId besitzerId = mock();
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(besitzerId));
        Event event = serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline, true, serienId, originalerZeitpunktInWiederholung);
        assertInstanceOf(Aufgabe.class, event);
        Aufgabe aufgabe = (Aufgabe) event;
        assertTrue(aufgabe.istGetan());
        assertEquals(besitzerId, aufgabe.wurdeGemachtVon().get());
        verify(eventRepository).saveAufgabe(aufgabe);
    }

    @Test
    void createEventSollGeplanteAufgabeSpeichernWennZeitraumUndGeplantMitgegeben() throws SaveException, KeinZugriffException {
        Zeitraum zeitraum = mock();
        Event event = serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, false, serienId, originalerZeitpunktInWiederholung);
        assertInstanceOf(GeplanteAufgabe.class, event);
        verify(eventRepository).saveGeplanteAufgabe((GeplanteAufgabe) event);
        assertEquals(titel, event.getTitel());
        assertEquals(herkunftId, event.getHerkunftId());
        assertEquals(beschreibung, event.getBeschreibung());
        assertEquals(zeitraum, ((GeplanteAufgabe) event).getZeitraum());
        assertFalse(((GeplanteAufgabe) event).istGetan());
        assertEquals(serienId, event.getSerienId().get());
    }

    @Test
    void createEventSollGetanVonAufBesitzerSpeichernWennGetanWahrMitgegebenBeiGeplanteAufgabe() throws SaveException, KeinZugriffException {
        Zeitraum zeitraum = mock();
        BenutzerId besitzerId = mock();
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(besitzerId));
        Event event = serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, true, serienId, originalerZeitpunktInWiederholung);
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
        Event event = serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, serienId, originalerZeitpunktInWiederholung);
        assertEquals(id, event.getId());
    }

    @Test
    void createEventSollSaveExceptionWerfenWennKeinSchreibzugriffAufHerkunftExistiert() throws KeinZugriffException {
        doThrow(KeinZugriffException.class).when(schreibZugriffVerifizierer).verifiziereZugriffFuerHerkunft(herkunftId);
        assertThrows(KeinZugriffException.class, () -> serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, serienId, originalerZeitpunktInWiederholung));
        assertThrows(KeinZugriffException.class, () -> serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline, false, serienId, originalerZeitpunktInWiederholung));
        assertThrows(KeinZugriffException.class, () -> serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, false, serienId, originalerZeitpunktInWiederholung));
        verifyNoInteractions(eventRepository);
    }

    @Test
    void createEventSollSaveExceptionWerfenWennKeinSchreibzugriffAufSerieExistiert() throws KeinZugriffException{
        when(serieRead.getSerie(serienId)).thenThrow(KeinZugriffException.class);
        assertThrows(KeinZugriffException.class, () -> serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, serienId, originalerZeitpunktInWiederholung));
        assertThrows(KeinZugriffException.class, () -> serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline, false, serienId, originalerZeitpunktInWiederholung));
        assertThrows(KeinZugriffException.class, () -> serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, false, serienId, originalerZeitpunktInWiederholung));
    }

    @Test
    void createEventSollSaveExceptionWerfenWennEventNichtVonSelberHerkunftWieSerieIst(){
        HerkunftId serienHerkunft = mock();
        when(defaultEvent.getHerkunftId()).thenReturn(serienHerkunft);
        assertThrows(SaveException.class, () -> serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, serienId, originalerZeitpunktInWiederholung));
        assertThrows(SaveException.class, () -> serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline, false, serienId, originalerZeitpunktInWiederholung));
        assertThrows(SaveException.class, () -> serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, false, serienId, originalerZeitpunktInWiederholung));
    }

    @Test
    void createEventSollExceptionWerfenWennEventNichtInSerieSeinKann(){
        doThrow(IllegalArgumentException.class).when(serie).changeEventAnZeitpunkt(any(), any());
        assertThrows(SaveException.class, () -> serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, serienId, originalerZeitpunktInWiederholung));
        assertThrows(SaveException.class, () -> serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline, false, serienId, originalerZeitpunktInWiederholung));
        assertThrows(SaveException.class, () -> serienEventCreation.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, false, serienId, originalerZeitpunktInWiederholung));
    }

}
