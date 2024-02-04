package main.java.domain.entities.event;


import main.java.domain.entities.herkunft.Herkunft;
import main.java.domain.entities.serie.SerienId;

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

}
