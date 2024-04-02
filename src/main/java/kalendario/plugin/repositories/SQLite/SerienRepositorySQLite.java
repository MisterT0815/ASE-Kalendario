package kalendario.plugin.repositories.SQLite;

import kalendario.application.wiederholung.ZeitlicherAbstandWiederholung;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.entities.serie.Wiederholung;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.repositories.SerienRepository;

import java.sql.*;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SerienRepositorySQLite implements SerienRepository {

    private static final String WIEDERHOLUNG_TYP_ZEITLICHER_ABSTAND = "ZeitlicherAbstand";
    private static final String CREATE_TABLE_SERIE = """
            CREATE TABLE IF NOT EXISTS Serie (
                SerienId VARCHAR(255) PRIMARY KEY,
                Start TIMESTAMP,
                defaultEventId VARCHAR(255),
                Wiederholungstyp VARCHAR(255),
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
                PRIMARY KEY (SerienId, Wann),
                FOREIGN KEY (SerienId) REFERENCES Serie(SerienId) ON UPDATE CASCADE ON DELETE CASCADE ,
                FOREIGN KEY (EventId) REFERENCES Events(EventId) ON UPDATE CASCADE ON DELETE CASCADE 
            );
            """;
    private static final String INSERT_SERIE = "INSERT INTO Serie (SerienId, Start, defaultEventId, Wiederholungstyp) VALUES (?, ?, ?, ?);";
    private static final String GET_SERIE = "SELECT * FROM Serie WHERE SerienId = ?;";
    private static final String INSERT_WIEDERHOLUNG_ZEITLICHER_ABSTAND = "INSERT INTO ZeitlicherAbstandWiederholung (SerienId, Abstand) VALUES (?, ?);";
    private static final String GET_ZEITLICHER_ABSTAND_WIEDERHOLUNG = "SELECT * FROM ZeitlicherAbstandWiederholung WHERE SerienId = ?;";
    private static final String INSERT_ANGEPASSTE_EVENTS = "INSERT INTO AngepassteEvents (SerienId, EventId, Wann) VALUES (?, ?, ?);";
    private static final String DELETE_ANGEPASSTE_EVENTS = "DELETE FROM AngepassteEvents WHERE SerienId = ? AND Wann = ?;";
    private static final String GET_ANGEPASSTE_EVENTS_OF_SERIENID = "SELECT * FROM AngepassteEvents WHERE SerienId = ?;";
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
        return new SerienId(UUID.randomUUID());
    }

    @Override
    public void saveSerie(Serie serie) throws SaveException {
        try{
            connection.setAutoCommit(false);
            this.saveSerienInformation(serie);
            this.saveWiederholungInformation(serie.getId(), serie.getWiederholung());
            for(Date date: serie.getAngepassteEventIds().keySet()){
                this.addAngepasstesEvent(serie.getId(), date, serie.getAngepassteEventIds().get(date));
            }
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new SaveException(e);
        }
    }

    @Override
    public void addAngepasstesEvent(SerienId serie, Date when, EventId event) throws SaveException {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ANGEPASSTE_EVENTS);
            preparedStatement.setString(1, serie.getId().toString());
            preparedStatement.setString(2, event.getId().toString());
            preparedStatement.setTimestamp(3, Timestamp.from(when.toInstant()));
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new SaveException(e);
        }
    }

    @Override
    public void removeAngepasstesEvent(SerienId serie, Date when) throws SaveException {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ANGEPASSTE_EVENTS);
            preparedStatement.setString(1, serie.getId().toString());
            preparedStatement.setTimestamp(2, Timestamp.from(when.toInstant()));
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new SaveException(e);
        }
    }

    @Override
    public Serie getSerie(SerienId serienId) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_SERIE);
            preparedStatement.setString(1, serienId.getId().toString());
            ResultSet results = preparedStatement.executeQuery();
            if(results.next()){
                EventId defaultEvent = new EventId(UUID.fromString(results.getString("defaultEventId")));
                Date start = new Date(Long.parseLong(results.getString("Start")));
                String wiederholungstyp = results.getString("Wiederholungstyp");
                Wiederholung wiederholung;
                switch (wiederholungstyp){
                    case WIEDERHOLUNG_TYP_ZEITLICHER_ABSTAND -> {
                        wiederholung = this.getZeitlicherAbstandWiederholung(serienId, start);
                        break;
                    }
                    default -> throw new SQLException("Wiederholung kein passender Typ");
                }
                Serie serie = new Serie(serienId, defaultEvent, start, wiederholung);
                Map<Date, EventId> angepassteEvents = this.getAngepassteEvents(serienId);
                for(Date date: angepassteEvents.keySet()){
                    serie.changeEventAnZeitpunkt(date, angepassteEvents.get(date));
                }
                return serie;
            }
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    private void saveSerienInformation(Serie serie) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SERIE);
        preparedStatement.setString(1, serie.getId().getId().toString());
        preparedStatement.setTimestamp(2, Timestamp.from(serie.getStart().toInstant()));
        preparedStatement.setString(3, serie.getDefaultEvent().getId().toString());
        if(serie.getWiederholung() instanceof ZeitlicherAbstandWiederholung){
            preparedStatement.setString(4, WIEDERHOLUNG_TYP_ZEITLICHER_ABSTAND);
        }else{
            throw new SQLException("Wiederholung kein gültiger Typ");
        }
        preparedStatement.execute();
    }

    private void saveWiederholungInformation(SerienId serie, Wiederholung wiederholung) throws SQLException {
        if(wiederholung instanceof ZeitlicherAbstandWiederholung){
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_WIEDERHOLUNG_ZEITLICHER_ABSTAND);
            preparedStatement.setString(1, serie.getId().toString());
            preparedStatement.setString(2, Long.toString(((ZeitlicherAbstandWiederholung) wiederholung).getAbstand().getSeconds()));
            preparedStatement.execute();
        }
    }

    private Wiederholung getZeitlicherAbstandWiederholung(SerienId serie, Date start) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_ZEITLICHER_ABSTAND_WIEDERHOLUNG);
        preparedStatement.setString(1,serie.getId().toString());
        ResultSet results = preparedStatement.executeQuery();
        if(results.next()){
            Duration duration = Duration.ofSeconds(results.getLong("Abstand"));
            return new ZeitlicherAbstandWiederholung(duration , start);
        }
        throw new SQLException("Keine Wiederholung gefunden");
    }

    private Map<Date, EventId> getAngepassteEvents(SerienId serie) throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement(GET_ANGEPASSTE_EVENTS_OF_SERIENID);
        preparedStatement.setString(1, serie.getId().toString());
        ResultSet results = preparedStatement.executeQuery();
        Map<Date, EventId> angepassteEvents = new HashMap<>();
        while(results.next()){
            angepassteEvents.put(
                    new Date(Long.parseLong(results.getString("Wann"))),
                    new EventId(UUID.fromString(results.getString("EventId")))
            );
        }
        return angepassteEvents;
    }
}
