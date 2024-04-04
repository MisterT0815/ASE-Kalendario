package kalendario.domain.entities.event;


import java.time.Duration;
import java.util.Date;

import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.value_objects.Zeitraum;

public class Termin extends Event {
    protected Zeitraum zeitraum;

    public Termin(EventId id, String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum) {
        super(id, titel, herkunft, sichtbarkeit, beschreibung);
        this.zeitraum = zeitraum;
    }

    public Termin(EventId id, String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, SerienId serienId, Zeitraum zeitraum) {
        super(id, titel, herkunft, sichtbarkeit, beschreibung, serienId);
        this.zeitraum = zeitraum;
    }

    public Zeitraum getZeitraum() {
        return zeitraum;
    }

    @Override
    public void pushByDuration(Duration duration) {
        zeitraum = zeitraum.pushByDuration(duration);
    }

    @Override
    public String toString() {
        return "Termin{" +
                "zeitraum=" + zeitraum +
                ", id=" + id +
                ", titel='" + titel + '\'' +
                ", herkunft=" + herkunft +
                ", sichtbarkeit=" + sichtbarkeit +
                ", beschreibung='" + beschreibung + '\'' +
                ", serienId=" + serienId +
                '}';
    }
}
