package kalendario.application.crud.event;

import kalendario.application.crud.benutzer.BenutzerRead;
import kalendario.application.crud.sicherheit.ExistiertNichtException;
import kalendario.application.crud.sicherheit.SchreibZugriffVerifizierer;
import kalendario.application.session.KeinZugriffException;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.*;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.SaveException;

import java.util.Set;

public class EventUpdate {

    EventRepository eventRepository;
    EventRead eventRead;
    BenutzerRead benutzerRead;
    SchreibZugriffVerifizierer schreibZugriffVerifizierer;

    public EventUpdate(EventRepository eventRepository, EventRead eventRead, BenutzerRead benutzerRead, SchreibZugriffVerifizierer schreibZugriffVerifizierer) {
        this.eventRepository = eventRepository;
        this.eventRead = eventRead;
        this.benutzerRead = benutzerRead;
        this.schreibZugriffVerifizierer = schreibZugriffVerifizierer;
    }

    public Event setzeTitel(EventId eventId, String titel) throws KeinZugriffException, ExistiertNichtException, SaveException {
        schreibZugriffVerifizierer.verifiziereZugriffFuerEvent(eventId);
        eventRepository.setTitel(eventId, titel);
        return eventRepository.getEvent(eventId);
    }

    public Event setzteBeschreibung(EventId eventId, String beschreibung) throws KeinZugriffException, ExistiertNichtException, SaveException {
        schreibZugriffVerifizierer.verifiziereZugriffFuerEvent(eventId);
        eventRepository.setBeschreibung(eventId, beschreibung);
        return eventRepository.getEvent(eventId);
    }

    public Event machOeffentlich(EventId eventId) throws KeinZugriffException, ExistiertNichtException, SaveException {
        schreibZugriffVerifizierer.verifiziereZugriffFuerEvent(eventId);
        eventRepository.setSichtbarkeit(eventId, new PublicSichtbarkeit());
        return eventRepository.getEvent(eventId);
    }

    public Event machPrivat(EventId eventId) throws KeinZugriffException, ExistiertNichtException, SaveException {
        schreibZugriffVerifizierer.verifiziereZugriffFuerEvent(eventId);
        eventRepository.setSichtbarkeit(eventId, new PrivateSichtbarkeit(Set.of()));
        return eventRepository.getEvent(eventId);
    }

    public Event machSichtbarFuer(EventId eventId, String benutzerName) throws KeinZugriffException, ExistiertNichtException, SaveException {
        schreibZugriffVerifizierer.verifiziereZugriffFuerEvent(eventId);
        BenutzerId benutzer = benutzerRead.getBenutzerIdOfName(benutzerName).orElseThrow(() -> new ExistiertNichtException(String.format("Kein Benutzer mit Name %s existiert", benutzerName)));
        PrivateSichtbarkeit sichtbarkeit = getPrivateSichtbarkeitFuerEvent(eventId);

        sichtbarkeit.machSichtbarFuer(benutzer);
        eventRepository.setSichtbarkeit(eventId, sichtbarkeit);

        return eventRepository.getEvent(eventId);
    }

    public Event entferneSichtbarkeitFuer(EventId eventId, String benutzerName) throws KeinZugriffException, ExistiertNichtException, SaveException {
        schreibZugriffVerifizierer.verifiziereZugriffFuerEvent(eventId);
        BenutzerId benutzer = benutzerRead.getBenutzerIdOfName(benutzerName).orElseThrow(() -> new ExistiertNichtException(String.format("Kein Benutzer mit Name %s existiert", benutzerName)));
        PrivateSichtbarkeit sichtbarkeit = getPrivateSichtbarkeitFuerEvent(eventId);

        sichtbarkeit.entferneSichtbarkeitFuer(benutzer);
        eventRepository.setSichtbarkeit(eventId, sichtbarkeit);

        return eventRepository.getEvent(eventId);
    }

    private PrivateSichtbarkeit getPrivateSichtbarkeitFuerEvent(EventId eventId) throws KeinZugriffException, ExistiertNichtException {
        Sichtbarkeit sichtbarkeit = eventRepository.getEvent(eventId).getSichtbarkeit();
        if(!(sichtbarkeit instanceof PrivateSichtbarkeit)){
            throw new KeinZugriffException("Sichtbarkeit kann nicht gegeben werden, da sie sowieso Public ist");
        }
        return (PrivateSichtbarkeit) sichtbarkeit;
    }
}
