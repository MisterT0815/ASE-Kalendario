package kalendario.domain.repositories;

import kalendario.domain.entities.event.*;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.entities.serie.SerienId;

import java.util.List;

public interface EventRepository {

    EventId neueId();
    void saveTermin(Termin termin) throws SaveException;
    void saveAufgabe(Aufgabe aufgabe) throws SaveException;
    void saveGeplanteAufgabe(GeplanteAufgabe geplanteAufgabe) throws SaveException;
    Event getEvent(EventId id);
    List<Event> getEventsOfSerie(SerienId serie);
    void setSerie(EventId event, SerienId serie) throws SaveException;
    void setTitel(EventId event, String titel) throws SaveException;
    void setBeschreibung(EventId event, String beschreibung) throws SaveException;
    void setSichtbarkeit(EventId event, Sichtbarkeit sichtbarkeit) throws SaveException;
    List<Event> getEventsOfHerkunft(HerkunftId herkunftId);
}
