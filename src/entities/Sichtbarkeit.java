package entities;

import entities.Benutzer;

public interface Sichtbarkeit {
    public boolean istSichtbarFuer(Benutzer benutzer);
}
