package kalendario.application.crud.serie;

import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.SerienRepository;

import java.util.Optional;

public class SerieRead {

    SerienRepository serienRepository;

    public SerieRead(SerienRepository serienRepository) {
        this.serienRepository = serienRepository;
    }

    public Optional<Serie> getSerie(SerienId serienId){
        return Optional.ofNullable(serienRepository.getSerie(serienId));
    }


}
