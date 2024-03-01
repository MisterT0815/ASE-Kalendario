package kalendario.application.crud.sicherheit;

import kalendario.application.session.KeinZugriffException;
import kalendario.application.session.Session;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.Event;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.HerkunftRepository;
import kalendario.domain.repositories.SerienRepository;

public class SchreibZugriffVerifizierer extends ZugriffVerifizierer{

    public SchreibZugriffVerifizierer(Session session, EventRepository eventRepository, SerienRepository serienRepository, HerkunftRepository herkunftRepository) {
        super(session, eventRepository, serienRepository, herkunftRepository);
    }

    @Override
    public void verifiziereZugriffFuerSerie(SerienId serienId) throws KeinZugriffException {
        Serie serie = serienRepository.getSerie(serienId);
        nullCheck(serie);
        verifiziereZugriffFuerSerie(serie);
    }

    @Override
    public void verifiziereZugriffFuerSerie(Serie serie) throws KeinZugriffException {
        verifiziereZugriffFuerEvent(serie.getDefaultEvent());
    }

    @Override
    public void verifiziereZugriffFuerEvent(EventId eventId) throws KeinZugriffException {
        Event event = eventRepository.getEvent(eventId);
        nullCheck(event);
        verifiziereZugriffFuerEvent(event);
    }

    @Override
    public void verifiziereZugriffFuerEvent(Event event) throws KeinZugriffException {
        if(!currentBenutzerIstBesitzerVon(event)){
            throw new KeinZugriffException();
        }
    }


}
