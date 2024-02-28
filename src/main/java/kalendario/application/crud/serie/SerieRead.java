package kalendario.application.crud.serie;

import kalendario.application.crud.event.EventRead;
import kalendario.application.session.NoAccessException;
import kalendario.application.session.Session;
import kalendario.domain.entities.event.Event;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.SerienRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

public class SerieRead {

    SerienRepository serienRepository;
    EventRead eventRead;
    Session session;

    public SerieRead(SerienRepository serienRepository, Session session, EventRead eventRead) {
        this.serienRepository = serienRepository;
        this.session = session;
        this.eventRead = eventRead;
    }

    public Optional<Serie> getSerie(SerienId serienId) throws NoAccessException {
        Serie serie =  serienRepository.getSerie(serienId);
        if(serie == null){
            return Optional.empty();
        }
        if(!serieIstSichtbarFuerAktuellenNutzer(serie)){
            throw new NoAccessException();
        }
        return Optional.of(serie);
    }

    private boolean serieIstSichtbarFuerAktuellenNutzer(Serie serie) {
        try{
            return eventRead.getEvent(serie.getDefaultEvent()).orElseThrow().istSichtbarFuer(session.getCurrentBenutzer().orElseThrow());
        }catch (NoAccessException | NoSuchElementException e){
            return false;
        }
    }

}
