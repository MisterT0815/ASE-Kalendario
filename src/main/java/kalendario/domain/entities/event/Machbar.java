package kalendario.domain.entities.event;

import kalendario.domain.entities.benutzer.Benutzer;

import java.util.Optional;

public interface Machbar {
    public boolean istGetan();
    public Optional<Benutzer> wurdeGemachtVon();

    public void setGetan(Benutzer von, boolean zu);
}
