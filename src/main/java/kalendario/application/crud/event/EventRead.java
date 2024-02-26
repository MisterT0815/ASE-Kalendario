package kalendario.application.crud.event;

import kalendario.application.crud.exception.NotAvailableException;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;

import java.util.List;

public class EventRead {

    EventRepository eventRepository;

    public EventRead(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event getEvent(EventId eventId){
        return eventRepository.getEvent(eventId);
    }

    public Aufgabe getAufgabe(EventId eventId) throws NotAvailableException {
        Event event = eventRepository.getEvent(eventId);
        if(event == null || !(event instanceof Aufgabe)){
            throw new NotAvailableException(String.format("Aufgabe mit Id %d exitstiert nicht", eventId.getId()));
        }
        return (Aufgabe) event;
    }

    public GeplanteAufgabe getGeplanteAufgabe(EventId eventId) throws NotAvailableException {
        Event event = eventRepository.getEvent(eventId);
        if(event == null || !(event instanceof GeplanteAufgabe)){
            throw new NotAvailableException(String.format("GeplanteAufgabe mit Id %d exitstiert nicht", eventId.getId()));
        }
        return (GeplanteAufgabe) event;
    }

    public Termin getTermin(EventId eventId) throws NotAvailableException {
        Event event = eventRepository.getEvent(eventId);
        if(event == null || !(event instanceof Termin)){
            throw new NotAvailableException(String.format("Termin mit Id %d exitstiert nicht", eventId.getId()));
        }
        return (Termin) event;
    }

    public List<Event> getEventsOfSerie(SerienId serienId){
        return eventRepository.getEventsOfSerie(serienId);
    }

}
