package kalendario.domain.entities.event;

import kalendario.domain.entities.benutzer.Benutzer;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.serie.SerienId;

import java.util.Date;
import java.util.Optional;

public class Aufgabe extends Event implements Machbar {

    private Date deadline;
    private boolean getan = false;
    private BenutzerId getanVon = null;

    public Aufgabe(EventId id, String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Date deadline) {
        super(id, titel, herkunft, sichtbarkeit, beschreibung);
        this.deadline = deadline;
    }

    public Aufgabe(EventId id, String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, SerienId serienId, Date deadline) {
        super(id, titel, herkunft, sichtbarkeit, beschreibung, serienId);
        this.deadline = deadline;
    }

    @Override
    public boolean istGetan() {
        return getan;
    }

    @Override
    public Optional<Benutzer> wurdeGemachtVon() {
        return Optional.empty();
    }

    @Override
    public void setGetan(Benutzer von, boolean zu) {

    }
}
