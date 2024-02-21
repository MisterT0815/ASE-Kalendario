package kalendario.domain.repositories;

import kalendario.domain.entities.event.Aufgabe;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.event.GeplanteAufgabe;
import kalendario.domain.entities.event.Termin;

public interface EventRepository {

    EventId neueId();
    void saveTermin(Termin termin) throws SaveException;
    void saveAufgabe(Aufgabe aufgabe) throws SaveException;
    void saveGeplanteAufgabe(GeplanteAufgabe geplanteAufgabe) throws SaveException;

}
