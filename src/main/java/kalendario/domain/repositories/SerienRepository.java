package kalendario.domain.repositories;

import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;

public interface SerienRepository {

    SerienId neueId();
    void saveSerie(Serie serie) throws SaveException;

    Serie getSerie(SerienId serienId);
}
