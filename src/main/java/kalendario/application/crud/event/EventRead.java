package kalendario.application.crud.event;

import kalendario.application.crud.sicherheit.ExistiertNichtException;
import kalendario.application.crud.sicherheit.LeseZugriffVerfizierer;
import kalendario.application.session.KeinZugriffException;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;

import java.util.List;
import java.util.Optional;

public class EventRead {

    EventRepository eventRepository;
    LeseZugriffVerfizierer leseZugriffVerfizierer;

    public EventRead(EventRepository eventRepository, LeseZugriffVerfizierer leseZugriffVerfizierer) {
        this.eventRepository = eventRepository;
        this.leseZugriffVerfizierer = leseZugriffVerfizierer;
    }

    public Optional<Event> getEvent(EventId eventId) throws KeinZugriffException {
        try {
            leseZugriffVerfizierer.verifiziereZugriffFuerEvent(eventId);
        }catch (ExistiertNichtException e){
            return Optional.empty();
        }
        Event event = eventRepository.getEvent(eventId);
        return Optional.ofNullable(event);
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

    public List<Event> getEventsOfSerie(SerienId serienId) throws KeinZugriffException, ExistiertNichtException {
        leseZugriffVerfizierer.verifiziereZugriffFuerSerie(serienId);
        List<Event> events = eventRepository.getEventsOfSerie(serienId);
        return events.stream().filter(event -> {
            try {
                leseZugriffVerfizierer.verifiziereZugriffFuerEvent(event);
            } catch (KeinZugriffException e) {
                return false;
            }
            return true;
        }).toList();
    }

}
