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

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public class SchreibZugriffVerifizierer extends ZugriffVerifizierer{

    public SchreibZugriffVerifizierer(Session session, EventRepository eventRepository, SerienRepository serienRepository, HerkunftRepository herkunftRepository) {
        super(session, eventRepository, serienRepository, herkunftRepository);
        BooleanSupplier userIstAngemeldet = () -> session.getCurrentBenutzer().isPresent();
        Predicate<Event> userIstAngemeldetEvent = x -> userIstAngemeldet.getAsBoolean();
        Predicate<Event> userIstBesitzer = this::currentBenutzerIstBesitzerVon;
        eventCheck = userIstAngemeldetEvent.and(userIstBesitzer);
        Predicate<Serie> userIstAngemeltetSerie = x -> userIstAngemeldet.getAsBoolean();
        Predicate<Serie> userHatZugriffAufDefaultEvent = (serie) -> {
            try{
                verifiziereZugriffFuerEvent(serie.getDefaultEvent());
            } catch (KeinZugriffException e) {
                return false;
            }
            return true;
        };
        serieCheck = userIstAngemeltetSerie.and(userHatZugriffAufDefaultEvent);
        Predicate<Herkunft> userIstAngemeltetHerkunft = x -> userIstAngemeldet.getAsBoolean();
        Predicate<Herkunft> userIstBesitzerDerHerkunft = (herkunft) -> herkunft.getBesitzerId().equals(session.getCurrentBenutzer().get());
        herkunftCheck = userIstAngemeltetHerkunft.and(userIstBesitzerDerHerkunft);
    }


}
