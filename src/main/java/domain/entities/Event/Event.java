package main.java.domain.entities.Event;


import main.java.domain.entities.herkunft.Herkunft;
import main.java.domain.entities.Serie.SerienId;

public abstract class Event {
    protected String titel;
    protected Herkunft herkunft;
    protected Sichtbarkeit sichtbarkeit;
    protected String beschreibung;
    protected SerienId serienId;

    public Event(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung) {
        this.titel = titel;
        this.herkunft = herkunft;
        this.sichtbarkeit = sichtbarkeit;
        this.beschreibung = beschreibung;
        serienId = null;
    }

    public Event(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, SerienId serienId) {
        this.titel = titel;
        this.herkunft = herkunft;
        this.sichtbarkeit = sichtbarkeit;
        this.beschreibung = beschreibung;
        this.serienId = serienId;
    }
}
