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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class LeseZugriffVerfizierer extends ZugriffVerifizierer {

    public LeseZugriffVerfizierer(Session session, EventRepository eventRepository, SerienRepository serienRepository, HerkunftRepository herkunftRepository) {
        super(session, eventRepository, serienRepository, herkunftRepository);
        Predicate<Event> userIstAngemeldet = (event) -> session.getCurrentBenutzer().isPresent();
        Predicate<Event> userIstBesitzer = this::currentBenutzerIstBesitzerVon;
        Predicate<Event> userSiehtEvent = (event) -> event.istSichtbarFuer(session.getCurrentBenutzer().get());
        eventCheck = userIstAngemeldet.and(userSiehtEvent.or(userIstBesitzer));
        serieCheck = (serie) -> {
            try{
                verifiziereZugriffFuerEvent(serie.getDefaultEvent());
            } catch (KeinZugriffException e) {
                return false;
            }
            return true;
        };
    }


}
