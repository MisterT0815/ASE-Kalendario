package entities;


import java.util.Optional;

public abstract class Event {
    protected String titel;
    protected Herkunft herkunft;
    protected Sichtbarkeit sichtbarkeit;
    protected String beschreibung;
    private Optional<SerienEventController> Serie;

    public Event(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung) {
        this.titel = titel;
        this.herkunft = herkunft;
        this.sichtbarkeit = sichtbarkeit;
        this.beschreibung = beschreibung;
        Serie = Optional.empty();
    }

    public <T extends Event> Event(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, SerienEventController<T> serie) {
        this.titel = titel;
        this.herkunft = herkunft;
        this.sichtbarkeit = sichtbarkeit;
        this.beschreibung = beschreibung;
        Serie = Optional.of(serie);
    }
}
