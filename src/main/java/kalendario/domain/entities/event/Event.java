package kalendario.domain.entities.event;


import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.serie.SerienId;

import java.util.Optional;

public abstract class Event {
    protected EventId id;
    protected String titel;
    protected Herkunft herkunft;
    protected Sichtbarkeit sichtbarkeit;
    protected String beschreibung;
    protected SerienId serienId;

    public Event(EventId id, String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung) {
        this.id = id;
        this.titel = titel;
        this.herkunft = herkunft;
        this.sichtbarkeit = sichtbarkeit;
        this.beschreibung = beschreibung;
        serienId = null;
    }

    public Event(EventId id, String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, SerienId serienId) {
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

    public EventId getId() {
        return id;
    }

    public String getTitel() {
        return titel;
    }

    public Herkunft getHerkunft() {
        return herkunft;
    }

    public boolean istSichtbarFuer(BenutzerId benutzer){
        if(getBesitzer().equals(benutzer)){
            return true;
        }else{
            return sichtbarkeit.istSichtbarFuer(benutzer);
        }
    }

    public BenutzerId getBesitzer(){
        return herkunft.getBesitzerId();
    }

    public String getBeschreibung() {
        return beschreibung;
    }
}
