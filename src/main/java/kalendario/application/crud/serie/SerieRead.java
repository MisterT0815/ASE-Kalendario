package kalendario.application.crud.serie;

import kalendario.application.crud.sicherheit.ZugriffVerfizierer;
import kalendario.application.session.KeinZugriffException;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.SerienRepository;

import java.util.Optional;

public class SerieRead {

    SerienRepository serienRepository;
    ZugriffVerfizierer zugriffVerfizierer;

    public SerieRead(SerienRepository serienRepository, ZugriffVerfizierer zugriffVerfizierer) {
        this.serienRepository = serienRepository;
        this.zugriffVerfizierer = zugriffVerfizierer;
    }

    public Optional<Serie> getSerie(SerienId serienId) throws KeinZugriffException {
        Serie serie =  serienRepository.getSerie(serienId);
        if(serie == null){
            return Optional.empty();
        }
        zugriffVerfizierer.verifiziereZugriffFuerSerie(serie);
        return Optional.of(serie);
    }


}
