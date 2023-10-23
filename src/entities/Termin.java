package entities;

import value_objects.Zeitraum;

import java.util.Date;

public class Termin extends Geplant{

    public Termin(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum) {
        super(titel, herkunft, sichtbarkeit, beschreibung, zeitraum);
    }

    public <T extends Event> Termin(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, SerienEventController<T> serie, Zeitraum zeitraum) {
        super(titel, herkunft, sichtbarkeit, beschreibung, serie, zeitraum);
    }
}
