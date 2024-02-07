package kalendario.domain.entities.event;


import kalendario.domain.entities.benutzer.Benutzer;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.value_objects.Zeitraum;

import java.util.Optional;

public class GeplanteAufgabe extends Termin implements Machbar {

    private boolean getan = false;
    private Benutzer getanVon;

    public GeplanteAufgabe(EventId id, String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum) {
        super(id, titel, herkunft, sichtbarkeit, beschreibung, zeitraum);
    }

    public GeplanteAufgabe(EventId id, String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, SerienId serienId, Zeitraum zeitraum) {
        super(id, titel, herkunft, sichtbarkeit, beschreibung, serienId, zeitraum);
    }

    @Override
    public boolean istGetan() {
        return this.getan;
    }

    @Override
    public Optional<Benutzer> wurdeGemachtVon() {
        return Optional.ofNullable(this.getanVon);
    }

    @Override
    public void setGetan(Benutzer von, boolean zu) {
        this.getan = zu;
        if(this.getan) {
            this.getanVon = von;
        }else{
            this.getanVon = null;
        }
    }


}
