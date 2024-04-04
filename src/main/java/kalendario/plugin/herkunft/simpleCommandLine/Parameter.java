package kalendario.plugin.herkunft.simpleCommandLine;

import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.PrivateSichtbarkeit;
import kalendario.domain.entities.event.PublicSichtbarkeit;
import kalendario.domain.entities.event.Sichtbarkeit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

public class Parameter {
    private String asText;

    public Parameter(String text){
        this.asText = text;
    }

    public String asText(){
        return this.asText;
    }

    public Date asDate() throws ParseException {
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
            return simpleDateFormat.parse(asText);
        } catch (ParseException e){
            try{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss@dd.MM.yyyy");
                return simpleDateFormat.parse(asText());
            } catch (ParseException ex){
                throw new ParseException("Datum im Format \"dd.MM.yyyy\" oder \"hh:mm:ss@dd.MM.yyyy\" erwartet, aber \"" + asText + "\" gegeben", 0);
            }
        }
    }

    public Sichtbarkeit asSichtbarkeit() throws ParseException{
        try {
            if (this.asText().equals("Public")) {
                return new PublicSichtbarkeit();
            } else if (this.asText().startsWith("Privat(")) {
                String working = this.asText.replace("Privat(", "");
                working = working.replace(")", "");
                String[] benutzerIds = working.split(",");
                return new PrivateSichtbarkeit(Arrays.stream(benutzerIds).map(benutzerId -> new BenutzerId(UUID.fromString(benutzerId.trim()))).collect(Collectors.toSet()));
            }
        } catch (Exception e){
            throw new ParseException("Sichtbarkeit erwartet, dies sollte entweder \"Public\" sein, oder \"Privat(*,*,*)\" wobei * BenutzerIds sind. Es wurde aber \"" + asText + "\" gegeben.", 0);
        }
        return null;
    }


    public boolean asBool() throws ParseException {
        if(this.asText().equalsIgnoreCase("true")){
            return true;
        }else if(this.asText().equalsIgnoreCase("false")) {
            return false;
        }
        throw new ParseException("Boolean erwartet (\"true\" oder \"false\", aber \"" + asText + "\" erhalten", 0);
    }

    public Duration asDuration() throws ParseException {
        try {
            String[] info = asText().split(":");
            int days = Integer.parseInt(info[0]);
            int hours = Integer.parseInt(info[1]);
            int minutes = Integer.parseInt(info[2]);
            int seconds = Integer.parseInt(info[3]);
            return Duration.ofSeconds(seconds + minutes* 60L + hours* 3600L + days *3600L*24);
        }catch (Exception e){
            throw new ParseException("Duration erwartet im Format \"dd:hh:mm:ss\" erwartet, aber \"" + asText + "\" gegeben", 0);
        }
    }
}
