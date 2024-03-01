package kalendario.application.crud.event;

import kalendario.application.crud.herkunft.HerkunftRead;
import kalendario.application.crud.serie.SerieRead;
import kalendario.application.crud.serie.SerieUpdate;
import kalendario.application.crud.sicherheit.SchreibZugriffVerifizierer;
import kalendario.application.session.KeinZugriffException;
import kalendario.application.session.Session;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.HerkunftRepository;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.repositories.SerienRepository;
import kalendario.domain.value_objects.Zeitraum;

import javax.sql.RowSetInternal;
import javax.sql.rowset.spi.TransactionalWriter;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Date;
import java.util.Optional;

public class SerienEventCreation {

    EventRepository eventRepository;
    SerieRead serieRead;
    SerieUpdate serieUpdate;
    EventRead eventRead;
    Session session;
    SchreibZugriffVerifizierer schreibZugriffVerifizierer;

    public SerienEventCreation(EventRepository eventRepository, SerieRead serieRead, SerieUpdate serieUpdate, EventRead eventRead, Session session, SchreibZugriffVerifizierer schreibZugriffVerifizierer) {
        this.eventRepository = eventRepository;
        this.serieRead = serieRead;
        this.serieUpdate = serieUpdate;
        this.eventRead = eventRead;
        this.session = session;
        this.schreibZugriffVerifizierer = schreibZugriffVerifizierer;
    }

    public Event createEvent(String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum, SerienId serie, Date originalerZeitpunktInSerie) throws SaveException, KeinZugriffException {
        schreibZugriffVerifizierer.verifiziereZugriffFuerHerkunft(herkunft);
        schreibZugriffVerifizierer.verifiziereZugriffFuerSerie(serie);

        EventId id = eventRepository.neueId();
        Termin termin = new Termin(id, titel, herkunft, sichtbarkeit, beschreibung, serie, zeitraum);
        Serie serieToEdit = serieMitGeandertemEvent(serie, originalerZeitpunktInSerie, id, herkunft);

        eventRepository.saveTermin(termin);
        serieUpdate.updateSerie(serieToEdit);

        return termin;
    }

    public Event createEvent(String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Date deadline, boolean getan, SerienId serie, Date originalerZeitpunktInSerie) throws SaveException, KeinZugriffException {
        schreibZugriffVerifizierer.verifiziereZugriffFuerHerkunft(herkunft);
        schreibZugriffVerifizierer.verifiziereZugriffFuerSerie(serie);

        EventId id = eventRepository.neueId();
        Aufgabe aufgabe = new Aufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, serie, deadline);
        if (getan) {
            aufgabe.setGetan(session.getCurrentBenutzer().orElseThrow(), true);
        }
        Serie serieToEdit = serieMitGeandertemEvent(serie, originalerZeitpunktInSerie, id, herkunft);

        eventRepository.saveAufgabe(aufgabe);
        serieUpdate.updateSerie(serieToEdit);

        return aufgabe;
    }

    public Event createEvent(String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum, boolean getan, SerienId serie, Date originalerZeitpunktInSerie) throws SaveException, KeinZugriffException {
        schreibZugriffVerifizierer.verifiziereZugriffFuerHerkunft(herkunft);
        schreibZugriffVerifizierer.verifiziereZugriffFuerSerie(serie);

        EventId id = eventRepository.neueId();
        GeplanteAufgabe geplanteAufgabe = new GeplanteAufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, serie, zeitraum);
        if (getan) {
            geplanteAufgabe.setGetan(session.getCurrentBenutzer().orElseThrow(), true);
        }
        Serie serieToEdit = serieMitGeandertemEvent(serie, originalerZeitpunktInSerie, id, herkunft);

        eventRepository.saveGeplanteAufgabe(geplanteAufgabe);
        serieUpdate.updateSerie(serieToEdit);

        return geplanteAufgabe;
    }

    private Serie serieMitGeandertemEvent(SerienId serie, Date originalerZeitpunktInSerie, EventId eventId, HerkunftId herkunft) throws SaveException, KeinZugriffException {
        Serie serieToEdit = serieRead.getSerie(serie).orElseThrow(() -> new SaveException("Serie existiert nicht"));
        Event defaultEvent = eventRead.getEvent(serieToEdit.getDefaultEvent()).orElseThrow(() -> new SaveException("Serie hat ungueltiges DefaultEvent"));

        if(!defaultEvent.getHerkunftId().equals(herkunft)){
            throw new SaveException("Herkunft von Serie und Herkunft des neuen Events sind verschieden");
        }

        try {
            serieToEdit.changeEventAnZeitpunkt(originalerZeitpunktInSerie, eventId);
        } catch(IllegalArgumentException e){
            throw new SaveException("Event ist nicht Teil der Serie, checke den urspünglichen Zeitpunkt in der Serie");
        }

        return serieToEdit;
    }

}
