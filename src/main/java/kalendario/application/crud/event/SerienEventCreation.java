package kalendario.application.crud.event;

import kalendario.domain.entities.event.*;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.value_objects.Zeitraum;

import java.util.Date;

public class SerienEventCreation {

    EventRepository eventRepository;

    public SerienEventCreation(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }
    public Event createEvent(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum, SerienId serie) throws SaveException {
        EventId id = eventRepository.neueId();
        Termin termin = new Termin(id, titel, herkunft, sichtbarkeit, beschreibung, serie, zeitraum);
        eventRepository.saveTermin(termin);
        return termin;
    }

    public Event createEvent(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Date deadline, boolean getan, SerienId serie) throws SaveException {
        EventId id = eventRepository.neueId();
        Aufgabe aufgabe = new Aufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, serie, deadline);
        if (getan) {
            aufgabe.setGetan(herkunft.getBesitzerId(), true);
        }
        eventRepository.saveAufgabe(aufgabe);
        return aufgabe;
    }

    public Event createEvent(String titel, Herkunft herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum, boolean getan, SerienId serie) throws SaveException {
        EventId id = eventRepository.neueId();
        GeplanteAufgabe geplanteAufgabe = new GeplanteAufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, serie, zeitraum);
        if (getan) {
            geplanteAufgabe.setGetan(herkunft.getBesitzerId(), true);
        }
        eventRepository.saveGeplanteAufgabe(geplanteAufgabe);
        return geplanteAufgabe;
    }
}
