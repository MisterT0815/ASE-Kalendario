package kalendario.domain.entities.event;

import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.Aufgabe;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.event.Sichtbarkeit;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.herkunft.HerkunftId;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class AufgabeTest {

    EventId id = mock();
    String titel = "Titel";
    HerkunftId herkunft = mock();
    Sichtbarkeit sichtbarkeit = mock();
    String beschreibung = "Beschreibung";
    Date deadline = mock();
    BenutzerId benutzer = mock();


    @Test
    void getanSollFalschSeinWennNichtGesetzt(){
        Aufgabe aufgabe = new Aufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, deadline);
        assertFalse(aufgabe.istGetan());
        assertTrue(aufgabe.wurdeGemachtVon().isEmpty());
    }

    @Test
    void getanSollBenutzerZurueckgebenDerGemachtHat(){
        Aufgabe aufgabe = new Aufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, deadline);

        aufgabe.setGetan(benutzer, true);

        assertTrue(aufgabe.istGetan());
        assertTrue(aufgabe.wurdeGemachtVon().isPresent());
        assertEquals(benutzer, aufgabe.wurdeGemachtVon().get());
    }

    @Test
    void getanVonSollKeinenBenutzerZurueckgebenWennNichtGetanWurde(){
        Aufgabe aufgabe = new Aufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, deadline);

        aufgabe.setGetan(benutzer, true);
        aufgabe.setGetan(benutzer, false);

        assertFalse(aufgabe.istGetan());
        assertTrue(aufgabe.wurdeGemachtVon().isEmpty());
    }

    @Test
    void pushByDurationSollDeadlineVerschieben(){
        Duration duration = Duration.ofMillis(1000L);
        Date date1 = new Date(1000L);
        Date date2 = new Date(2000L);
        Aufgabe aufgabe = new Aufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, date1);
        aufgabe.pushByDuration(duration);
        assertEquals(date2, aufgabe.getDeadline());
    }
}
