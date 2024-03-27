package kalendario.application.wiederholung;

import kalendario.domain.entities.serie.Wiederholung;
import kalendario.domain.value_objects.Zeitraum;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class ZeitlicherAbstandWiederholungTest {

    final int YEAR = 2020;
    final int MONTH = 1;
    final int DAY = 2;
    final int HOUR = 2;
    final int MINUTE = 4;
    final int SECOND = 6;
    Calendar calendar;
    Date start;
    Duration abstand;
    Date vierTageSpaeter;
    Date fuenfTageSpeater;
    Date vierEinhalbTageSpaeter;
    Wiederholung wiederholung;


    @BeforeEach
    void init(){
        abstand = Duration.ofDays(1);
        calendar = new Calendar.Builder().setDate(YEAR, MONTH, DAY).setTimeOfDay(HOUR,MINUTE,SECOND).build();
        start = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 4);
        vierTageSpaeter = calendar.getTime();
        calendar.add(Calendar.HOUR, 12);
        vierEinhalbTageSpaeter = calendar.getTime();
        calendar.add(Calendar.HOUR, 12);
        fuenfTageSpeater = calendar.getTime();

        wiederholung = new ZeitlicherAbstandWiederholung(abstand, start);
    }


    @Test
    void naechsterZeitpunktAb(){
        assertEquals(fuenfTageSpeater, wiederholung.naechsterZeitpunktAb(vierEinhalbTageSpaeter));
        assertEquals(fuenfTageSpeater, wiederholung.naechsterZeitpunktAb(vierTageSpaeter));
    }

    @Test
    void istZeitpunktInWiederholung(){
        assertTrue(wiederholung.istZeitpunktInWiederholung(fuenfTageSpeater));
        assertFalse(wiederholung.istZeitpunktInWiederholung(vierEinhalbTageSpaeter));
        assertTrue(wiederholung.istZeitpunktInWiederholung(start));
        assertTrue(wiederholung.istZeitpunktInWiederholung(vierTageSpaeter));
    }

    @Test
    void alleZeitpunkteInZeitraumSollInklusiveStartExklusiveEndeArbeiten(){
        Zeitraum zeitraum = new Zeitraum(start, fuenfTageSpeater);
        calendar = new Calendar.Builder().setDate(YEAR, MONTH, DAY).setTimeOfDay(HOUR,MINUTE,SECOND).build();
        List<Date> gewollteZeitpunkte = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            gewollteZeitpunkte.add(calendar.getTime());
            calendar.add(Calendar.HOUR, 24);
        }
        assertEquals(gewollteZeitpunkte, wiederholung.alleZeitpunkteInZeitraum(zeitraum));
        assertTrue(gewollteZeitpunkte.contains(start));
        assertFalse(gewollteZeitpunkte.contains(fuenfTageSpeater));
    }





}
