package domain.entities.event;

import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.event.GeplanteAufgabe;
import kalendario.domain.entities.event.Sichtbarkeit;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.value_objects.Zeitraum;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class GeplanteAufgabeTest {

    EventId id = mock();
    String titel = "Titel";
    Herkunft herkunft = mock();
    Sichtbarkeit sichtbarkeit = mock();
    String beschreibung = "Beschreibung";
    Zeitraum zeitraum = mock();
    BenutzerId benutzer = mock();


    @Test
    void getanSollteFalschSeinWennNichtGesetzt(){
        GeplanteAufgabe geplanteAufgabe = new GeplanteAufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, zeitraum);
        assertFalse(geplanteAufgabe.istGetan());
        assertTrue(geplanteAufgabe.wurdeGemachtVon().isEmpty());
    }

    @Test
    void getanSollteBenutzerZurueckgebenDerGemachtHat(){
        GeplanteAufgabe geplanteAufgabe = new GeplanteAufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, zeitraum);

        geplanteAufgabe.setGetan(benutzer, true);

        assertTrue(geplanteAufgabe.istGetan());
        assertTrue(geplanteAufgabe.wurdeGemachtVon().isPresent());
        assertEquals(benutzer, geplanteAufgabe.wurdeGemachtVon().get());
    }

    @Test
    void getanVonSollteKeinenBenutzerZurueckgebenWennNichtGetanWurde(){
        GeplanteAufgabe geplanteAufgabe = new GeplanteAufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, zeitraum);

        geplanteAufgabe.setGetan(benutzer, true);
        geplanteAufgabe.setGetan(benutzer, false);

        assertFalse(geplanteAufgabe.istGetan());
        assertTrue(geplanteAufgabe.wurdeGemachtVon().isEmpty());
    }

}
