package kalendario.application.crud.event;

import kalendario.application.crud.serie.SerieRead;
import kalendario.application.crud.sicherheit.ExistiertNichtException;
import kalendario.application.crud.sicherheit.SchreibZugriffVerifizierer;
import kalendario.application.session.KeinZugriffException;
import kalendario.application.session.Session;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.repositories.SerienRepository;
import kalendario.domain.value_objects.Zeitraum;

import java.util.Date;
import java.util.function.Consumer;

public class SerienEventAnpassung {

    private EventRepository eventRepository;
    private SerienRepository serienRepository;
    private SerieRead serieRead;
    private EventRead eventRead;
    private Session session;
    private SchreibZugriffVerifizierer schreibZugriffVerifizierer;

    public SerienEventAnpassung(EventRepository eventRepository, SerieRead serieRead, SerienRepository serienRepository, EventRead eventRead, Session session, SchreibZugriffVerifizierer schreibZugriffVerifizierer) {
        this.eventRepository = eventRepository;
        this.serieRead = serieRead;
        this.serienRepository = serienRepository;
        this.eventRead = eventRead;
        this.session = session;
        this.schreibZugriffVerifizierer = schreibZugriffVerifizierer;
    }

    public Event createEvent(String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum, SerienId serie, Date originalerZeitpunktInSerie) throws SaveException, KeinZugriffException, ExistiertNichtException {
        verifiziereZugriffe(herkunft, serie);

        EventId id = eventRepository.neueId();
        Termin termin = new Termin(id, titel, herkunft, sichtbarkeit, beschreibung, serie, zeitraum);
        fuegeEventSerieHinzu(serie, originalerZeitpunktInSerie, id, herkunft);

        try{
            eventRepository.saveTermin(termin);
        }catch (SaveException e){
            serienRepository.removeAngepasstesEvent(serie, originalerZeitpunktInSerie);
            throw e;
        }

        return termin;
    }

    public Event createEvent(String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Date deadline, boolean getan, SerienId serie, Date originalerZeitpunktInSerie) throws SaveException, KeinZugriffException, ExistiertNichtException {
        verifiziereZugriffe(herkunft, serie);

        EventId id = eventRepository.neueId();
        Aufgabe aufgabe = new Aufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, serie, deadline);
        if (getan) {
            aufgabe.setGetan(session.getCurrentBenutzer().orElseThrow(), true);
        }
        fuegeEventSerieHinzu(serie, originalerZeitpunktInSerie, id, herkunft);

        try{
            eventRepository.saveAufgabe(aufgabe);
        }catch (SaveException e){
            serienRepository.removeAngepasstesEvent(serie, originalerZeitpunktInSerie);
            throw e;
        }

        return aufgabe;
    }

    public Event createEvent(String titel, HerkunftId herkunft, Sichtbarkeit sichtbarkeit, String beschreibung, Zeitraum zeitraum, boolean getan, SerienId serie, Date originalerZeitpunktInSerie) throws SaveException, KeinZugriffException, ExistiertNichtException {
        verifiziereZugriffe(herkunft, serie);

        EventId id = eventRepository.neueId();
        GeplanteAufgabe geplanteAufgabe = new GeplanteAufgabe(id, titel, herkunft, sichtbarkeit, beschreibung, serie, zeitraum);
        if (getan) {
            geplanteAufgabe.setGetan(session.getCurrentBenutzer().orElseThrow(), true);
        }
        fuegeEventSerieHinzu(serie, originalerZeitpunktInSerie, id, herkunft);

        try{
            eventRepository.saveGeplanteAufgabe(geplanteAufgabe);
        }catch (SaveException e){
            serienRepository.removeAngepasstesEvent(serie, originalerZeitpunktInSerie);
            throw e;
        }

        return geplanteAufgabe;
    }

    private void fuegeEventSerieHinzu(SerienId serie, Date originalerZeitpunktInSerie, EventId eventId, HerkunftId herkunft) throws SaveException, KeinZugriffException {
        Serie serieToEdit = serieRead.getSerie(serie).orElseThrow(() -> new SaveException("Serie existiert nicht, sollte bereits früher ueberprueft worden sein"));
        Event defaultEvent = eventRead.getEvent(serieToEdit.getDefaultEvent()).orElseThrow(() -> new SaveException("Serie hat ungueltiges DefaultEvent"));

        if(!defaultEvent.getHerkunftId().equals(herkunft)){
            throw new SaveException("Herkunft von Serie und Herkunft des neuen Events sind verschieden");
        }

        try {
            serieToEdit.changeEventAnZeitpunkt(originalerZeitpunktInSerie, eventId);
        } catch(IllegalArgumentException e){
            throw new SaveException("Event ist nicht Teil der Serie, checke den urspünglichen Zeitpunkt in der Serie");
        }

        serienRepository.addAngepasstesEvent(serie, originalerZeitpunktInSerie, eventId);

    }

    private void verifiziereZugriffe(HerkunftId herkunft, SerienId serie) throws KeinZugriffException, SaveException {
        try{
            schreibZugriffVerifizierer.verifiziereZugriffFuerHerkunft(herkunft);
        }catch(ExistiertNichtException e){
            throw new SaveException(String.format("Herkunft mit Id %d existiert nicht", herkunft.getId()), e);
        }
        try{
            schreibZugriffVerifizierer.verifiziereZugriffFuerSerie(serie);
        }catch(ExistiertNichtException e) {
            throw new SaveException(String.format("Serie mit Id %d existiert nicht", herkunft.getId()), e);
        }
    }


}
