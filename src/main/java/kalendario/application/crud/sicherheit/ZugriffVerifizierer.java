package kalendario.application.crud.sicherheit;

import kalendario.application.session.KeinZugriffException;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.Event;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;

public interface ZugriffVerifizierer {

    void verifiziereZugriffFuerSerie(Serie serie) throws KeinZugriffException;
    void verifiziereZugriffFuerSerie(SerienId serienId) throws KeinZugriffException;
    public void verifiziereZugriffFuerEvent(Event event) throws KeinZugriffException;
    public void verifiziereZugriffFuerEvent(EventId eventId) throws KeinZugriffException;
}
