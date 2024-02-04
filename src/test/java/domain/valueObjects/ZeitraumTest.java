package domain.valueObjects;

import kalendario.domain.value_objects.Zeitraum;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZeitraumTest {


    private final Date ersterZeitpunkt = new Date(1000L);
    private final Date zweiterZeitpunkt = new Date(2000L);

    private final Date dritterZeitpunkt = new Date(3000L);

    @Test
    void validerZeitraumSollteErstellbarSein() throws IllegalAccessException {
        Zeitraum zeitraum = new Zeitraum(ersterZeitpunkt, zweiterZeitpunkt);
        assertEquals(zeitraum.getStart(), ersterZeitpunkt);
        assertEquals(zeitraum.getEnde(), zweiterZeitpunkt);
    }


}
