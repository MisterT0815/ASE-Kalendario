package kalendario.plugin.repositories.SQLite;

import kalendario.application.crud.benutzer.BenutzerCreation;
import kalendario.application.crud.benutzer.BenutzerNameExistiertException;
import kalendario.application.crud.benutzer.BenutzerRead;
import kalendario.application.crud.benutzer.BenutzerUpdate;
import kalendario.application.session.KeinZugriffException;
import kalendario.application.session.Session;
import kalendario.domain.entities.benutzer.Benutzer;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.repositories.BenutzerRepository;
import kalendario.domain.repositories.SaveException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BenutzerRepositoryIntegrationTest {

    static Connection connection;


    @BeforeAll
    static void initializeConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:benutzerrepositorytest.db");
        System.out.println("connection initialized");
    }

    @AfterAll
    static void deleteConnection() throws SQLException {
        connection.close();
        boolean result = new File("benutzerrepositorytest.db").delete();
        System.out.println("connection dead: " + result);
    }

    BenutzerRepository benutzerRepository;
    BenutzerCreation benutzerCreation;
    BenutzerRead benutzerRead;
    BenutzerUpdate benutzerUpdate;
    Session session = mock();

    @BeforeEach
    void init(){
        try {
            benutzerRepository = new BenutzerRepositorySQLite(connection);
            benutzerCreation = new BenutzerCreation(benutzerRepository);
            benutzerRead = new BenutzerRead(benutzerRepository);
            benutzerUpdate = new BenutzerUpdate(benutzerRepository, session);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createBenutzerUndBenutzerRead() throws SaveException, BenutzerNameExistiertException {
        Benutzer benutzer = benutzerCreation.createBenutzer("Name1", "Passwort1");
        assertEquals("Name1", benutzer.getName());
        assertEquals("Passwort1", benutzer.getPasswort());
        assertNotNull(benutzer.getId());
        assertNotNull(benutzer.getId().getId());
        Optional<BenutzerId> maybeId = benutzerRead.getBenutzerIdOfName("Name1");
        assertTrue(maybeId.isPresent());
        assertEquals(benutzer.getId(), maybeId.get());
        Optional<String> maybeName = benutzerRead.getBenutzerNameOfId(benutzer.getId());
        assertTrue(maybeName.isPresent());
        assertEquals(benutzer.getName(), maybeName.get());
        assertTrue(benutzerRead.benutzerExistiert("Name1"));
    }

    @Test
    void benutzerUpdateName() throws SaveException, BenutzerNameExistiertException, KeinZugriffException, SQLException {
        Benutzer benutzer = benutzerCreation.createBenutzer("Name2", "Passwort2");
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer.getId()));
        when(session.getCurrentBenutzerName()).thenReturn(Optional.of(benutzer.getName()));
        benutzerUpdate.updateName("Name3");
        verify(session).logout();
        assertTrue(benutzerRead.verifyBenutzer("Name3", "Passwort2"));
        assertFalse(benutzerRead.benutzerExistiert("Name2"));
    }

    @Test
    void benutzerUpdatePasswort() throws SaveException, KeinZugriffException, SQLException, BenutzerNameExistiertException {
        Benutzer benutzer = benutzerCreation.createBenutzer("Name4", "Passwort4");
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer.getId()));
        when(session.getCurrentBenutzerName()).thenReturn(Optional.of(benutzer.getName()));
        benutzerUpdate.updatePasswort("Passwort4", "Passwort5");
        verify(session).logout();
        assertTrue(benutzerRead.verifyBenutzer("Name4", "Passwort5"));
    }

    @Test
    void benutzerNameClashSollNichtPassieren() throws SaveException, BenutzerNameExistiertException {
        benutzerCreation.createBenutzer("Name6", "Passwort6");
        assertThrows(BenutzerNameExistiertException.class, () -> benutzerCreation.createBenutzer("Name6", "p"));
        Benutzer benutzer = benutzerCreation.createBenutzer("Name7", "Passwort7");
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer.getId()));
        when(session.getCurrentBenutzerName()).thenReturn(Optional.of(benutzer.getName()));
        assertThrows(BenutzerNameExistiertException.class, () -> benutzerUpdate.updateName("Name6"));
    }

    @Test
    void passwortUpdateOhneRichtigesPasswortNichtMoeglich() throws SaveException, BenutzerNameExistiertException {
        Benutzer benutzer = benutzerCreation.createBenutzer("Name8", "Passwort8");
        when(session.getCurrentBenutzer()).thenReturn(Optional.of(benutzer.getId()));
        when(session.getCurrentBenutzerName()).thenReturn(Optional.of(benutzer.getName()));
        assertThrows(KeinZugriffException.class, () -> benutzerUpdate.updatePasswort("Falsch", "Neu"));
    }
}
