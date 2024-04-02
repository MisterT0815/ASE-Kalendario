package kalendario.plugin.herkunft.simpleCommandLine.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kalendario.application.crud.event.EventRead;
import kalendario.domain.entities.event.EventId;
import kalendario.plugin.gson.GsonOptionalDeserializer;
import kalendario.plugin.herkunft.simpleCommandLine.Command;
import kalendario.plugin.herkunft.simpleCommandLine.Parameter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EventInfo implements Command {

    EventRead eventRead;
    Gson gson;

    public EventInfo(EventRead eventRead, Gson gson) {
        this.eventRead = eventRead;
        this.gson = gson;
    }

    @Override
    public boolean executeWithParameters(String command, List<Parameter> parameters) throws Exception {
        if(command.equals("eventInfo")){
            System.out.println(gson.toJson(eventRead.getEvent(new EventId(UUID.fromString(parameters.get(0).asText())))));
            return true;
        }
        return false;
    }
}
