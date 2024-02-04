package domain.entities.Event;

import domain.entities.benutzer.Benutzer;
import domain.entities.herkunft.Herkunft;
import domain.entities.benutzer.Machbar;

import java.util.Date;

public class Aufgabe extends Event implements Machbar {

    private Date deadline;
    private boolean getan = false;
    private Benutzer getanVon = null;

    public Aufgabe(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Date deadline) {
        super(titel, herkunft, sichtbarkeit, beschreibung);
        this.deadline = deadline;
    }

    public Aufgabe(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, domain.entities.Serie.Serie<Aufgabe> serie, Date deadline) {
        super(titel, herkunft, sichtbarkeit, beschreibung, serie);
        this.deadline = deadline;
    }

    @Override
    public boolean istGetan() {
        return getan;
    }
}
