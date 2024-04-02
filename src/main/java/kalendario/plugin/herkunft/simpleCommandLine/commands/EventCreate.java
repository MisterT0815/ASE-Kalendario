package kalendario.plugin.herkunft.simpleCommandLine.commands;

import kalendario.application.crud.event.EventCreation;
import kalendario.application.crud.herkunft.HerkunftCreate;
import kalendario.application.crud.herkunft.HerkunftRead;
import kalendario.domain.entities.event.Sichtbarkeit;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.value_objects.Zeitraum;
import kalendario.plugin.herkunft.simpleCommandLine.Command;
import kalendario.plugin.herkunft.simpleCommandLine.Parameter;

import javax.security.auth.login.LoginException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class EventCreate implements Command {

    EventCreation eventCreation;
    HerkunftCreate herkunftCreate;

    public EventCreate(EventCreation eventCreation, HerkunftCreate herkunftCreate) {
        this.eventCreation = eventCreation;
        this.herkunftCreate = herkunftCreate;
    }

    @Override
    public boolean executeWithParameters(String command, List<Parameter> parameters) throws Exception {
        if(command.equals("createEvent")){
            if(parameters.size()<5){
                throw new LoginException("CreateEvent brauch mindestens 5 Parameter: Titel, Sichtbarkeit, Beschreibung, StartDatum (EndDatum/Gemacht), (Gemacht)");
            }
            String titel = parameters.get(0).asText();
            HerkunftId herkunft = herkunftCreate.createOrGetCommandLineHerkunftVonAktuellemBenutzer().getId();
            Sichtbarkeit sichtbarkeit = parameters.get(1).asSichtbarkeit();
            String beschreibung = parameters.get(2).asText();
            Date date1 = parameters.get(3).asDate();
            Date date2 = null;
            Boolean getan = null;
            try{
                date2 = parameters.get(4).asDate();
            }catch(ParseException e){
                try {
                    getan = parameters.get(4).asBool();
                } catch (ParseException p){
                    throw new ParseException("Boolean oder Date als 5. Parameter erwartet", 0);
                }
            }
            if(parameters.size() == 6){
                getan = parameters.get(5).asBool();
            }
            if(date2 != null){
                Zeitraum zeitraum = new Zeitraum(date1, date2);
                if(getan == null) {
                    eventCreation.createEvent(titel, herkunft, sichtbarkeit, beschreibung, zeitraum);
                }else{
                    eventCreation.createEvent(titel, herkunft, sichtbarkeit, beschreibung, zeitraum, getan);
                }
            }else{
                eventCreation.createEvent(titel, herkunft, sichtbarkeit, beschreibung, date1, getan);
            }
            return true;
        }
        return false;
    }
}
