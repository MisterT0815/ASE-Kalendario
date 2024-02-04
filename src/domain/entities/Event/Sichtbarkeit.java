package domain.entities.Event;

import domain.entities.benutzer.Benutzer;

public interface Sichtbarkeit {
    public boolean istSichtbarFuer(Benutzer benutzer);
}
