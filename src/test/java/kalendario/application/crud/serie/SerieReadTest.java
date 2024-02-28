package kalendario.application.crud.serie;

import kalendario.application.crud.event.EventRead;
import kalendario.application.session.NoAccessException;
import kalendario.application.session.Session;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.Event;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.SerienRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class SerieReadTest {

    SerienRepository serienRepository = mock();
    EventRead eventRead = mock();
    BenutzerId benutzerId = mock();
    Session session = mock();
    SerienId serienId = mock();
    Serie serie = mock();
    Event defaultEvent = mock();
    EventId defaultEventId = mock();
    SerieRead serieRead;

    @BeforeEach
    void init(){
        serieRead = new SerieRead(serienRepository, session, eventRead);
    }

    @Test
    void getSerieSollSerieVonSerienRepositoryHolen() throws NoAccessException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        when(serie.getDefaultEvent()).thenReturn(defaultEventId);
        when(eventRead.getEvent(defaultEventId)).thenReturn(Optional.of(defaultEvent));
        when(defaultEvent.istSichtbarFuer(benutzerId)).thenReturn(true);
        when(serienRepository.getSerie(serienId)).thenReturn(serie);
        assertEquals(serie, serieRead.getSerie(serienId).get());
    }

    @Test
    void getSerieSollEmptyOptionalZurueckgebenWennSerienRepositoryNichtsZurueckgibt() throws NoAccessException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        when(serie.getDefaultEvent()).thenReturn(defaultEventId);
        when(eventRead.getEvent(defaultEventId)).thenReturn(Optional.of(defaultEvent));
        when(defaultEvent.istSichtbarFuer(benutzerId)).thenReturn(true);
        when(serienRepository.getSerie(serienId)).thenReturn(null);
        assertTrue(serieRead.getSerie(serienId).isEmpty());
    }

    @Test
    void getSerieSollNoAccessExceptionWerfenBasierendAufSichtbarkeitDesDefaultEvents() throws NoAccessException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        when(serie.getDefaultEvent()).thenReturn(defaultEventId);
        when(eventRead.getEvent(defaultEventId)).thenReturn(Optional.of(defaultEvent));
        when(defaultEvent.istSichtbarFuer(benutzerId)).thenReturn(false);
        when(serienRepository.getSerie(serienId)).thenReturn(serie);
        assertThrows(NoAccessException.class, () -> serieRead.getSerie(serienId));
    }

    @Test
    void getSerieSollNoAccessExceptionWerfenWennDefaultEventNichtSichtbarIst() throws NoAccessException {
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzerId));
        when(serie.getDefaultEvent()).thenReturn(defaultEventId);
        when(eventRead.getEvent(defaultEventId)).thenThrow(NoAccessException.class);
        when(serienRepository.getSerie(serienId)).thenReturn(serie);
        assertThrows(NoAccessException.class, () -> serieRead.getSerie(serienId));
    }

}
