package kalendario.application.crud.benutzer;

import kalendario.domain.entities.benutzer.Benutzer;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.repositories.BenutzerRepository;
import kalendario.domain.repositories.SaveException;

import java.sql.SQLException;

public class BenutzerCreation {

    BenutzerRepository benutzerRepository;

    public BenutzerCreation(BenutzerRepository benutzerRepository){
        this.benutzerRepository = benutzerRepository;
    }

    public Benutzer createBenutzer(String name, String passwordHashed) throws BenutzerNameExistiertException, SaveException {
        try {
            if(benutzerRepository.benutzerNameExistiert(name)){
                throw new BenutzerNameExistiertException();
            }
        } catch (SQLException e) {
            throw new SaveException(e);
        }
        BenutzerId id = benutzerRepository.neueId();
        Benutzer benutzer = new Benutzer(id, name, passwordHashed);
        benutzerRepository.saveBenutzer(benutzer);
        return benutzer;
    }


}
