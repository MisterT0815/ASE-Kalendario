package kalendario.domain.entities.serie;


import kalendario.domain.entities.event.Event;
import kalendario.domain.entities.event.EventId;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
}
