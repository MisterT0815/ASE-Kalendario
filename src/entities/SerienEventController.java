package entities;

import value_objects.Wiederholung;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SerienEventController<T extends Event> {
    private T event;
    private Date start;
    private Wiederholung wiederholung;
    private Map<Date, T> events = new HashMap<>();

    public SerienEventController(T event, Date start, Wiederholung wiederholung) {
        this.event = event;
        this.start = start;
        this.wiederholung = wiederholung;
    }
}
