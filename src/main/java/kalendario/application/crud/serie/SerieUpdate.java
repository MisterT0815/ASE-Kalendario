package kalendario.application.crud.serie;

import kalendario.application.crud.sicherheit.SchreibZugriffVerifizierer;
import kalendario.application.session.KeinZugriffException;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.repositories.SerienRepository;

public class SerieUpdate {

    SerienRepository serienRepository;
    SchreibZugriffVerifizierer schreibZugriffVerifizierer;

    public SerieUpdate(SerienRepository serienRepository, SchreibZugriffVerifizierer schreibZugriffVerifizierer) {
        this.serienRepository = serienRepository;
        this.schreibZugriffVerifizierer = schreibZugriffVerifizierer;
    }

    public void updateSerie(Serie serie) throws KeinZugriffException, SaveException {
        schreibZugriffVerifizierer.verifiziereZugriffFuerSerie(serie);
        serienRepository.editSerie(serie);
    }

}
