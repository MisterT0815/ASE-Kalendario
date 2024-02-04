package domain.entities.Event;

import domain.entities.benutzer.Benutzer;

public class PublicSichtbarkeit implements Sichtbarkeit {
    
    @Override
    public boolean istSichtbarFuer(Benutzer benutzer) {
        return true;
    }
}
