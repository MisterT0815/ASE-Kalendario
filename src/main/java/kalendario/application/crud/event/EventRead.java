package kalendario.application.crud.event;

import kalendario.application.crud.sicherheit.ZugriffVerfizierer;
import kalendario.application.session.KeinZugriffException;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;

import java.util.List;
import java.util.Optional;

public class EventRead {

    EventRepository eventRepository;
    ZugriffVerfizierer zugriffVerfizierer;

    public EventRead(EventRepository eventRepository, ZugriffVerfizierer zugriffVerfizierer) {
        this.eventRepository = eventRepository;
        this.zugriffVerfizierer = zugriffVerfizierer;
    }

    public Optional<Event> getEvent(EventId eventId) throws KeinZugriffException {
        Event event = eventRepository.getEvent(eventId);
        if(event == null){
            return Optional.empty();
        }
        zugriffVerfizierer.verifiziereZugriffFuerEvent(event);
        return Optional.of(event);
    }


    public Optional<Aufgabe> getAufgabe(EventId eventId) throws KeinZugriffException {
        Optional<Event> event = this.getEvent(eventId);
        try{
            return (Optional.of((Aufgabe) event.orElseThrow()));
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public Optional<GeplanteAufgabe> getGeplanteAufgabe(EventId eventId) throws KeinZugriffException {
        Optional<Event> event = this.getEvent(eventId);
        try{
            return (Optional.of((GeplanteAufgabe) event.orElseThrow()));
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public Optional<Termin> getTermin(EventId eventId) throws KeinZugriffException {
        Optional<Event> event = this.getEvent(eventId);
        try{
            return (Optional.of((Termin) event.orElseThrow()));
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public List<Event> getEventsOfSerie(SerienId serienId) throws KeinZugriffException {
        zugriffVerfizierer.verifiziereZugriffFuerSerie(serienId);
        List<Event> events = eventRepository.getEventsOfSerie(serienId);
        return events.stream().filter(event -> {
            try {
                zugriffVerfizierer.verifiziereZugriffFuerEvent(event);
            } catch (KeinZugriffException e) {
                return false;
            }
            return true;
        }).toList();
    }

}
