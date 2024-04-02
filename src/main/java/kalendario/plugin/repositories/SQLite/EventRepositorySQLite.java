package kalendario.plugin.repositories.SQLite;

import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.value_objects.Zeitraum;

import java.sql.*;
import java.util.*;
import java.util.Date;

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
                CREATE TABLE IF NOT EXISTS Zeitraum (
                    EventId VARCHAR(255) PRIMARY KEY,
                    Start TIMESTAMP,
                    Ende TIMESTAMP,
                    FOREIGN KEY (EventId) REFERENCES Events(EventId) ON UPDATE CASCADE ON DELETE CASCADE
                );
            """;

    private static final String CREATE_TABLE_DEADLINE = """
                CREATE TABLE IF NOT EXISTS Deadline (
                    EventId VARCHAR(255) PRIMARY KEY,
                    Deadline TIMESTAMP,
                    FOREIGN KEY (EventId) REFERENCES Events(EventId) ON UPDATE CASCADE ON DELETE CASCADE
                );
            """;

    private static final String INSERT_EVENT = "INSERT INTO Events (EventId, Titel, HerkunftId, Sichtbarkeit, Beschreibung, SerienId, Typ) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String GET_EVENT = "SELECT * FROM Events WHERE EventId = ?;";
    private static final String GET_EVENT_OF_SERIE = "SELECT * FROM Events WHERE SerienId = ?;";
    private static final String GET_EVENT_OF_HERKUNFT = "SELECT * FROM Events WHERE HerkunftId = ?;";
    private static final String INSERT_ZEITRAUM = "INSERT INTO Zeitraum (EventId, Start, Ende) VALUES (?, ?, ?);";
    private static final String GET_ZEITRAUM = "SELECT * FROM Zeitraum WHERE EventId = ?;";
    private static final String INSERT_SICHTBARKEIT = "INSERT INTO Sichtbarkeit (EventId, BenutzerId) VALUES (?, ?);";
    private static final String GET_SICHTBARKEIT = "SELECT * FROM Sichtbarkeit WHERE EventId = ?;";
    private static final String INSERT_MACHBAR = "INSERT INTO Machbar (EventId, Gemacht, Von) VALUES (?, ?, ?);";
    private static final String GET_MACHBAR = "SELECT * FROM Machbar WHERE EventId = ?;";
    private static final String INSERT_DEADLINE = "INSERT INTO Deadline (EventId, Deadline) VALUES (?, ?);";
    private static final String GET_DEADLINE = "SELECT * FROM Deadline WHERE EventId = ?;";
    private static final String UPDATE_SERIEN_ID = "UPDATE Events SET SerienId = ? WHERE EventId = ?;";
    private static final String UPDATE_TITEL = "UPDATE Events SET Titel = ? WHERE EventId = ?;";
    private static final String UPDATE_BESCHREIBUNG = "UPDATE Events SET Beschreibung = ? WHERE EventId = ?;";
    private static final String UPDATE_SICHTBARKEIT = "UPDATE Events SET Sichtbarkeit = ? WHERE EventId = ?;";
    private static final String DELETE_EVENT_FROM_SICHTBARKEIT = "DELETE FROM Sichtbarkeit WHERE EventId = ?;";

    public EventRepositorySQLite(Connection connection) throws SQLException {
        this.connection = connection;
        Statement createTables = connection.createStatement();
        createTables.execute(CREATE_TABLE_EVENTS);
        createTables.execute(CREATE_TABLE_SICHTBARKEIT);
        createTables.execute(CREATE_TABLE_MACHBAR);
        createTables.execute(CREATE_TABLE_ZEITRAUM);
        createTables.execute(CREATE_TABLE_DEADLINE);
    }


    @Override
    public EventId neueId() {
        return new EventId(UUID.randomUUID());
    }

    @Override
    public void saveTermin(Termin termin) throws SaveException {
        try{
            connection.setAutoCommit(false);
            this.saveEventInformation(termin, TYP_TERMIN);
            this.saveZeitraum(termin.getId(), termin.getZeitraum());
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new SaveException(e);
        }
    }

    @Override
    public void saveAufgabe(Aufgabe aufgabe) throws SaveException {
        try{
            connection.setAutoCommit(false);
            this.saveEventInformation(aufgabe, TYP_AUFGABE);
            this.saveDeadline(aufgabe.getId(), aufgabe.getDeadline());
            this.saveMachbar(aufgabe.getId(), aufgabe.istGetan(), aufgabe.wurdeGemachtVon().orElse(null));
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new SaveException(e);
        }
    }

    @Override
    public void saveGeplanteAufgabe(GeplanteAufgabe geplanteAufgabe) throws SaveException {
        try{
            connection.setAutoCommit(false);
            this.saveEventInformation(geplanteAufgabe, TYP_GEPLANTE_AUFGABE);
            this.saveZeitraum(geplanteAufgabe.getId(), geplanteAufgabe.getZeitraum());
            this.saveMachbar(geplanteAufgabe.getId(), geplanteAufgabe.istGetan(), geplanteAufgabe.wurdeGemachtVon().orElse(null));
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new SaveException(e);
        }
    }

    private void saveEventInformation(Event event, String typ) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EVENT);
        preparedStatement.setString(1, event.getId().getId().toString());
        preparedStatement.setString(2, event.getTitel());
        preparedStatement.setString(3, event.getHerkunftId().getId().toString());
        if(event.getSichtbarkeit() instanceof PublicSichtbarkeit){
            preparedStatement.setString(4, SICHTBARKEIT_PUBLIC);
        }else if(event.getSichtbarkeit() instanceof PrivateSichtbarkeit){
            preparedStatement.setString(4, SICHTBARKEIT_PRIVAT);
            savePrivateSichtbarkeit(event.getId(), (PrivateSichtbarkeit) event.getSichtbarkeit());
        }
        preparedStatement.setString(5, event.getBeschreibung());
        if (event.getSerienId().isPresent()) {
            preparedStatement.setString(6, event.getSerienId().get().getId().toString());
        } else {
            preparedStatement.setNull(6, Types.NULL);
        }
        preparedStatement.setString(7, typ);
        preparedStatement.execute();
    }

    @Override
    public Event getEvent(EventId id) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_EVENT);
            preparedStatement.setString(1, id.getId().toString());
            ResultSet resultsEvent = preparedStatement.executeQuery();
            Event event = null;
            if(resultsEvent.next()) {
                event = getEventFromResultSet(resultsEvent);
            }
            if (event != null) return event;
            return null;
        } catch (SQLException e) {
            return null;
        }
    }



    @Override
    public List<Event> getEventsOfSerie(SerienId serie) {
        return getEventsOfStatementAndID(GET_EVENT_OF_SERIE, serie.getId());
    }

    @Override
    public List<Event> getEventsOfHerkunft(HerkunftId herkunftId) {
        return getEventsOfStatementAndID(GET_EVENT_OF_HERKUNFT, herkunftId.getId());
    }

    private List<Event> getEventsOfStatementAndID(String getEventOfHerkunft, UUID id) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(getEventOfHerkunft);
            preparedStatement.setString(1, id.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Event> events = new ArrayList<>();
            while(resultSet.next()){
                Event event = getEventFromResultSet(resultSet);
                if( event != null){
                    events.add(event);
                }
            }
            return events;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public void setSerie(EventId event, SerienId serie) throws SaveException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SERIEN_ID);
            if(serie != null){
                preparedStatement.setString(1, serie.getId().toString());
            }else{
                preparedStatement.setNull(1, Types.NULL);
            }
            preparedStatement.setString(2, event.getId().toString());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new SaveException(e);
        }
    }

    @Override
    public void setTitel(EventId event, String titel) throws SaveException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_TITEL);
            preparedStatement.setString(1, titel);
            preparedStatement.setString(2, event.getId().toString());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new SaveException(e);
        }
    }

    @Override
    public void setBeschreibung(EventId event, String beschreibung) throws SaveException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BESCHREIBUNG);
            preparedStatement.setString(1, beschreibung);
            preparedStatement.setString(2, event.getId().toString());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new SaveException(e);
        }
    }

    @Override
    public void setSichtbarkeit(EventId event, Sichtbarkeit sichtbarkeit) throws SaveException {
        try{
            connection.setAutoCommit(false);
            PreparedStatement preparedStatementDropOld = connection.prepareStatement(DELETE_EVENT_FROM_SICHTBARKEIT);
            preparedStatementDropOld.setString(1, event.getId().toString());
            preparedStatementDropOld.execute();
            PreparedStatement preparedStatementSetSichtbarkeitInEvent = connection.prepareStatement(UPDATE_SICHTBARKEIT);
            preparedStatementSetSichtbarkeitInEvent.setString(2, event.getId().toString());
            if(sichtbarkeit instanceof PublicSichtbarkeit){
                preparedStatementSetSichtbarkeitInEvent.setString(1, SICHTBARKEIT_PUBLIC);
            }else if(sichtbarkeit instanceof PrivateSichtbarkeit){
                preparedStatementSetSichtbarkeitInEvent.setString(1, SICHTBARKEIT_PRIVAT);
                savePrivateSichtbarkeit(event, (PrivateSichtbarkeit) sichtbarkeit);
            }
            preparedStatementSetSichtbarkeitInEvent.execute();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new SaveException(e);
        }
    }


    private void saveZeitraum(EventId eventId, Zeitraum zeitraum) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ZEITRAUM);
        preparedStatement.setString(1, eventId.getId().toString());
        preparedStatement.setTimestamp(2, Timestamp.from(zeitraum.getStart().toInstant()));
        preparedStatement.setTimestamp(3, Timestamp.from(zeitraum.getEnde().toInstant()));
        preparedStatement.execute();
    }

    private Zeitraum getZeitraum(EventId eventId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_ZEITRAUM);
        preparedStatement.setString(1, eventId.getId().toString());
        ResultSet results = preparedStatement.executeQuery();
        if(results.next()){
            Date start = new Date(Long.parseLong(results.getString("Start")));
            Date ende = new Date(Long.parseLong(results.getString("Ende")));
            return new Zeitraum(start, ende);
        }
        throw new SQLException("Kein Eintrag in Termine fuer EventId " + eventId.getId().toString());
    }

    private void saveMachbar(EventId id, boolean getan, BenutzerId von) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_MACHBAR);
        preparedStatement.setString(1, id.getId().toString());
        if(getan){
            preparedStatement.setInt(2, 1);
            preparedStatement.setString(3, von.getId().toString());
        } else {
            preparedStatement.setInt(2, 0);
            preparedStatement.setNull(3, Types.NULL);
        }
        preparedStatement.execute();
    }

    private Machbar getMachbar(EventId id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_MACHBAR);
        preparedStatement.setString(1, id.getId().toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            boolean getan = resultSet.getInt("Gemacht") == 1;
            BenutzerId von = null;
            if(getan){
                von = new BenutzerId(UUID.fromString(resultSet.getString("Von")));
            }
            BenutzerId finalVon = von;
            return new Machbar() {

                @Override
                public boolean istGetan() {
                    return getan;
                }

                @Override
                public Optional<BenutzerId> wurdeGemachtVon() {
                    return Optional.ofNullable(finalVon);
                }

                @Override
                public void setGetan(BenutzerId von, boolean zu) {

                }
            };
        }
        throw new SQLException("Kein Machbar gefunden fuer Id " + id.getId().toString());
    }

    private void saveDeadline(EventId id, Date deadline) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DEADLINE);
        preparedStatement.setString(1, id.getId().toString());
        preparedStatement.setTimestamp(2, Timestamp.from(deadline.toInstant()));
        preparedStatement.execute();
    }

    private Date getDeadline(EventId id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_DEADLINE);
        preparedStatement.setString(1, id.getId().toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            return new Date(Long.parseLong(resultSet.getString("Deadline")));
        }
        throw new SQLException("Keine Deadline fuer Event gefunden " + id.getId().toString());
    }

    private void savePrivateSichtbarkeit(EventId event, PrivateSichtbarkeit sichtbarkeit) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SICHTBARKEIT);
        for(BenutzerId benutzerId: sichtbarkeit.getSichtbarFuer()){
            preparedStatement.setString(1, event.getId().toString());
            preparedStatement.setString(2, benutzerId.getId().toString());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
    }

    private Sichtbarkeit getPrivateSichtbarkeit(EventId eventId) throws SQLException {
        Sichtbarkeit sichtbarkeit;
        PreparedStatement sichtbarkeitsQuery = connection.prepareStatement(GET_SICHTBARKEIT);
        sichtbarkeitsQuery.setString(1, eventId.getId().toString());
        ResultSet resultsSichtbarkeit = sichtbarkeitsQuery.executeQuery();
        Set<BenutzerId> sichtbarFuer = new HashSet<>();
        while(resultsSichtbarkeit.next()){
            sichtbarFuer.add(new BenutzerId(UUID.fromString(resultsSichtbarkeit.getString("BenutzerId"))));
        }
        sichtbarkeit = new PrivateSichtbarkeit(sichtbarFuer);
        return sichtbarkeit;
    }

    private Event getEventFromResultSet(ResultSet resultsEvent) throws SQLException {
        EventId eventId = new EventId(UUID.fromString(resultsEvent.getString("EventId")));
        String titel = resultsEvent.getString("Titel");
        HerkunftId herkunft = new HerkunftId(UUID.fromString(resultsEvent.getString("HerkunftId")));
        Sichtbarkeit sichtbarkeit;
        if(resultsEvent.getString("Sichtbarkeit").equals(SICHTBARKEIT_PUBLIC)){
            sichtbarkeit = new PublicSichtbarkeit();
        }else{
            sichtbarkeit = getPrivateSichtbarkeit(eventId);
        }
        String beschreibung = resultsEvent.getString("Beschreibung");
        String serienIdStr = resultsEvent.getString("SerienId");
        SerienId serienId;
        try{
            serienId = new SerienId(UUID.fromString(serienIdStr));
        }catch(IllegalArgumentException | NullPointerException e){
            serienId = null;
        }
        switch (resultsEvent.getString("Typ")){
            case TYP_TERMIN -> {
                Zeitraum zeitraum = getZeitraum(eventId);
                return new Termin(eventId, titel, herkunft, sichtbarkeit, beschreibung, serienId, zeitraum);
            }
            case TYP_AUFGABE -> {
                Machbar machbar = getMachbar(eventId);
                Date deadline = getDeadline(eventId);
                Aufgabe aufgabe = new Aufgabe(eventId, titel, herkunft, sichtbarkeit, beschreibung, serienId, deadline);
                if(machbar.istGetan()){
                    aufgabe.setGetan(machbar.wurdeGemachtVon().get(), machbar.istGetan());
                }
                return aufgabe;
            }
            case TYP_GEPLANTE_AUFGABE ->{
                Machbar machbar = getMachbar(eventId);
                Zeitraum zeitraum = getZeitraum(eventId);
                GeplanteAufgabe geplanteAufgabe = new GeplanteAufgabe(eventId, titel, herkunft, sichtbarkeit, beschreibung, serienId, zeitraum);
                if(machbar.istGetan()){
                    geplanteAufgabe.setGetan(machbar.wurdeGemachtVon().get(), machbar.istGetan());
                }
                return geplanteAufgabe;
            }
        }
        return null;
    }

}
