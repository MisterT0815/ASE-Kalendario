package kalendario.domain.entities.event;


import kalendario.domain.entities.benutzer.Benutzer;

public class PublicSichtbarkeit implements Sichtbarkeit {
    
    @Override
    public boolean istSichtbarFuer(Benutzer benutzer) {
        return true;
    }
}
