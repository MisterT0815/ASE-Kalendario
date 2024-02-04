package main.java.domain.entities.event;

import main.java.domain.entities.benutzer.Benutzer;

public interface Sichtbarkeit {
    public boolean istSichtbarFuer(Benutzer benutzer);
}
