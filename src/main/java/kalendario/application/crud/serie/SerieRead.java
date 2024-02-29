package kalendario.application.crud.serie;

import kalendario.application.crud.sicherheit.LeseZugriffVerfizierer;
import kalendario.application.session.KeinZugriffException;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.SerienRepository;

import java.util.Optional;

public class SerieRead {

    SerienRepository serienRepository;
    LeseZugriffVerfizierer leseZugriffVerfizierer;

    public SerieRead(SerienRepository serienRepository, LeseZugriffVerfizierer leseZugriffVerfizierer) {
        this.serienRepository = serienRepository;
        this.leseZugriffVerfizierer = leseZugriffVerfizierer;
    }

    public Optional<Serie> getSerie(SerienId serienId) throws KeinZugriffException {
        Serie serie =  serienRepository.getSerie(serienId);
        if(serie == null){
            return Optional.empty();
        }
        leseZugriffVerfizierer.verifiziereZugriffFuerSerie(serie);
        return Optional.of(serie);
    }


}
