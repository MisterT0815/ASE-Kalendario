package kalendario.application.crud.event;

import kalendario.application.crud.exception.NotAvailableException;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;

import java.util.List;
import java.util.Optional;

public class EventRead {

    EventRepository eventRepository;

    public EventRead(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event getEvent(EventId eventId){
        return eventRepository.getEvent(eventId);
    }

    public Optional<Aufgabe> getAufgabe(EventId eventId) {
        Event event = this.getEvent(eventId);
        try{
            return (Optional.of((Aufgabe) event));
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public Optional<GeplanteAufgabe> getGeplanteAufgabe(EventId eventId) {
        Event event = this.getEvent(eventId);
        try{
            return (Optional.of((GeplanteAufgabe) event));
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public Optional<Termin> getTermin(EventId eventId) {
        Event event = this.getEvent(eventId);
        try{
            return (Optional.of((Termin) event));
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public List<Event> getEventsOfSerie(SerienId serienId){
        return eventRepository.getEventsOfSerie(serienId);
    }

}
