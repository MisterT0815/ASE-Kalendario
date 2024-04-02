package kalendario;

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
import kalendario.domain.repositories.BenutzerRepository;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.HerkunftRepository;
import kalendario.domain.repositories.SerienRepository;
import kalendario.plugin.herkunft.simpleCommandLine.SimpleCommandLine;
import kalendario.plugin.repositories.SQLite.BenutzerRepositorySQLite;
import kalendario.plugin.repositories.SQLite.EventRepositorySQLite;
import kalendario.plugin.repositories.SQLite.HerkunftRepositorySQLite;
import kalendario.plugin.repositories.SQLite.SerienRepositorySQLite;

import java.io.IOException;
import java.sql.*;

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
        SimpleCommandLine simpleCommandLine = new SimpleCommandLine(session);
        simpleCommandLine.run();
    }
}