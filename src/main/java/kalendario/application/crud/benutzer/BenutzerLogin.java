package kalendario.application.crud.benutzer;

import kalendario.domain.entities.benutzer.Benutzer;
import kalendario.domain.repositories.BenutzerRepository;

public class BenutzerLogin {

    BenutzerRepository benutzerRepository;

    public BenutzerLogin(BenutzerRepository benutzerRepository) {
        this.benutzerRepository = benutzerRepository;
    }

    public boolean verifyBenutzer(String name, String passwordHashed){
        return benutzerRepository.benutzerExistiert(name, passwordHashed);
    }

}
