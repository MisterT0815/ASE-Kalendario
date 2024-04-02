package kalendario.plugin.herkunft.simpleCommandLine.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kalendario.application.crud.event.EventRead;
import kalendario.application.crud.herkunft.HerkunftCreate;
import kalendario.domain.entities.event.Event;
import kalendario.plugin.herkunft.simpleCommandLine.Command;
import kalendario.plugin.herkunft.simpleCommandLine.Parameter;

import java.util.List;

public class AlleEventsVonHier implements Command {

    EventRead eventRead;
    HerkunftCreate herkunftCreate;

    public AlleEventsVonHier(EventRead eventRead, HerkunftCreate herkunftCreate) {
        this.eventRead = eventRead;
        this.herkunftCreate = herkunftCreate;
    }


    @Override
    public boolean executeWithParameters(String command, List<Parameter> parameters) throws Exception {
        if(command.equals("alleEvents")){
            System.out.println("Alle Events: \n");
            for (Event event: eventRead.getEventsOfHerkunft(herkunftCreate.createOrGetCommandLineHerkunftVonAktuellemBenutzer().getId())){
                Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
                System.out.println(gson.toJson(event));
            }
            return true;
        }
        return false;
    }
}
