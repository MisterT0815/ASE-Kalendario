package kalendario.domain.entities.event;

import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.entities.serie.SerienId;

import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

public class Aufgabe extends Event implements Machbar {

    private Date deadline;
    private boolean getan = false;
    private BenutzerId getanVon = null;

    public Aufgabe(EventId id, String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Date deadline) {
        super(id, titel, herkunft, sichtbarkeit, beschreibung);
        this.deadline = deadline;
    }

    public Aufgabe(EventId id, String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, SerienId serienId, Date deadline) {
        super(id, titel, herkunft, sichtbarkeit, beschreibung, serienId);
        this.deadline = deadline;
    }

    @Override
    public boolean istGetan() {
        return getan;
    }

    @Override
    public Optional<BenutzerId> wurdeGemachtVon() {
        return Optional.ofNullable(getanVon);
    }

    @Override
    public void setGetan(BenutzerId von, boolean zu) {
        this.getan = zu;
        if(this.getan) {
            this.getanVon = von;
        }else{
            this.getanVon = null;
        }
    }

    public Date getDeadline() {
        return deadline;
    }

    @Override
    public void pushByDuration(Duration duration) {
        this.deadline = new Date( deadline.getTime() + duration.getSeconds()*1000);
    }

}
