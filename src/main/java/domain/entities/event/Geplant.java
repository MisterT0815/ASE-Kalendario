package main.java.domain.entities.event;

import main.java.domain.entities.herkunft.Herkunft;
import main.java.domain.entities.serie.SerienId;
import main.java.domain.value_objects.Zeitraum;

public abstract class Geplant extends Event {
    protected Zeitraum zeitraum;

    public Geplant(EventId id, String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum) {
        super(id, titel, herkunft, sichtbarkeit, beschreibung);
        this.zeitraum = zeitraum;
    }

    public <T extends Event> Geplant(EventId id, String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, SerienId serienId, Zeitraum zeitraum) {
        super(id, titel, herkunft, sichtbarkeit, beschreibung, serienId);
        this.zeitraum = zeitraum;
    }
}
