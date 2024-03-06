package kalendario.application.crud.serie;

import kalendario.application.crud.event.EventRead;
import kalendario.application.crud.sicherheit.SchreibZugriffVerifizierer;
import kalendario.application.session.KeinZugriffException;
import kalendario.domain.entities.event.Event;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.entities.serie.Wiederholung;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.repositories.SerienRepository;

import java.util.Date;

public class SerieCreation {

    SerienRepository serienRepository;
    EventRepository eventRepository;
    SchreibZugriffVerifizierer schreibZugriffVerifizierer;

    public SerieCreation(SerienRepository serienRepository, EventRepository eventRepository, SchreibZugriffVerifizierer schreibZugriffVerifizierer){
        this.serienRepository = serienRepository;
        this.eventRepository = eventRepository;
        this.schreibZugriffVerifizierer = schreibZugriffVerifizierer;
    }

    public Serie createSerie(EventId defaultEventId, Date start, Wiederholung wiederholung) throws SaveException, KeinZugriffException {
        SerienId id = serienRepository.neueId();
        Serie serie = new Serie(id, defaultEventId, start, wiederholung);
        schreibZugriffVerifizierer.verifiziereZugriffFuerSerie(serie);
        eventRepository.setSerie(defaultEventId, id);
        try {
            serienRepository.saveSerie(serie);
        }catch(SaveException e){
            eventRepository.setSerie(defaultEventId, null);
            throw e;
        }
        return serie;
    }

}
