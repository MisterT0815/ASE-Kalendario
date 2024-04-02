package kalendario.plugin.herkunft.simpleCommandLine.commands;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kalendario.application.crud.serie.SerieCreation;
import kalendario.application.crud.serie.SerieRead;
import kalendario.domain.entities.serie.SerienId;
import kalendario.plugin.herkunft.simpleCommandLine.Command;
import kalendario.plugin.herkunft.simpleCommandLine.Parameter;

import java.util.List;
import java.util.UUID;

public class SerieInfo implements Command {

    SerieRead serieRead;
    Gson gson;

    public SerieInfo(SerieRead serieRead, Gson gson) {
        this.serieRead = serieRead;
        this.gson = gson;
    }

    @Override
    public boolean executeWithParameters(String command, List<Parameter> parameters) throws Exception {
        if(command.equals("serieInfo")){
            System.out.println(gson.toJson(serieRead.getSerie(new SerienId(UUID.fromString(parameters.get(0).asText())))));
            return true;
        }
        return false;
    }
}
