package kalendario.domain.entities.event;


import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.value_objects.Zeitraum;

public class Termin extends Geplant {

    public Termin(EventId id, String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum) {
        super(id, titel, herkunft, sichtbarkeit, beschreibung, zeitraum);
    }

    public <T extends Event> Termin(EventId id, String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, SerienId serienId, Zeitraum zeitraum) {
        super(id, titel, herkunft, sichtbarkeit, beschreibung, serienId, zeitraum);
    }
}
