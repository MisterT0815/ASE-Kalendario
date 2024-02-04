package domain.valueObjects;

import kalendario.domain.value_objects.Zeitraum;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
class ZeitraumTest {


    private final Date ersterZeitpunkt = new Date(1000L);
    private final Date zweiterZeitpunkt = new Date(2000L);

    private final Date dritterZeitpunkt = new Date(3000L);
    private final Date vierterZeitpunkt = new Date(4000L);

    @Test
    void validerZeitraumSollteErstellbarSein() throws IllegalAccessException {
        Zeitraum zeitraum = new Zeitraum(ersterZeitpunkt, zweiterZeitpunkt);
        assertEquals(zeitraum.getStart(), ersterZeitpunkt);
        assertEquals(zeitraum.getEnde(), zweiterZeitpunkt);
    }

    @Test
    void invaliderZeitraumSollteExceptionWerfen(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Zeitraum(zweiterZeitpunkt, ersterZeitpunkt);
        });
    }

    @Test
    void enthaeltSollteTrueZurueckgebenWennUebergebenerZeitpunktStrictlyInZeitraum(){
        Zeitraum zeitraum = new Zeitraum(ersterZeitpunkt, dritterZeitpunkt);
        assertTrue(zeitraum.enthaelt(zweiterZeitpunkt));
    }

    @Test
    void enthealtSollteFalseZurueckgebenWennUebergebenerZeitpunktNichtStrictlyInZeitraum(){
        Zeitraum zeitraum = new Zeitraum(ersterZeitpunkt, dritterZeitpunkt);
        assertFalse(zeitraum.enthaelt(ersterZeitpunkt));
        assertFalse(zeitraum.enthaelt(dritterZeitpunkt));
        assertFalse(zeitraum.enthaelt(vierterZeitpunkt));
    }

    @Test
    void equalsSollteTrueZurueggebenWennZeitrauemeSelbenStartUndEndeHaben(){
        Zeitraum zeitraum1 = new Zeitraum(ersterZeitpunkt, dritterZeitpunkt);
        Zeitraum zeitraum2 = new Zeitraum(new Date(ersterZeitpunkt.getTime()), new Date(dritterZeitpunkt.getTime()));
        assertEquals(zeitraum1, zeitraum2);
    }


}
