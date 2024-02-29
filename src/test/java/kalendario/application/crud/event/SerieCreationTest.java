package kalendario.application.crud.event;

import kalendario.application.crud.benutzer.BenutzerCreation;
import kalendario.application.crud.serie.SerieCreation;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.entities.serie.Wiederholung;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.repositories.SerienRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class SerieCreationTest {

    EventId eventId = mock();
    Date start = mock();
    Wiederholung wiederholung = mock();
    SerienRepository serienRepository = mock();
    SerieCreation serieCreation;

    @BeforeEach
    void init(){
        serieCreation = new SerieCreation(serienRepository);
    }

    @Test
    void createSerieSollSerienRepositoryRufen() throws SaveException {
        Serie serie = serieCreation.createSerie(eventId, start, wiederholung);
        verify(serienRepository).saveSerie(serie);
    }

    @Test
    void createSerieSollExceptionWerfenWennSerienRepositoryFehlerWirft() throws SaveException {
        doThrow(SaveException.class).when(serienRepository).saveSerie(any(Serie.class));
        assertThrows(SaveException.class, () -> serieCreation.createSerie(eventId, start, wiederholung));
    }

    @Test
    void createSerieSollSerieEindeutigeIdVonRepositoryGeben() throws SaveException, BenutzerCreation.BenutzerNameExistiertException {
        SerienId id = mock();
        when(serienRepository.neueId()).thenReturn(id);
        Serie serie = serieCreation.createSerie(eventId, start, wiederholung);
        assertEquals(id, serie.getId());
    }

    @Test
    void createSerieSollAlleUebergebenenEigenschaftenSpeichern() throws SaveException {
        Serie serie = serieCreation.createSerie(eventId, start, wiederholung);
        assertEquals(eventId, serie.getDefaultEvent());
        assertEquals(start, serie.getStart());
        assertEquals(wiederholung, serie.getWiederholung());
    }

}