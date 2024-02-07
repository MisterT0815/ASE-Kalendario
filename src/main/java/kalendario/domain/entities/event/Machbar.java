package kalendario.domain.entities.event;

import kalendario.domain.entities.benutzer.BenutzerId;

import java.util.Optional;

public interface Machbar {
    public boolean istGetan();
    public Optional<BenutzerId> wurdeGemachtVon();

    public void setGetan(BenutzerId von, boolean zu);
}
