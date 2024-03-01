package kalendario.application.crud.herkunft;

import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.repositories.HerkunftRepository;

import java.util.Optional;

public class HerkunftRead {

    HerkunftRepository herkunftRepository;

    public HerkunftRead(HerkunftRepository herkunftRepository) {
        this.herkunftRepository = herkunftRepository;
    }

    public Optional<Herkunft> getHerkunft(HerkunftId herkunftId){
        return Optional.ofNullable(herkunftRepository.getHerkunftMitId(herkunftId));
    }

}
