package kalendario.plugin.herkunft.simpleCommandLine.commands;

import kalendario.application.crud.serie.SerieCreation;
import kalendario.application.wiederholung.ZeitlicherAbstandWiederholung;
import kalendario.domain.entities.event.Event;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.serie.Wiederholung;
import kalendario.plugin.herkunft.simpleCommandLine.Command;
import kalendario.plugin.herkunft.simpleCommandLine.Parameter;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SerieCreate implements Command {

    SerieCreation serieCreation;

    public SerieCreate(SerieCreation serieCreation) {
        this.serieCreation = serieCreation;
    }

    @Override
    public boolean executeWithParameters(String command, List<Parameter> parameters) throws Exception {
        if(command.equals("createSerie")){
            EventId defaultEvent = new EventId(UUID.fromString(parameters.get(0).asText()));
            Date start = parameters.get(1).asDate();
            Duration abstand = parameters.get(2).asDuration();
            Wiederholung wiederholung= new ZeitlicherAbstandWiederholung(abstand, start);
            serieCreation.createSerie(defaultEvent, start, wiederholung);
            return true;
        }
        return false;
    }
}
