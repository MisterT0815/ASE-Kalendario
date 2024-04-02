package kalendario.application.crud.herkunft;

import kalendario.application.herkunft.CommandLineHerkunft;
import kalendario.application.session.KeinZugriffException;
import kalendario.application.session.Session;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.repositories.HerkunftRepository;
import kalendario.domain.repositories.SaveException;

import java.util.List;
import java.util.Optional;

public class HerkunftCreate {

    HerkunftRepository herkunftRepository;
    Session session;

    public HerkunftCreate(HerkunftRepository herkunftRepository, Session session) {
        this.herkunftRepository = herkunftRepository;
        this.session = session;
    }

    public Herkunft createOrGetCommandLineHerkunftVonAktuellemBenutzer() throws KeinZugriffException {
        CommandLineHerkunft commandLineHerkunft = new CommandLineHerkunft(session);
        List<Herkunft>  herkuenfteVonBesitzer = herkunftRepository.getHerkuenfteVonBesitzer(session.getCurrentBenutzer().orElseThrow(KeinZugriffException::new));
        Optional<Herkunft> schonGespeicherteHerkunft =  herkuenfteVonBesitzer.stream().filter(herkunft -> herkunft.equals(commandLineHerkunft)).findFirst();
        if(schonGespeicherteHerkunft.isEmpty()){
            commandLineHerkunft.setId(herkunftRepository.neueId());
            try {
                herkunftRepository.saveHerkunft(commandLineHerkunft);
            } catch (SaveException e) {
                throw new KeinZugriffException(e);
            }
            return commandLineHerkunft;
        }else{
            return schonGespeicherteHerkunft.get();
        }
    }

}
