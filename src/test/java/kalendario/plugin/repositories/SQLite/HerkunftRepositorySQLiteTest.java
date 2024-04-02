package kalendario.plugin.repositories.SQLite;

import kalendario.application.herkunft.CommandLineHerkunft;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.repositories.HerkunftRepository;
import kalendario.domain.repositories.SaveException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HerkunftRepositorySQLiteTest {
    static Connection connection;
    @BeforeAll
    static void initializeConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:herkunftrepositorytest.db");
        System.out.println("connection initialized");
    }

    @AfterAll
    static void deleteConnection() throws SQLException {
        connection.close();
        boolean result = new File("herkunftrepositorytest.db").delete();
        System.out.println("Temp Database deleted: " + result);
    }

    HerkunftRepository herkunftRepository;
    Herkunft herkunft;
    BenutzerId benutzerId;
    HerkunftId herkunftId;

    @BeforeEach
    void init() throws SQLException {
        herkunftRepository = new HerkunftRepositorySQLite(connection);
        benutzerId = new BenutzerId(UUID.randomUUID());
        herkunftId = herkunftRepository.neueId();
        herkunft = new CommandLineHerkunft(benutzerId, herkunftId);
    }

    @Test
    void createUndRead() throws SaveException {
        herkunftRepository.saveHerkunft(herkunft);
        Herkunft actualHerkunft = herkunftRepository.getHerkunftMitId(herkunftId);
        assertEquals(herkunft, actualHerkunft);
        List<Herkunft> herkuenfte = herkunftRepository.getHerkuenfteVonBesitzer(benutzerId);
        assertEquals(1, herkuenfte.size());
        assertEquals(herkunft, herkuenfte.get(0));
    }


}
