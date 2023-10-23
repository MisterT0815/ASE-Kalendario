package entities;

import value_objects.Zeitraum;

import java.util.Date;

public class GeplanteAufgabe extends Geplant implements Machbar{

    private boolean getan;
    private Benutzer getanVon;

    public GeplanteAufgabe(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum) {
        super(titel, herkunft, sichtbarkeit, beschreibung, zeitraum);
    }

    public <T extends Event> GeplanteAufgabe(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, SerienEventController<T> serie, Zeitraum zeitraum) {
        super(titel, herkunft, sichtbarkeit, beschreibung, serie, zeitraum);
    }

    @Override
    public boolean istGetan() {
        return getan;
    }
}
