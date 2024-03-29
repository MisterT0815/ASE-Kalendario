package kalendario.plugin.repositories.SQLite;

import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.repositories.SerienRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class SerienRepositorySQLite implements SerienRepository {

    private static final String CREATE_TABLE_SERIE = """
            CREATE TABLE IF NOT EXISTS Serie (
                SerienId VARCHAR(255) PRIMARY KEY,
                Start TIMESTAMP,
                defaultEventId VARCHAR(255),
                FOREIGN KEY (defaultEventId) REFERENCES Events(EventId) ON UPDATE CASCADE ON DELETE CASCADE 
            );
        """;

    private static final String CREATE_TABLE_ZEITLICHER_ABSTAND_WIEDERHOLUNG = """
            CREATE TABLE IF NOT EXISTS ZeitlicherAbstandWiederholung (
                SerienId VARCHAR(255) PRIMARY KEY,
                Abstand TIMESTAMP,
                FOREIGN KEY (SerienId) REFERENCES Serie(SerienId) ON UPDATE CASCADE ON DELETE CASCADE 
            );
            """;

    private static final String CREATE_TABLE_ANGEPASSTE_EVENTS = """
            CREATE TABLE IF NOT EXISTS AngepassteEvents (
                SerienId VARCHAR(255),
                EventId VARCHAR(255),
                Wann TIMESTAMP,
                PRIMARY KEY (SerienId, EventId),
                FOREIGN KEY (SerienId) REFERENCES Serie(SerienId) ON UPDATE CASCADE ON DELETE CASCADE ,
                FOREIGN KEY (EventId) REFERENCES Events(EventId) ON UPDATE CASCADE ON DELETE CASCADE 
            );
            """;

    Connection connection;

    public SerienRepositorySQLite(Connection connection) throws SQLException {
        this.connection = connection;
        Statement createTables = connection.createStatement();
        createTables.execute(CREATE_TABLE_SERIE);
        createTables.execute(CREATE_TABLE_ZEITLICHER_ABSTAND_WIEDERHOLUNG);
        createTables.execute(CREATE_TABLE_ANGEPASSTE_EVENTS);
    }

    @Override
    public SerienId neueId() {
        return null;
    }

    @Override
    public void saveSerie(Serie serie) throws SaveException {

    }

    @Override
    public void addAngepasstesEvent(Date when, EventId event) throws SaveException {

    }

    @Override
    public void removeAngepasstesEvent(Date when) throws SaveException {

    }

    @Override
    public Serie getSerie(SerienId serienId) {
        return null;
    }
}
