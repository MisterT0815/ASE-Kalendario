package main.java.domain.entities.event;

import main.java.domain.entities.herkunft.Herkunft;
import main.java.domain.entities.serie.SerienId;
import main.java.domain.value_objects.Zeitraum;

public class Termin extends Geplant {

    public Termin(EventId id, String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum) {
        super(id, titel, herkunft, sichtbarkeit, beschreibung, zeitraum);
    }

    public <T extends Event> Termin(EventId id, String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, SerienId serienId, Zeitraum zeitraum) {
        super(id, titel, herkunft, sichtbarkeit, beschreibung, serienId, zeitraum);
    }
}
