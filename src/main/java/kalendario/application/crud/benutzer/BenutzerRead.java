package kalendario.application.crud.benutzer;

import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.repositories.BenutzerRepository;

import java.util.Optional;

public class BenutzerRead {

    BenutzerRepository benutzerRepository;

    public BenutzerRead(BenutzerRepository benutzerRepository) {
        this.benutzerRepository = benutzerRepository;
    }

    public boolean verifyBenutzer(String name, String passwordHashed){
        return benutzerRepository.benutzerExistiert(name, passwordHashed);
    }

    public Optional<BenutzerId> getBenutzerIdOfName(String name){
        return Optional.ofNullable(benutzerRepository.getIdOfName(name));
    }

}
