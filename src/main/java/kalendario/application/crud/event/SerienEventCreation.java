package kalendario.application.crud.event;

import kalendario.application.crud.herkunft.HerkunftRead;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.HerkunftRepository;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.value_objects.Zeitraum;

import java.util.Date;
import java.util.Optional;

public class SerienEventCreation {

    EventRepository eventRepository;
    HerkunftRead herkunftRead;

    public SerienEventCreation(EventRepository eventRepository, HerkunftRead herkunftRead){
        this.eventRepository = eventRepository;
        this.herkunftRead = herkunftRead;
    }
    public Event createEvent(String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum, SerienId serie) throws SaveException {
        EventId id = eventRepository.neueId();
        Termin termin = new Termin(id, titel, herkunft, sichtbarkeit, beschreibung, serie, zeitraum);
        eventRepository.saveTermin(termin);
        return termin;
    }

    public Event createEvent(String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Date deadline, boolean getan, SerienId serie) throws SaveException {
        EventId id = eventRepository.neueId();
        Aufgabe aufgabe = new Aufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, serie, deadline);
        if (getan) {
            setGetanToBesitzer(herkunft, aufgabe);
        }
        eventRepository.saveAufgabe(aufgabe);
        return aufgabe;
    }

    public Event createEvent(String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum, boolean getan, SerienId serie) throws SaveException {
        EventId id = eventRepository.neueId();
        GeplanteAufgabe geplanteAufgabe = new GeplanteAufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, serie, zeitraum);
        if (getan) {
            setGetanToBesitzer(herkunft, geplanteAufgabe);
        }
        eventRepository.saveGeplanteAufgabe(geplanteAufgabe);
        return geplanteAufgabe;
    }

    private void setGetanToBesitzer(HerkunftId herkunftId, Machbar aufgabe) throws SaveException {
        Optional<Herkunft> herkunftOptional = herkunftRead.getHerkunft(herkunftId);
        if(herkunftOptional.isEmpty()){
            throw new SaveException(String.format("Herkunft mit Id %d existiert nicht", herkunftId.getId()));
        }
        aufgabe.setGetan(herkunftOptional.get().getBesitzerId(), true);
    }
}
