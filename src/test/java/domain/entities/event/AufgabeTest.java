package domain.entities.event;

import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.Aufgabe;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.event.Sichtbarkeit;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.value_objects.Zeitraum;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class AufgabeTest {

    EventId id = mock();
    String titel = "Titel";
    Herkunft herkunft = mock();
    Sichtbarkeit sichtbarkeit = mock();
    String beschreibung = "Beschreibung";
    Date deadline = mock();
    BenutzerId benutzer = mock();


    @Test
    void getanSollteFalschSeinWennNichtGesetzt(){
        Aufgabe aufgabe = new Aufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, deadline);
        assertFalse(aufgabe.istGetan());
        assertTrue(aufgabe.wurdeGemachtVon().isEmpty());
    }

    @Test
    void getanSollteBenutzerZurueckgebenDerGemachtHat(){
        Aufgabe aufgabe = new Aufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, deadline);

        aufgabe.setGetan(benutzer, true);

        assertTrue(aufgabe.istGetan());
        assertTrue(aufgabe.wurdeGemachtVon().isPresent());
        assertEquals(benutzer, aufgabe.wurdeGemachtVon().get());
    }

    @Test
    void getanVonSollteKeinenBenutzerZurueckgebenWennNichtGetanWurde(){
        Aufgabe aufgabe = new Aufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, deadline);

        aufgabe.setGetan(benutzer, true);
        aufgabe.setGetan(benutzer, false);

        assertFalse(aufgabe.istGetan());
        assertTrue(aufgabe.wurdeGemachtVon().isEmpty());
    }
}
