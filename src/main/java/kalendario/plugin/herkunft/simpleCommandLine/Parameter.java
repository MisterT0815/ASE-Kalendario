package kalendario.plugin.herkunft.simpleCommandLine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Parameter {
    public String asText;

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
                return simpleDateFormat.parse(asText);
            } catch (ParseException ex){
                throw new ParseException("Datum im Format \"dd.MM.yyyy\" oder \"hh:mm:ss@dd.MM.yyyy\" erwartet, aber \"" + asText + "\" gegeben", 0);
            }
        }
    }


}
