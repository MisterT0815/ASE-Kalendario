package domain.entities.Event;

import domain.entities.benutzer.Benutzer;
import domain.entities.herkunft.Herkunft;
import domain.entities.benutzer.Machbar;
import domain.value_objects.Zeitraum;

public class GeplanteAufgabe extends Geplant implements Machbar {

    private boolean getan;
    private Benutzer getanVon;

    public GeplanteAufgabe(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum) {
        super(titel, herkunft, sichtbarkeit, beschreibung, zeitraum);
    }

    public <T extends Event> GeplanteAufgabe(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, domain.entities.Serie.Serie<T> serie, Zeitraum zeitraum) {
        super(titel, herkunft, sichtbarkeit, beschreibung, serie, zeitraum);
    }

    @Override
    public boolean istGetan() {
        return getan;
    }
}
