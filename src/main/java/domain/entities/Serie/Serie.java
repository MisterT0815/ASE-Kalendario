package main.java.domain.entities.Serie;

import main.java.domain.entities.Event.Event;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Serie<T extends Event> {

    private SerienId id;
    private T defaultEvent;
    private Date start;
    private Wiederholung wiederholung;
    private Map<Date, T> events = new HashMap<>();

    public Serie(SerienId id, T defaultEvent, Date start, Wiederholung wiederholung) {
        this.defaultEvent = defaultEvent;
        this.start = start;
        this.wiederholung = wiederholung;
    }
}
