package main.java.domain.entities.Event;

import main.java.domain.entities.herkunft.Herkunft;
import main.java.domain.value_objects.Zeitraum;

public class Termin extends Geplant {

    public Termin(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum) {
        super(titel, herkunft, sichtbarkeit, beschreibung, zeitraum);
    }

    public <T extends Event> Termin(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, main.java.domain.entities.Serie.Serie<T> serie, Zeitraum zeitraum) {
        super(titel, herkunft, sichtbarkeit, beschreibung, serie, zeitraum);
    }
}
