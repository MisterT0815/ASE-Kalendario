package kalendario.application.crud.event;

import kalendario.application.crud.benutzer.BenutzerRead;
import kalendario.application.crud.sicherheit.ExistiertNichtException;
import kalendario.application.crud.sicherheit.SchreibZugriffVerifizierer;
import kalendario.application.session.KeinZugriffException;
import kalendario.domain.entities.event.*;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.SaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventUpdateTest {

    EventRepository eventRepository = mock();
    EventRead eventRead = mock();
    BenutzerRead benutzerRead = mock();
    SchreibZugriffVerifizierer schreibZugriffVerifizierer = mock();
    EventId eventId = mock();
    Event event = mock();
    Sichtbarkeit sichtbarkeit = mock();
    PrivateSichtbarkeit privateSichtbarkeit = mock();


    EventUpdate eventUpdate;

    @BeforeEach
    void init(){
        eventUpdate = new EventUpdate(eventRepository, eventRead, benutzerRead, schreibZugriffVerifizierer);
        when(eventRepository.getEvent(eventId)).thenReturn(event);
    }

    @Test
    void setzeTitelSollRepositoryRufen() throws KeinZugriffException, SaveException, ExistiertNichtException {
        String titel = "";
        eventUpdate.setzeTitel(eventId, titel);
        verify(eventRepository).setTitel(eventId, titel);
    }

    @Test
    void setzeBeschreibungSollRepositoryRufen() throws KeinZugriffException, SaveException, ExistiertNichtException {
        String beschreibung = "";
        eventUpdate.setzteBeschreibung(eventId, beschreibung);
        verify(eventRepository).setBeschreibung(eventId, beschreibung);
    }

    @Test
    void machOeffentlichSollPublicSichtbarkeitAlsSichtbarkeitSetzen() throws KeinZugriffException, SaveException, ExistiertNichtException {
        eventUpdate.machOeffentlich(eventId);
        ArgumentCaptor<EventId> eventIds = ArgumentCaptor.forClass(EventId.class);
        ArgumentCaptor<Sichtbarkeit> sichtbarkeiten = ArgumentCaptor.forClass(Sichtbarkeit.class);
        verify(eventRepository).setSichtbarkeit(eventIds.capture(), sichtbarkeiten.capture());
        assertEquals(1, eventIds.getAllValues().size());
        assertEquals(eventId, eventIds.getValue());
        assertInstanceOf(PublicSichtbarkeit.class, sichtbarkeiten.getValue());
    }

    @Test
    void machPrivatSollPrivateSichtbarkeitAlsSichtbarkeitSetzen() throws KeinZugriffException, SaveException, ExistiertNichtException {
        eventUpdate.machPrivat(eventId);
        ArgumentCaptor<EventId> eventIds = ArgumentCaptor.forClass(EventId.class);
        ArgumentCaptor<Sichtbarkeit> sichtbarkeiten = ArgumentCaptor.forClass(Sichtbarkeit.class);
        verify(eventRepository).setSichtbarkeit(eventIds.capture(), sichtbarkeiten.capture());
        assertEquals(1, eventIds.getAllValues().size());
        assertEquals(eventId, eventIds.getValue());
        assertInstanceOf(PrivateSichtbarkeit.class, sichtbarkeiten.getValue());
    }


}
