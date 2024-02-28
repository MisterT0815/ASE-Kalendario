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

public class ZugriffVerfizierer {

    Session session;
    EventRepository eventRepository;
    SerienRepository serienRepository;
    HerkunftRepository herkunftRepository;

    public ZugriffVerfizierer(Session session, EventRepository eventRepository, SerienRepository serienRepository, HerkunftRepository herkunftRepository) {
        this.session = session;
        this.eventRepository = eventRepository;
        this.serienRepository = serienRepository;
        this.herkunftRepository = herkunftRepository;
    }

    public void verifiziereZugriffFuerSerie(Serie serie) throws KeinZugriffException {
        Event defaultEvent = eventRepository.getEvent(serie.getDefaultEvent());
        if(!currentBenutzerIstBesitzerVon(defaultEvent) && !defaultEvent.istSichtbarFuer(getCurrentBenutzerOrThrow())){
            throw new KeinZugriffException();
        }
    }

    public void verifiziereZugriffFuerSerie(SerienId serienId) throws KeinZugriffException{
        Serie serie = serienRepository.getSerie(serienId);
        if(serie == null){
            throw new KeinZugriffException();
        }
        verifiziereZugriffFuerSerie(serie);
    }

    public void verifiziereZugriffFuerEvent(Event event) throws KeinZugriffException{
        if(!currentBenutzerIstBesitzerVon(event) &&  !event.istSichtbarFuer(getCurrentBenutzerOrThrow())){
            throw new KeinZugriffException();
        }
    }

    public void verifiziereZugriffFuerEvent(EventId eventId) throws KeinZugriffException{
        Event event = eventRepository.getEvent(eventId);
        if(event == null){
            throw new KeinZugriffException();
        }
        verifiziereZugriffFuerEvent(event);
    }

    private BenutzerId getCurrentBenutzerOrThrow() throws KeinZugriffException{
        return session.getCurrentBenutzer().orElseThrow(KeinZugriffException::new);
    }

    private boolean currentBenutzerIstBesitzerVon(Event event) throws KeinZugriffException {
        Herkunft herkunft = herkunftRepository.getHerkunftWithId(event.getHerkunftId());
        if(herkunft == null){
            throw new RuntimeException("Herkunft von Event with id %d ist nicht verfuegbar");
        }
        return herkunft.getBesitzerId().equals(getCurrentBenutzerOrThrow());
    }
}
