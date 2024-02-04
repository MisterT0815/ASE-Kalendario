package kalendario.domain.entities.event;

import kalendario.domain.entities.benutzer.Benutzer;

public interface Sichtbarkeit {
    boolean istSichtbarFuer(Benutzer benutzer);
}
