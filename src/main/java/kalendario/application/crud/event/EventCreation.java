package kalendario.application.crud.event;

import kalendario.application.crud.sicherheit.ExistiertNichtException;
import kalendario.application.crud.sicherheit.SchreibZugriffVerifizierer;
import kalendario.application.session.KeinZugriffException;
import kalendario.application.session.Session;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.value_objects.Zeitraum;

import java.util.Date;

public class EventCreation {

    EventRepository eventRepository;
    Session session;
    SchreibZugriffVerifizierer schreibZugriffVerifizierer;

    public EventCreation(EventRepository eventRepository, Session session, SchreibZugriffVerifizierer schreibZugriffVerifizierer) {
        this.eventRepository = eventRepository;
        this.session = session;
        this.schreibZugriffVerifizierer = schreibZugriffVerifizierer;
    }

    public Event createEvent(String titel, HerkunftId herkunftId, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum) throws SaveException, KeinZugriffException {
        verifiziereZugriffe(herkunftId);
        EventId id = eventRepository.neueId();
        Termin termin = new Termin(id, titel, herkunftId, sichtbarkeit, beschreibung, zeitraum);
        eventRepository.saveTermin(termin);
        return termin;
    }

    public Event createEvent(String titel, HerkunftId herkunftId, Sichtbarkeit sichtbarkeit, String beschreibung, Date deadline, boolean getan) throws SaveException, KeinZugriffException {
        verifiziereZugriffe(herkunftId);
        EventId id = eventRepository.neueId();
        Aufgabe aufgabe = new Aufgabe(id, titel, herkunftId, sichtbarkeit, beschreibung, deadline);
        aufgabe.setGetan(session.getCurrentBenutzer().orElseThrow(), getan);
        eventRepository.saveAufgabe(aufgabe);
        return aufgabe;
    }

    public Event createEvent(String titel, HerkunftId herkunftId, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum, boolean getan) throws SaveException, KeinZugriffException {
        verifiziereZugriffe(herkunftId);
        EventId id = eventRepository.neueId();
        GeplanteAufgabe geplanteAufgabe = new GeplanteAufgabe(id, titel, herkunftId, sichtbarkeit, beschreibung, zeitraum);
        geplanteAufgabe.setGetan(session.getCurrentBenutzer().orElseThrow(), getan);
        eventRepository.saveGeplanteAufgabe(geplanteAufgabe);
        return geplanteAufgabe;
    }

    private void verifiziereZugriffe(HerkunftId herkunftId) throws KeinZugriffException, SaveException {
        try{
            schreibZugriffVerifizierer.verifiziereZugriffFuerHerkunft(herkunftId);
        } catch(ExistiertNichtException e){
            throw new SaveException(String.format("Herkunft mit id %d existiert nicht", herkunftId.getId()), e);
        }
    }


}
