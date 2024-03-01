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

public abstract class ZugriffVerifizierer {

    Session session;
    EventRepository eventRepository;
    SerienRepository serienRepository;
    HerkunftRepository herkunftRepository;

    public ZugriffVerifizierer(Session session, EventRepository eventRepository, SerienRepository serienRepository, HerkunftRepository herkunftRepository) {
        this.session = session;
        this.eventRepository = eventRepository;
        this.serienRepository = serienRepository;
        this.herkunftRepository = herkunftRepository;
    }

    abstract void verifiziereZugriffFuerSerie(Serie serie) throws KeinZugriffException;
    abstract void verifiziereZugriffFuerSerie(SerienId serienId) throws KeinZugriffException;
    abstract public void verifiziereZugriffFuerEvent(Event event) throws KeinZugriffException;
    abstract public void verifiziereZugriffFuerEvent(EventId eventId) throws KeinZugriffException;

    protected boolean currentBenutzerIstBesitzerVon(Event event) throws KeinZugriffException {
        Herkunft herkunft = herkunftRepository.getHerkunftWithId(event.getHerkunftId());
        if(herkunft == null) {
            throw new KeinZugriffException();
        }
        return herkunft.getBesitzerId().equals(getCurrentBenutzerOrThrow());
    }

    protected BenutzerId getCurrentBenutzerOrThrow() throws KeinZugriffException{
        return session.getCurrentBenutzer().orElseThrow(KeinZugriffException::new);
    }

    protected void nullCheck(Object o) throws KeinZugriffException{
        if(o == null){
            throw new KeinZugriffException();
        }
    }

}
