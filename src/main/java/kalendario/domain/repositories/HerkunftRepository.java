package kalendario.domain.repositories;

import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.herkunft.HerkunftId;

public interface HerkunftRepository {
    Herkunft getHerkunftWithId(HerkunftId herkunftId);
}
