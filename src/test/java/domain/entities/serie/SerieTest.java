package domain.entities.serie;

import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.entities.serie.Wiederholung;
import kalendario.domain.value_objects.Zeitraum;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class SerieTest {

    SerienId serienId = mock();
    EventId defaultEventId = mock();
    Date start = mock();
    Wiederholung wiederholung = mock();
    Serie serie;

    @BeforeEach
    void init(){
        serie = new Serie(serienId, defaultEventId, start, wiederholung);
    }

    @Test
    void changeEventAnZeitpunktSollIllegalArgumentExceptionWerfenWennZeitpunktNichtInSerie(){
        EventId eventId = mock();
        Date date = mock();
        when(wiederholung.istZeitpunktInWiederholung(date)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> serie.changeEventAnZeitpunkt(date, eventId));
    }

    @Test
    void getEventAnZeitpunktSollIllegalArgumentExceptionWerfenWennZeitpunktNichtInSerie(){
        Date date = mock();
        when(wiederholung.istZeitpunktInWiederholung(date)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> serie.getEventAnZeitpunkt(date));
    }

    @Test
    void getEventAnZeitpunktSollDefaultEventZurueckgebenWennSerieAnZeitpunktNichtVeraendertWurde(){
        when(wiederholung.istZeitpunktInWiederholung(start)).thenReturn(true);
        assertEquals(defaultEventId, serie.getEventAnZeitpunkt(start));
    }

    @Test
    void changeEventAnZeitpunktSollDefaultEventAnDemZeitpunktErsetzen(){
        EventId changedEventId = mock();
        Date date = mock();
        when(wiederholung.istZeitpunktInWiederholung(date)).thenReturn(true);
        serie.changeEventAnZeitpunkt(date, changedEventId);
        assertEquals(changedEventId, serie.getEventAnZeitpunkt(date));
    }

    @Test
    void changeDefaultEventSollDefaultEventErsetzen(){
        EventId changedEvent = mock();
        when(wiederholung.istZeitpunktInWiederholung(start)).thenReturn(true);
        serie.changeDefaultEvent(changedEvent);
        assertEquals(changedEvent, serie.getEventAnZeitpunkt(start));
    }

    @Test
    void getEventsInZeitraumSollFuerJedenZeitpunktInWiederholungEinEventZurueckgeben(){
        Zeitraum zeitraum = mock();
        List<Date> dates = mock();
        Date date1 = mock();
        Date date2 = mock();
        Iterator<Date> dateIterator = mock();
        when(dates.iterator()).thenReturn(dateIterator);
        when(dateIterator.hasNext()).thenReturn(true, true, false);
        when(dateIterator.next()).thenReturn(date1, date2);
        when(wiederholung.alleZeitpunkteInZeitraum(zeitraum)).thenReturn(dates);
        when(wiederholung.istZeitpunktInWiederholung(date1)).thenReturn(true);
        when(wiederholung.istZeitpunktInWiederholung(date2)).thenReturn(true);
        assertEquals(2, serie.getEventsInZeitraum(zeitraum).size());
    }

}
