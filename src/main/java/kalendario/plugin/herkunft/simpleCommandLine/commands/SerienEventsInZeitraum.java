package kalendario.plugin.herkunft.simpleCommandLine.commands;

import com.google.gson.Gson;
import kalendario.application.crud.event.EventRead;
import kalendario.application.crud.serie.SerieRead;
import kalendario.application.crud.sicherheit.ExistiertNichtException;
import kalendario.application.wiederholung.ZeitlicherAbstandWiederholung;
import kalendario.domain.entities.event.Event;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.value_objects.Zeitraum;
import kalendario.plugin.herkunft.simpleCommandLine.Command;
import kalendario.plugin.herkunft.simpleCommandLine.Parameter;

import java.util.*;

public class SerienEventsInZeitraum implements Command {

    SerieRead serieRead;
    EventRead eventRead;
    Gson gson;

    public SerienEventsInZeitraum(SerieRead serieRead, EventRead eventRead, Gson gson) {
        this.serieRead = serieRead;
        this.eventRead = eventRead;
        this.gson = gson;
    }

    @Override
    public boolean executeWithParameters(String command, List<Parameter> parameters) throws Exception {
        if(command.equals("serieInZeitraum")){
            SerienId serienId = new SerienId(UUID.fromString(parameters.get(0).asText()));
            Date start = parameters.get(1).asDate();
            Date end = parameters.get(2).asDate();
            Optional<Serie> maybeSerie = serieRead.getSerie(serienId);
            List<EventId> eventIds = maybeSerie.orElseThrow(() -> new ExistiertNichtException("Keine Serie mit Id " + serienId.getId().toString())).getEventsInZeitraum(new Zeitraum(start, end));
            List<Event> events = new ArrayList();
            for(int i = 0; i < eventIds.size(); i++){
                Event event = eventRead.getEvent(eventIds.get(i)).get();
                if(event.getId().equals(maybeSerie.get().getDefaultEvent())) {
                    for (int k = 0; k < i; k++) {
                        event.pushByDuration(((ZeitlicherAbstandWiederholung) maybeSerie.get().getWiederholung()).getAbstand());
                    }
                }
                events.add(event);
            }
            System.out.println(gson.toJson(events));
            return true;
        }
        return false;
    }
}
