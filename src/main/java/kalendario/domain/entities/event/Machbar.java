package kalendario.domain.entities.event;

import kalendario.domain.entities.benutzer.BenutzerId;

import java.util.Optional;

public interface Machbar {
    boolean istGetan();
    Optional<BenutzerId> wurdeGemachtVon();

    void setGetan(BenutzerId von, boolean zu);
}
