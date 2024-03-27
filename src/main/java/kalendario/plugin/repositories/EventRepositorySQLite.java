package kalendario.plugin.repositories;

import kalendario.domain.entities.event.*;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.SaveException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class EventRepositorySQLite implements EventRepository {

    Connection connection;

    public EventRepositorySQLite(Connection connection) throws SQLException {
        this.connection = connection;
        Statement createTable = connection.createStatement();
        createTable.execute("""
                CREATE TABLE IF NOT EXIST events(
                )
                """);
    }

    @Override
    public EventId neueId() {
        return null;
    }

    @Override
    public void saveTermin(Termin termin) throws SaveException {

    }

    @Override
    public void saveAufgabe(Aufgabe aufgabe) throws SaveException {

    }

    @Override
    public void saveGeplanteAufgabe(GeplanteAufgabe geplanteAufgabe) throws SaveException {

    }

    @Override
    public Event getEvent(EventId id) {
        return null;
    }

    @Override
    public List<Event> getEventsOfSerie(SerienId serie) {
        return null;
    }

    @Override
    public void setSerie(EventId event, SerienId serie) throws SaveException {

    }

    @Override
    public void setTitel(EventId event, String titel) throws SaveException {

    }

    @Override
    public void setBeschreibung(EventId event, String beschreibung) throws SaveException {

    }

    @Override
    public void setSichtbarkeit(EventId event, Sichtbarkeit sichtbarkeit) throws SaveException {

    }
}
