package kalendario.application.crud.serie;

import kalendario.domain.entities.event.Event;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.entities.serie.Wiederholung;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.repositories.SerienRepository;

import java.util.Date;

public class SerieCreation {

    SerienRepository serienRepository;

    public SerieCreation(SerienRepository serienRepository){
        this.serienRepository = serienRepository;
    }

    public Serie createSerie(EventId defaultEvent, Date start, Wiederholung wiederholung) throws SaveException {
        SerienId id = serienRepository.neueId();
        Serie serie = new Serie(id, defaultEvent, start, wiederholung);
        serienRepository.saveSerie(serie);
        return serie;
    }

}
