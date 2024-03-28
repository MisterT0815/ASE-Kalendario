package kalendario.plugin.repositories.SQLite;

import kalendario.domain.entities.event.*;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.SaveException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

public class EventRepositorySQLite implements EventRepository {

    Connection connection;
    private static final String SICHTBARKEIT_PRIVAT = "PRIVAT";
    private static final String SICHTBARKEIT_PUBLIC = "PUBLIC";
    private static final String TYP_TERMIN = "TERMIN";
    private static final String TYP_AUFGABE = "AUFGABE";
    private static final String TYP_GEPLANTE_AUFGABE = "GEPLANTE_AUFGABE";
    private static final String CREATE_TABLE_EVENTS = """
                CREATE TABLE IF NOT EXISTS Events (
                    EventId VARCHAR(255) PRIMARY KEY,
                    Titel VARCHAR(255),
                    HerkunftId VARCHAR(255),
                    Sichtbarkeit VARCHAR(6),
                    Beschreibung TEXT,
                    SerienId VARCHAR(255),
                    Typ VARCHAR(16)
                );
            """;
    private static final String CREATE_TABLE_SICHTBARKEIT = """
                CREATE TABLE IF NOT EXISTS Sichtbarkeit (
                    EventId VARCHAR(255),
                    BenutzerId VARCHAR(255),
                    PRIMARY KEY (EventId, BenutzerId),
                    FOREIGN KEY (EventId) REFERENCES Events(EventId) ON UPDATE CASCADE ON DELETE CASCADE,
                    FOREIGN KEY (BenutzerId) REFERENCES Benutzer(BenutzerId) ON UPDATE CASCADE ON DELETE CASCADE
                );
            """;

    private static final String CREATE_TABLE_MACHBAR = """
                CREATE TABLE IF NOT EXISTS Machbar (
                    EventId VARCHAR(255) PRIMARY KEY,
                    Gemacht INTEGER(1),
                    Von VARCHAR(255),
                    FOREIGN KEY (EventId) REFERENCES Events(EventId) ON UPDATE CASCADE ON DELETE CASCADE,
                    FOREIGN KEY (Von) REFERENCES Benutzer(BenutzerId) ON UPDATE CASCADE ON DELETE CASCADE
                );
            """;
    private static final String CREATE_TABLE_ZEITRAUM = """
                CREATE TABLE IF NOT EXIST Zeitraum (
                    EventId VARCHAR(255) PRIMARY KEY,
                    Start Date
            """;

    public EventRepositorySQLite(Connection connection) throws SQLException {
        this.connection = connection;
        Statement createTables = connection.createStatement();
        createTables.execute(CREATE_TABLE_EVENTS);
        createTables.execute(CREATE_TABLE_SICHTBARKEIT);
        createTables.execute(CREATE_TABLE_MACHBAR);
    }


    @Override
    public EventId neueId() {
        return new EventId(UUID.randomUUID());
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
