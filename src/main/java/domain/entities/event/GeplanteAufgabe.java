package main.java.domain.entities.event;

import main.java.domain.entities.benutzer.Benutzer;
import main.java.domain.entities.herkunft.Herkunft;
import main.java.domain.entities.serie.SerienId;
import main.java.domain.value_objects.Zeitraum;

public class GeplanteAufgabe extends Geplant implements Machbar {

    private boolean getan;
    private Benutzer getanVon;

    public GeplanteAufgabe(EventId id, String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum) {
        super(id, titel, herkunft, sichtbarkeit, beschreibung, zeitraum);
    }

    public <T extends Event> GeplanteAufgabe(EventId id, String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, SerienId serienId, Zeitraum zeitraum) {
        super(id, titel, herkunft, sichtbarkeit, beschreibung, serienId, zeitraum);
    }

    @Override
    public boolean istGetan() {
        return getan;
    }
}
