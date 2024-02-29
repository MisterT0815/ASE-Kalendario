package kalendario.application.crud.serie;

import kalendario.application.crud.sicherheit.SchreibZugriffVerifizierer;
import kalendario.application.session.KeinZugriffException;
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
    SchreibZugriffVerifizierer schreibZugriffVerifizierer;

    public SerieCreation(SerienRepository serienRepository, SchreibZugriffVerifizierer schreibZugriffVerifizierer){
        this.serienRepository = serienRepository;
        this.schreibZugriffVerifizierer = schreibZugriffVerifizierer;
    }

    public Serie createSerie(EventId defaultEvent, Date start, Wiederholung wiederholung) throws SaveException {
        SerienId id = serienRepository.neueId();
        Serie serie = new Serie(id, defaultEvent, start, wiederholung);
        try {
            schreibZugriffVerifizierer.verifiziereZugriffFuerSerie(serie);
        } catch (KeinZugriffException e) {
            throw new SaveException("Kein Zugriff auf Default Event");
        }
        serienRepository.saveSerie(serie);
        return serie;
    }

}
