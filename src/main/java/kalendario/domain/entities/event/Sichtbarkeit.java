package kalendario.domain.entities.event;

import kalendario.domain.entities.benutzer.BenutzerId;

public interface Sichtbarkeit {
    boolean istSichtbarFuer(BenutzerId benutzer);
}
