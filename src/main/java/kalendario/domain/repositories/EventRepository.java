package kalendario.domain.repositories;

import kalendario.domain.entities.event.*;
import kalendario.domain.entities.serie.SerienId;

import java.util.List;

public interface EventRepository {

    EventId neueId();
    void saveTermin(Termin termin) throws SaveException;
    void saveAufgabe(Aufgabe aufgabe) throws SaveException;
    void saveGeplanteAufgabe(GeplanteAufgabe geplanteAufgabe) throws SaveException;
    Event getEvent(EventId id);
    List<Event> getEventsOfSerie(SerienId serie);

}
