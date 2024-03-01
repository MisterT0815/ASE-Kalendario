package kalendario.application.crud.event;

import kalendario.application.crud.herkunft.HerkunftRead;
import kalendario.application.crud.sicherheit.SchreibZugriffVerifizierer;
import kalendario.application.crud.sicherheit.ZugriffVerifizierer;
import kalendario.application.session.KeinZugriffException;
import kalendario.application.session.Session;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.value_objects.Zeitraum;

import java.util.Date;
import java.util.Optional;

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
        schreibZugriffVerifizierer.verifiziereZugriffFuerHerkunft(herkunftId);
        EventId id = eventRepository.neueId();
        Termin termin = new Termin(id, titel, herkunftId, sichtbarkeit, beschreibung, zeitraum);
        eventRepository.saveTermin(termin);
        return termin;
    }

    public Event createEvent(String titel, HerkunftId herkunftId, Sichtbarkeit sichtbarkeit, String beschreibung, Date deadline, boolean getan) throws SaveException, KeinZugriffException {
        schreibZugriffVerifizierer.verifiziereZugriffFuerHerkunft(herkunftId);
        EventId id = eventRepository.neueId();
        Aufgabe aufgabe = new Aufgabe(id, titel, herkunftId, sichtbarkeit, beschreibung, deadline);
        if(getan){
            aufgabe.setGetan(session.getCurrentBenutzer().orElseThrow(), true);
        }
        eventRepository.saveAufgabe(aufgabe);
        return aufgabe;
    }

    public Event createEvent(String titel, HerkunftId herkunftId, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum, boolean getan) throws SaveException, KeinZugriffException {
        schreibZugriffVerifizierer.verifiziereZugriffFuerHerkunft(herkunftId);
        EventId id = eventRepository.neueId();
        GeplanteAufgabe geplanteAufgabe = new GeplanteAufgabe(id, titel, herkunftId, sichtbarkeit, beschreibung, zeitraum);
        if(getan){
            geplanteAufgabe.setGetan(session.getCurrentBenutzer().orElseThrow(), true);
        }
        eventRepository.saveGeplanteAufgabe(geplanteAufgabe);
        return geplanteAufgabe;
    }

}
