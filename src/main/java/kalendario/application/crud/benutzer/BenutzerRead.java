package kalendario.application.crud.benutzer;

import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.repositories.BenutzerRepository;

import java.sql.SQLException;
import java.util.Optional;

public class BenutzerRead {

    BenutzerRepository benutzerRepository;

    public BenutzerRead(BenutzerRepository benutzerRepository) {
        this.benutzerRepository = benutzerRepository;
    }

    public boolean verifyBenutzer(String name, String passwordHashed) throws SQLException {
        return benutzerRepository.benutzerExistiert(name, passwordHashed);
    }

    public Optional<BenutzerId> getBenutzerIdOfName(String name){
        try {
            return Optional.ofNullable(benutzerRepository.getIdOfName(name));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public Optional<String> getBenutzerNameOfId(BenutzerId benutzerId){
        try {
            return Optional.ofNullable(benutzerRepository.getBenutzerNameOfId(benutzerId));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public boolean benutzerExistiert(String name){
        return getBenutzerIdOfName(name).isPresent();
    }

}
