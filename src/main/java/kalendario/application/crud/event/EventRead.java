package kalendario.application.crud.event;

import kalendario.application.crud.serie.SerieRead;
import kalendario.application.session.NoAccessException;
import kalendario.application.session.Session;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventRead {

    EventRepository eventRepository;
    Session session;
    SerieRead serieRead;

    public EventRead(EventRepository eventRepository, Session session, SerieRead serieRead) {
        this.eventRepository = eventRepository;
        this.session = session;
        this.serieRead = serieRead;
    }

    public Optional<Event> getEvent(EventId eventId) throws NoAccessException {
        Event event = eventRepository.getEvent(eventId);
        if(event == null){
            return Optional.empty();
        }
        if(!eventIstSichtbarFuerAktuellenNutzer(event)){
            throw new NoAccessException();
        }
        return Optional.of(event);
    }


    public Optional<Aufgabe> getAufgabe(EventId eventId) throws NoAccessException {
        Optional<Event> event = this.getEvent(eventId);
        try{
            return (Optional.of((Aufgabe) event.orElseThrow()));
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public Optional<GeplanteAufgabe> getGeplanteAufgabe(EventId eventId) throws NoAccessException {
        Optional<Event> event = this.getEvent(eventId);
        try{
            return (Optional.of((GeplanteAufgabe) event.orElseThrow()));
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public Optional<Termin> getTermin(EventId eventId) throws NoAccessException {
        Optional<Event> event = this.getEvent(eventId);
        try{
            return (Optional.of((Termin) event.orElseThrow()));
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public List<Event> getEventsOfSerie(SerienId serienId) throws NoAccessException {
        //Serie holen um zu verifizieren, dass Access auf Serie vorhanden ist
        serieRead.getSerie(serienId);
        List<Event> events = eventRepository.getEventsOfSerie(serienId);
        return events.stream().filter(this::eventIstSichtbarFuerAktuellenNutzer).toList();
    }

    private boolean eventIstSichtbarFuerAktuellenNutzer(Event event) {
        try{
            return event.istSichtbarFuer(session.getCurrentBenutzer().orElseThrow());
        }catch(NoSuchElementException e){
            return false;
        }
    }

}
