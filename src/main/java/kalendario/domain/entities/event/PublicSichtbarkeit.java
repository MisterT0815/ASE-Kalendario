package kalendario.domain.entities.event;

import kalendario.domain.entities.benutzer.BenutzerId;

public class PublicSichtbarkeit implements Sichtbarkeit {
    
    @Override
    public boolean istSichtbarFuer(BenutzerId benutzer) {
        return true;
    }
}
