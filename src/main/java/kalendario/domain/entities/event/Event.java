package kalendario.domain.entities.event;


import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.entities.serie.SerienId;

import java.time.Duration;
import java.util.Optional;

public abstract class Event implements Verschiebbar{
    protected EventId id;
    protected String titel;
    protected HerkunftId herkunft;
    protected Sichtbarkeit sichtbarkeit;
    protected String beschreibung;
    protected SerienId serienId;

    public Event(EventId id, String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung) {
        this.id = id;
        this.titel = titel;
        this.herkunft = herkunft;
        this.sichtbarkeit = sichtbarkeit;
        this.beschreibung = beschreibung;
        serienId = null;
    }

    public Event(EventId id, String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, SerienId serienId) {
        this.id = id;
        this.titel = titel;
        this.herkunft = herkunft;
        this.sichtbarkeit = sichtbarkeit;
        this.beschreibung = beschreibung;
        this.serienId = serienId;
    }

    public Optional<SerienId> getSerienId(){
        return Optional.ofNullable(this.serienId);
    }
    public boolean istSichtbarFuer(BenutzerId benutzer){
        return sichtbarkeit.istSichtbarFuer(benutzer);
    }

    public EventId getId() {
        return id;
    }

    public String getTitel() {
        return titel;
    }

    public HerkunftId getHerkunftId() {
        return herkunft;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public Sichtbarkeit getSichtbarkeit(){return sichtbarkeit;}


}
