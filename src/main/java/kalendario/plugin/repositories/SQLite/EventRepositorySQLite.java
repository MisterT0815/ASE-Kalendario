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
    private static final String INSERT_ZEITRAUM = "INSERT INTO Zeitraum (EventId, Start, Ende) VALUES (?, ?, ?);";
    private static final String INSERT_SICHTBARKEIT = "INSERT INTO Sichtbarkeit (EventId, BenutzerId) VALUES (?, ?);";
    private static final String GET_EVENT = "SELECT * FROM Events WHERE EventId = ?";
    private static final String GET_SICHTBARKEIT = "SELECT * FROM Sichtbarkeit WHERE EventId = ?";
    private static final String GET_TERMIN = "SELECT * FROM Zeitraum WHERE EventId = ?";


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
            this.saveEvent(termin, TYP_TERMIN);
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
            this.saveEvent(aufgabe, TYP_AUFGABE);
            this.saveDeadline(aufgabe.getId(), aufgabe.getDeadline());
            this.saveMachbar(aufgabe.getId(), aufgabe.istGetan(), aufgabe.wurdeGemachtVon());
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new SaveException(e);
        }
    }

    @Override
    public void saveGeplanteAufgabe(GeplanteAufgabe geplanteAufgabe) throws SaveException {

    }

    private void saveEvent(Event event, String typ) throws SQLException {
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
            if(resultsEvent.next()){
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
                        return new Termin(eventId,titel, herkunft, sichtbarkeit, beschreibung, serienId, zeitraum);
                    }
                    case TYP_AUFGABE -> {
                        Machbar machbar = getMachbar(eventId);
                        Date deadline = getDeadline(eventId);
                        Aufgabe aufgabe = new Aufgabe(eventId, titel, herkunft, sichtbarkeit, beschreibung, serienId, deadline);
                        if(machbar != null && machbar.istGetan()){
                            aufgabe.setGetan(machbar.wurdeGemachtVon().get(), machbar.istGetan());
                        }
                        return aufgabe;
                    }
                }
            }
            return null;
        } catch (SQLException e) {
            return null;
        }
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





    private void saveZeitraum(EventId eventId, Zeitraum zeitraum) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ZEITRAUM);
        preparedStatement.setString(1, eventId.getId().toString());
        preparedStatement.setTimestamp(2, Timestamp.from(zeitraum.getStart().toInstant()));
        preparedStatement.setTimestamp(3, Timestamp.from(zeitraum.getEnde().toInstant()));
        preparedStatement.execute();
    }

    private Zeitraum getZeitraum(EventId eventId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(GET_TERMIN);
        preparedStatement.setString(1, eventId.getId().toString());
        ResultSet results = preparedStatement.executeQuery();
        if(results.next()){
            Date start = new Date(Long.parseLong(results.getString("Start")));
            Date ende = new Date(Long.parseLong(results.getString("Ende")));
            return new Zeitraum(start, ende);
        }
        throw new SQLException("Kein Eintrag in Termine fuer EventId " + eventId.getId().toString());
    }

    private void saveMachbar(EventId id, boolean b, Optional<BenutzerId> benutzerId) {

    }

    private Machbar getMachbar(EventId id){
        return null;
    }

    private void saveDeadline(EventId id, Date deadline) {

    }

    private Date getDeadline(EventId id){
        return null;
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



}
