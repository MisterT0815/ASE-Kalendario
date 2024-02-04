package main.java.domain.entities.Event;

import main.java.domain.entities.herkunft.Herkunft;
import main.java.domain.value_objects.Zeitraum;

public abstract class Geplant extends Event {
    protected Zeitraum zeitraum;

    public Geplant(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum) {
        super(titel, herkunft, sichtbarkeit, beschreibung);
        this.zeitraum = zeitraum;
    }

    public <T extends Event> Geplant(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, main.java.domain.entities.Serie.Serie<T> serie, Zeitraum zeitraum) {
        super(titel, herkunft, sichtbarkeit, beschreibung, serie);
        this.zeitraum = zeitraum;
    }
}
