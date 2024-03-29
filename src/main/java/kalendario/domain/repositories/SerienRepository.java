package kalendario.domain.repositories;

import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;

import java.util.Date;

public interface SerienRepository {

    SerienId neueId();
    void saveSerie(Serie serie) throws SaveException;
    void addAngepasstesEvent(Date when, EventId event) throws SaveException;
    void removeAngepasstesEvent(Date when) throws SaveException;
    Serie getSerie(SerienId serienId);

}
