package kalendario;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kalendario.application.crud.benutzer.BenutzerCreation;
import kalendario.application.crud.benutzer.BenutzerRead;
import kalendario.application.crud.benutzer.BenutzerUpdate;
import kalendario.application.crud.event.EventCreation;
import kalendario.application.crud.event.EventRead;
import kalendario.application.crud.event.EventUpdate;
import kalendario.application.crud.event.SerienEventAnpassung;
import kalendario.application.crud.herkunft.HerkunftCreate;
import kalendario.application.crud.herkunft.HerkunftRead;
import kalendario.application.crud.serie.SerieCreation;
import kalendario.application.crud.serie.SerieRead;
import kalendario.application.crud.sicherheit.LeseZugriffVerfizierer;
import kalendario.application.crud.sicherheit.SchreibZugriffVerifizierer;
import kalendario.application.session.Session;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.repositories.BenutzerRepository;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.HerkunftRepository;
import kalendario.domain.repositories.SerienRepository;
import kalendario.plugin.gson.GsonDurationDeserialzier;
import kalendario.plugin.gson.GsonOptionalDeserializer;
import kalendario.plugin.herkunft.simpleCommandLine.Command;
import kalendario.plugin.herkunft.simpleCommandLine.SimpleCommandLine;
import kalendario.plugin.herkunft.simpleCommandLine.commands.*;
import kalendario.plugin.repositories.SQLite.BenutzerRepositorySQLite;
import kalendario.plugin.repositories.SQLite.EventRepositorySQLite;
import kalendario.plugin.repositories.SQLite.HerkunftRepositorySQLite;
import kalendario.plugin.repositories.SQLite.SerienRepositorySQLite;

import java.io.IOException;
import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws SQLException, IOException {
        //DB Connection
        Connection conn = DriverManager.getConnection("jdbc:sqlite:sample.db");

        //Repositories
        BenutzerRepository benutzerRepository = new BenutzerRepositorySQLite(conn);
        EventRepository eventRepository = new EventRepositorySQLite(conn);
        SerienRepository serienRepository = new SerienRepositorySQLite(conn);
        HerkunftRepository herkunftRepository = new HerkunftRepositorySQLite(conn);

        //Security
        BenutzerRead benutzerRead = new BenutzerRead(benutzerRepository);
        BenutzerCreation benutzerCreation = new BenutzerCreation(benutzerRepository);

        Session session = new Session(benutzerRead, benutzerCreation);
        LeseZugriffVerfizierer leseZugriffVerfizierer = new LeseZugriffVerfizierer(session, eventRepository, serienRepository, herkunftRepository);
        SchreibZugriffVerifizierer schreibZugriffVerifizierer = new SchreibZugriffVerifizierer(session, eventRepository, serienRepository, herkunftRepository);

        //Use Cases
        BenutzerUpdate benutzerUpdate = new BenutzerUpdate(benutzerRepository, session);

        EventRead eventRead = new EventRead(eventRepository, leseZugriffVerfizierer);
        EventCreation eventCreation = new EventCreation(eventRepository, session, schreibZugriffVerifizierer);
        EventUpdate eventUpdate = new EventUpdate(eventRepository, eventRead, benutzerRead, schreibZugriffVerifizierer);

        SerieRead serieRead = new SerieRead(serienRepository, leseZugriffVerfizierer);
        SerieCreation serieCreation = new SerieCreation(serienRepository, eventRepository, schreibZugriffVerifizierer);
        SerienEventAnpassung serienEventAnpassung = new SerienEventAnpassung(eventRepository, serieRead, serienRepository, eventRead, session, schreibZugriffVerifizierer);

        HerkunftRead herkunftRead = new HerkunftRead(herkunftRepository);
        HerkunftCreate herkunftCreate = new HerkunftCreate(herkunftRepository, session);

        //Commandline
        GsonOptionalDeserializer deserializer = new GsonOptionalDeserializer();
        GsonDurationDeserialzier durationDeserialzier = new GsonDurationDeserialzier();
        Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(Optional.class, deserializer).registerTypeAdapter(Duration.class, durationDeserialzier).serializeNulls().create();
        List<Command> commandList = new ArrayList<>();
        commandList.add(new Login(session));
        commandList.add(new Signup(session));
        commandList.add(new Logout(session));
        commandList.add(new UserInfo(session));
        commandList.add(new EventCreate(eventCreation, herkunftCreate));
        commandList.add(new AlleEventsVonHier(eventRead, herkunftCreate));
        commandList.add(new EventInfo(eventRead, gson));
        commandList.add(new SerieInfo(serieRead, gson));
        commandList.add(new SerieCreate(serieCreation));
        commandList.add(new AddEventToSerie(serienEventAnpassung, herkunftCreate));
        commandList.add(new SerienEventsInZeitraum(serieRead, eventRead, gson));
        SimpleCommandLine simpleCommandLine = new SimpleCommandLine(session, commandList);
        simpleCommandLine.run();
    }
}