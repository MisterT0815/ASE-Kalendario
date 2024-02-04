package main.java.domain.entities.event;

import main.java.domain.entities.benutzer.Benutzer;
import main.java.domain.entities.herkunft.Herkunft;
import main.java.domain.entities.serie.SerienId;

import java.util.Date;

public class Aufgabe extends Event implements Machbar {

    private Date deadline;
    private boolean getan = false;
    private Benutzer getanVon = null;

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
}
