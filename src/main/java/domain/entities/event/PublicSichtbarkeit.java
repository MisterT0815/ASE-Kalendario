package main.java.domain.entities.event;

import main.java.domain.entities.benutzer.Benutzer;

public class PublicSichtbarkeit implements Sichtbarkeit {
    
    @Override
    public boolean istSichtbarFuer(Benutzer benutzer) {
        return true;
    }
}
