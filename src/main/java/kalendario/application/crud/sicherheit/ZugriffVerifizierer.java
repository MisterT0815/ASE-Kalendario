package kalendario.application.crud.sicherheit;

import kalendario.application.session.KeinZugriffException;
import kalendario.application.session.Session;
import kalendario.domain.entities.event.Event;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.HerkunftRepository;
import kalendario.domain.repositories.SerienRepository;

import java.util.function.Predicate;

public abstract class ZugriffVerifizierer {

    Session session;
    EventRepository eventRepository;
    SerienRepository serienRepository;
    HerkunftRepository herkunftRepository;
    Predicate eventCheck = x -> false;
    Predicate<Serie> serieCheck = x -> false;
    Predicate<Herkunft> herkunftCheck = x -> false;

    public ZugriffVerifizierer(Session session, EventRepository eventRepository, SerienRepository serienRepository, HerkunftRepository herkunftRepository) {
        this.session = session;
        this.eventRepository = eventRepository;
        this.serienRepository = serienRepository;
        this.herkunftRepository = herkunftRepository;
    }

    public void verifiziereZugriffFuerSerie(SerienId serienId) throws KeinZugriffException{
        Serie serie = serienRepository.getSerie(serienId);
        nullCheck(serie);
        verifiziereZugriffFuerSerie(serie);
    }

    public void verifiziereZugriffFuerSerie(Serie serie) throws KeinZugriffException{
        if(!serieCheck.test(serie)){
            throw new KeinZugriffException();
        }
    }

    public void verifiziereZugriffFuerEvent(EventId eventId) throws KeinZugriffException{
        Event event = eventRepository.getEvent(eventId);
        nullCheck(event);
        verifiziereZugriffFuerEvent(event);
    }

    public void verifiziereZugriffFuerEvent(Event event) throws KeinZugriffException{
        if(!eventCheck.test(event)){
            throw new KeinZugriffException();
        }
    }

    public void verifiziereZugriffFuerHerkunft(HerkunftId herkunftId) throws KeinZugriffException{
        Herkunft herkunft = herkunftRepository.getHerkunftWithId(herkunftId);
        nullCheck(herkunftId);
        verifiziereZugriffFuerHerkunft(herkunft);
    }

    public void verifiziereZugriffFuerHerkunft(Herkunft herkunft) throws KeinZugriffException{
        if(!herkunftCheck.test(herkunft)){
            throw new KeinZugriffException();
        }
    }

    protected boolean currentBenutzerIstBesitzerVon(Event event) {
        Herkunft herkunft = herkunftRepository.getHerkunftWithId(event.getHerkunftId());
        try{
            nullCheck(herkunft);
            return herkunft.getBesitzerId().equals(session.getCurrentBenutzer().orElseThrow());
        } catch (KeinZugriffException e) {
            return false;
        }
    }

    protected void nullCheck(Object o) throws KeinZugriffException{
        if(o == null){
            throw new KeinZugriffException();
        }
    }

}
