package kalendario.plugin.herkunft.simpleCommandLine.commands;

import kalendario.application.crud.event.SerienEventAnpassung;
import kalendario.application.crud.herkunft.HerkunftCreate;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.serie.SerienId;
import kalendario.plugin.herkunft.simpleCommandLine.Command;
import kalendario.plugin.herkunft.simpleCommandLine.Parameter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AddEventToSerie implements Command {

    SerienEventAnpassung serienEventAnpassung;
    HerkunftCreate herkunftCreate;

    public AddEventToSerie(SerienEventAnpassung serienEventAnpassung, HerkunftCreate herkunftCreate) {
        this.serienEventAnpassung = serienEventAnpassung;
        this.herkunftCreate = herkunftCreate;
    }

    @Override
    public boolean executeWithParameters(String command, List<Parameter> parameters) throws Exception {
        if(command.equals("eventZuSerie")){
            SerienId serienId = new SerienId(UUID.fromString(parameters.get(0).asText()));
            EventId eventId = new EventId(UUID.fromString(parameters.get(1).asText()));
            Date originalerZeitpunktInSerie = parameters.get(2).asDate();
            serienEventAnpassung.fuegeEventSerieHinzu(serienId, originalerZeitpunktInSerie, eventId, herkunftCreate.createOrGetCommandLineHerkunftVonAktuellemBenutzer().getId());
            return true;
        }
        return false;
    }
}
