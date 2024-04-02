package kalendario.application.herkunft;

import kalendario.application.session.KeinZugriffException;
import kalendario.application.session.Session;
import kalendario.domain.entities.benutzer.Benutzer;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.herkunft.HerkunftId;

import java.util.Objects;

public class CommandLineHerkunft implements Herkunft {

    private BenutzerId benutzer;
    private HerkunftId id;

    public CommandLineHerkunft(Session session) throws KeinZugriffException {
        this.benutzer = session.getCurrentBenutzer().orElseThrow(KeinZugriffException::new);
    }

    public CommandLineHerkunft(BenutzerId benutzer, HerkunftId herkunftId){
        this.benutzer = benutzer;
        this.id = herkunftId;
    }

    @Override
    public BenutzerId getBesitzerId() {
        return benutzer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommandLineHerkunft that = (CommandLineHerkunft) o;
        return Objects.equals(benutzer, that.benutzer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(benutzer);
    }

    @Override
    public HerkunftId getId() {
        return id;
    }

    public void setId(HerkunftId id) {
        this.id = id;
    }
}
