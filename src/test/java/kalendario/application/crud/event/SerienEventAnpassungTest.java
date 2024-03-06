package kalendario.application.crud.event;

import kalendario.application.crud.serie.SerieRead;
import kalendario.application.crud.sicherheit.ExistiertNichtException;
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

public class SerienEventAnpassungTest {


    String titel = "Titel";
    HerkunftId herkunftId = mock();
    Sichtbarkeit sichtbarkeit = mock();
    String beschreibung = "Beschreibung";
    SerienRepository serienRepository = mock();
    Date originalerZeitpunktInWiederholung = mock();
    EventRepository eventRepository = mock();
    SerieRead serieRead = mock();
    EventRead eventRead = mock();
    SerienId serienId = mock();
    Serie serie = mock();
    EventId defaultEventId = mock();
    Event defaultEvent = mock();
    Date deadline = mock();
    Zeitraum zeitraum = mock();
    SerienEventAnpassung serienEventAnpassung;
    Session session = mock();
    SchreibZugriffVerifizierer schreibZugriffVerifizierer = mock();

    @BeforeEach
    void init() throws KeinZugriffException {
        serienEventAnpassung = new SerienEventAnpassung(eventRepository, serieRead, serienRepository, eventRead, session, schreibZugriffVerifizierer);
        when(serieRead.getSerie(serienId)).thenReturn(Optional.ofNullable(serie));
        when(serie.getDefaultEvent()).thenReturn(defaultEventId);
        when(eventRead.getEvent(defaultEventId)).thenReturn(Optional.of(defaultEvent));
        when(defaultEvent.getHerkunftId()).thenReturn(herkunftId);
    }

