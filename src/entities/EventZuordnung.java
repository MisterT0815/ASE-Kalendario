package entities;
public class EventZuordnung {
    private Benutzer benutzer;
    private Event event;

    public EventZuordnung(Benutzer benutzer, Event event) {
        this.benutzer = benutzer;
        this.event = event;
    }
}