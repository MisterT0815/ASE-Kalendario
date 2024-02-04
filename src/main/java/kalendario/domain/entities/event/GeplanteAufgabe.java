package kalendario.domain.entities.event;


import kalendario.domain.entities.benutzer.Benutzer;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.value_objects.Zeitraum;

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
