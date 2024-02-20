package kalendario.domain.entities.serie;


import kalendario.domain.entities.event.Event;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.value_objects.Zeitraum;

import java.util.*;

public class Serie<T extends Event> {

    private SerienId id;
    private EventId defaultEventId;
    private Date start;
    private Wiederholung wiederholung;
    private Map<Date, EventId> angepassteEventIds = new HashMap<>();

    public Serie(SerienId id, EventId defaultEventId, Date start, Wiederholung wiederholung) {
        this.id = id;
        this.defaultEventId = defaultEventId;
        this.start = start;
        this.wiederholung = wiederholung;
    }

    public void changeEventAnZeitpunkt(Date zeitpunkt, EventId event) throws IllegalArgumentException{
        if(!wiederholung.istZeitpunktInWiederholung(zeitpunkt)){
            throw new IllegalArgumentException("Zeitpunkt ist nicht Teil der Serie");
        }
        angepassteEventIds.put(zeitpunkt, event);
    }

    public void changeDefaultEvent(EventId changedEvent){
        this.defaultEventId = changedEvent;
    }

    public List<EventId> getEventsInZeitraum(Zeitraum zeitraum) throws IllegalArgumentException{
        if(zeitraum.getStart().after(start)){
            throw new IllegalArgumentException("Zeitraum ist vor Start der Serie");
        }
        List<EventId> eventsInZeitraum = new ArrayList<>();
        for(Date date : wiederholung.alleZeitpunkteInZeitraum(zeitraum)){
            eventsInZeitraum.add(getEventAnZeitpunkt(date));
        }
        return eventsInZeitraum;
    }

    public EventId getEventAnZeitpunkt(Date zeitpunkt) throws IllegalArgumentException{
        if(!wiederholung.istZeitpunktInWiederholung(zeitpunkt)){
            throw new IllegalArgumentException("Zeitpunkt ist nicht Teil der Serie");
        }
        if(angepassteEventIds.containsKey(zeitpunkt)){
            return angepassteEventIds.get(zeitpunkt);
        } else {
            return defaultEventId;
        }
    }

}
