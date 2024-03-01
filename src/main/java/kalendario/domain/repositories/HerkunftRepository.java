package kalendario.domain.repositories;

import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.herkunft.HerkunftId;

import java.util.List;

public interface HerkunftRepository {
    HerkunftId neueId();
    Herkunft getHerkunftMitId(HerkunftId herkunftId);
    List<Herkunft> getHerkuenfteVonBesitzer(BenutzerId besitzer);
    void saveHerkunft(Herkunft herkunft);
}
