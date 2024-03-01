package kalendario.application.crud.event;

import kalendario.application.crud.herkunft.HerkunftRead;
import kalendario.application.crud.sicherheit.SchreibZugriffVerifizierer;
import kalendario.application.session.KeinZugriffException;
import kalendario.application.session.Session;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.HerkunftRepository;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.repositories.SerienRepository;
import kalendario.domain.value_objects.Zeitraum;

import java.util.Date;
import java.util.Optional;

public class SerienEventCreation {

    EventRepository eventRepository;
    SerienRepository serienRepository;
    Session session;
    SchreibZugriffVerifizierer schreibZugriffVerifizierer;

    public SerienEventCreation(EventRepository eventRepository, Session session, SchreibZugriffVerifizierer schreibZugriffVerifizierer) {
        this.eventRepository = eventRepository;
        this.session = session;
        this.schreibZugriffVerifizierer = schreibZugriffVerifizierer;
    }

    public Event createEvent(String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum, SerienId serie) throws SaveException, KeinZugriffException {
        schreibZugriffVerifizierer.verifiziereZugriffFuerHerkunft(herkunft);
        EventId id = eventRepository.neueId();
        Termin termin = new Termin(id, titel, herkunft, sichtbarkeit, beschreibung, serie, zeitraum);
        eventRepository.saveTermin(termin);
        return termin;
    }

    public Event createEvent(String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Date deadline, boolean getan, SerienId serie) throws SaveException, KeinZugriffException {
        schreibZugriffVerifizierer.verifiziereZugriffFuerHerkunft(herkunft);
        EventId id = eventRepository.neueId();
        Aufgabe aufgabe = new Aufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, serie, deadline);
        if (getan) {
            aufgabe.setGetan(session.getCurrentBenutzer().orElseThrow(), true);
        }
        eventRepository.saveAufgabe(aufgabe);
        return aufgabe;
    }

    public Event createEvent(String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum, boolean getan, SerienId serie) throws SaveException, KeinZugriffException {
        schreibZugriffVerifizierer.verifiziereZugriffFuerHerkunft(herkunft);
        EventId id = eventRepository.neueId();
        GeplanteAufgabe geplanteAufgabe = new GeplanteAufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, serie, zeitraum);
        if (getan) {
            geplanteAufgabe.setGetan(session.getCurrentBenutzer().orElseThrow(), true);
        }
        eventRepository.saveGeplanteAufgabe(geplanteAufgabe);
        return geplanteAufgabe;
    }

}