    @Test
    void createEventSollTerminSpeichernWennZeitraumMitgegeben() throws SaveException, KeinZugriffException, ExistiertNichtException {
        Zeitraum zeitraum = mock();
        Event event = serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, serienId, originalerZeitpunktInWiederholung);
        assertInstanceOf(Termin.class, event);
        verify(eventRepository).saveTermin((Termin) event);
        verify(serienRepository).addAngepasstesEvent(originalerZeitpunktInWiederholung, event.getId());
        Termin termin = (Termin) event;
        assertEquals(titel, termin.getTitel());
        assertEquals(herkunftId, termin.getHerkunftId());
        assertEquals(beschreibung, termin.getBeschreibung());
        assertEquals(zeitraum, ((Termin) termin).getZeitraum());
        assertEquals(serienId, termin.getSerienId().get());
    }

    @Test
    void createEventSollAufgabeSpeichernWennDeadlineUndGetanMitgegeben() throws SaveException, KeinZugriffException, ExistiertNichtException {
        Date deadline = mock();
        Event event = serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline,false, serienId, originalerZeitpunktInWiederholung);
        assertInstanceOf(Aufgabe.class, event);
        verify(eventRepository).saveAufgabe((Aufgabe) event);
        verify(serienRepository).addAngepasstesEvent(originalerZeitpunktInWiederholung, event.getId());
        assertEquals(titel, event.getTitel());
        assertEquals(herkunftId, event.getHerkunftId());
        assertEquals(beschreibung, event.getBeschreibung());
        assertEquals(deadline, ((Aufgabe) event).getDeadline());
        assertFalse(((Aufgabe) event).istGetan());
        assertEquals(serienId, event.getSerienId().get());

    }

    @Test
    void createEventSollGetanVonAufBesitzerSpeichernWennGetanWahrMitgegebenBeiAufgabe() throws SaveException, KeinZugriffException, ExistiertNichtException {
        Date deadline = mock();
        BenutzerId besitzerId = mock();
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(besitzerId));
        Event event = serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline, true, serienId, originalerZeitpunktInWiederholung);
        assertInstanceOf(Aufgabe.class, event);
        Aufgabe aufgabe = (Aufgabe) event;
        assertTrue(aufgabe.istGetan());
        assertEquals(besitzerId, aufgabe.wurdeGemachtVon().get());
        verify(eventRepository).saveAufgabe(aufgabe);
        verify(serienRepository).addAngepasstesEvent(originalerZeitpunktInWiederholung, event.getId());
    }

    @Test
    void createEventSollGeplanteAufgabeSpeichernWennZeitraumUndGeplantMitgegeben() throws SaveException, KeinZugriffException, ExistiertNichtException {
        Zeitraum zeitraum = mock();
        Event event = serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, false, serienId, originalerZeitpunktInWiederholung);
        assertInstanceOf(GeplanteAufgabe.class, event);
        verify(eventRepository).saveGeplanteAufgabe((GeplanteAufgabe) event);
        verify(serienRepository).addAngepasstesEvent(originalerZeitpunktInWiederholung, event.getId());
        assertEquals(titel, event.getTitel());
        assertEquals(herkunftId, event.getHerkunftId());
        assertEquals(beschreibung, event.getBeschreibung());
        assertEquals(zeitraum, ((GeplanteAufgabe) event).getZeitraum());
        assertFalse(((GeplanteAufgabe) event).istGetan());
        assertEquals(serienId, event.getSerienId().get());
    }

    @Test
    void createEventSollGetanVonAufBesitzerSpeichernWennGetanWahrMitgegebenBeiGeplanteAufgabe() throws SaveException, KeinZugriffException, ExistiertNichtException {
        Zeitraum zeitraum = mock();
        BenutzerId besitzerId = mock();
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(besitzerId));
        Event event = serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, true, serienId, originalerZeitpunktInWiederholung);
        assertInstanceOf(GeplanteAufgabe.class, event);
        GeplanteAufgabe geplanteAufgabe = (GeplanteAufgabe) event;
        assertTrue(geplanteAufgabe.istGetan());
        assertEquals(besitzerId, geplanteAufgabe.wurdeGemachtVon().get());
        verify(eventRepository).saveGeplanteAufgabe(geplanteAufgabe);
        verify(serienRepository).addAngepasstesEvent(originalerZeitpunktInWiederholung, event.getId());
    }

    @Test
    void createEventSollEventEindeutigeIdVonRepositoryGeben() throws SaveException, KeinZugriffException, ExistiertNichtException {
        EventId id = mock();
        Zeitraum zeitraum = mock();
        when(eventRepository.neueId()).thenReturn(id);
        Event event = serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, serienId, originalerZeitpunktInWiederholung);
        assertEquals(id, event.getId());
    }

    @Test
    void createEventSollSaveExceptionWerfenWennKeinSchreibzugriffAufHerkunftExistiert() throws KeinZugriffException, ExistiertNichtException {
        doThrow(KeinZugriffException.class).when(schreibZugriffVerifizierer).verifiziereZugriffFuerHerkunft(herkunftId);
        assertThrows(KeinZugriffException.class, () -> serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, serienId, originalerZeitpunktInWiederholung));
        assertThrows(KeinZugriffException.class, () -> serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline, false, serienId, originalerZeitpunktInWiederholung));
        assertThrows(KeinZugriffException.class, () -> serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, false, serienId, originalerZeitpunktInWiederholung));
        verifyNoInteractions(eventRepository);
        verifyNoInteractions(serienRepository);
    }

    @Test
    void createEventSollSaveExceptionWerfenWennKeinSchreibzugriffAufSerieExistiert() throws KeinZugriffException, ExistiertNichtException {
        doThrow(KeinZugriffException.class).when(schreibZugriffVerifizierer).verifiziereZugriffFuerSerie(serienId);
        assertThrows(KeinZugriffException.class, () -> serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, serienId, originalerZeitpunktInWiederholung));
        assertThrows(KeinZugriffException.class, () -> serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline, false, serienId, originalerZeitpunktInWiederholung));
        assertThrows(KeinZugriffException.class, () -> serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, false, serienId, originalerZeitpunktInWiederholung));
        verifyNoInteractions(eventRepository);
        verifyNoInteractions(serienRepository);
    }

    @Test
    void createEventSollSaveExceptionWerfenWennEventNichtVonSelberHerkunftWieSerieIst() throws SaveException {
        HerkunftId serienHerkunft = mock();
        when(defaultEvent.getHerkunftId()).thenReturn(serienHerkunft);
        assertThrows(SaveException.class, () -> serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, serienId, originalerZeitpunktInWiederholung));
        assertThrows(SaveException.class, () -> serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline, false, serienId, originalerZeitpunktInWiederholung));
        assertThrows(SaveException.class, () -> serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, false, serienId, originalerZeitpunktInWiederholung));
        verify(eventRepository, times(0)).saveTermin(any());
        verify(eventRepository, times(0)).saveAufgabe(any());
        verify(eventRepository, times(0)).saveGeplanteAufgabe(any());
        verifyNoInteractions(serienRepository);
    }

    @Test
    void createEventSollExceptionWerfenWennEventNichtInSerieSeinKann() throws SaveException {
        doThrow(IllegalArgumentException.class).when(serie).changeEventAnZeitpunkt(any(), any());
        assertThrows(SaveException.class, () -> serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, serienId, originalerZeitpunktInWiederholung));
        assertThrows(SaveException.class, () -> serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline, false, serienId, originalerZeitpunktInWiederholung));
        assertThrows(SaveException.class, () -> serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, false, serienId, originalerZeitpunktInWiederholung));
        verify(eventRepository, times(0)).saveTermin(any());
        verify(eventRepository, times(0)).saveAufgabe(any());
        verify(eventRepository, times(0)).saveGeplanteAufgabe(any());
        verifyNoInteractions(serienRepository);
    }

    @Test
    void createEventSollSaveExceptionWerfenWennHerkunftNichtExistiert() throws KeinZugriffException, ExistiertNichtException {
        doThrow(ExistiertNichtException.class).when(schreibZugriffVerifizierer).verifiziereZugriffFuerHerkunft(herkunftId);
        assertThrows(SaveException.class, () -> serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, serienId, originalerZeitpunktInWiederholung));
        verifyNoInteractions(eventRepository);
        verifyNoInteractions(serienRepository);
    }

    @Test
    void createEventSollSaveExceptionWerfenWennSerieNichtExistiert() throws KeinZugriffException, ExistiertNichtException {
        doThrow(ExistiertNichtException.class).when(schreibZugriffVerifizierer).verifiziereZugriffFuerSerie(serienId);
        assertThrows(SaveException.class, () -> serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, serienId, originalerZeitpunktInWiederholung));
        verifyNoInteractions(eventRepository);
        verifyNoInteractions(serienRepository);
    }

    @Test
    void createEventSollSerienSaveRollbackenWennEventSaveFehlschlaegt() throws SaveException {
        doThrow(SaveException.class).when(eventRepository).saveAufgabe(any());
        doThrow(SaveException.class).when(eventRepository).saveTermin(any());
        doThrow(SaveException.class).when(eventRepository).saveGeplanteAufgabe(any());
        assertThrows(SaveException.class, () -> serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, serienId, originalerZeitpunktInWiederholung));
        verify(serienRepository, times(1)).removeAngepasstesEvent(originalerZeitpunktInWiederholung);
        assertThrows(SaveException.class, () -> serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, deadline, false, serienId, originalerZeitpunktInWiederholung));
        verify(serienRepository, times(2)).removeAngepasstesEvent(originalerZeitpunktInWiederholung);
        assertThrows(SaveException.class, () -> serienEventAnpassung.createEvent(titel, herkunftId, sichtbarkeit, beschreibung, zeitraum, false, serienId, originalerZeitpunktInWiederholung));
        verify(serienRepository, times(3)).removeAngepasstesEvent(originalerZeitpunktInWiederholung);
    }


}
