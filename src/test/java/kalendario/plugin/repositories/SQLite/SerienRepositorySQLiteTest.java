package kalendario.plugin.repositories.SQLite;


import kalendario.application.wiederholung.ZeitlicherAbstandWiederholung;
import kalendario.domain.entities.event.EventId;
import kalendario.domain.entities.serie.Serie;
import kalendario.domain.entities.serie.Wiederholung;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.repositories.SerienRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class SerienRepositorySQLiteTest {
    static Connection connection;

    @BeforeAll
    static void initializeConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:serienrepositorytest.db");
        System.out.println("connection initialized");
    }

    @AfterAll
    static void deleteConnection() throws SQLException {
        connection.close();
        boolean result = new File("serienrepositorytest.db").delete();
        System.out.println("Temp Database deleted: " + result);
    }

    SerienRepository serienRepository;
    Date start;
    Map<Date, EventId> angepassteEvents;
    EventId defaultEventId;
    ZeitlicherAbstandWiederholung wiederholung;


    @BeforeEach
    void init() throws SQLException {
        serienRepository = new SerienRepositorySQLite(connection);
        start = new Date(1000L);
        angepassteEvents = new HashMap<>();
        for(int i = 1; i<=3; i++){
            angepassteEvents.put(
                    new Date(4000*i),
                    new EventId(UUID.randomUUID())
            );
        }
        defaultEventId = new EventId(UUID.randomUUID());
        wiederholung = new ZeitlicherAbstandWiederholung(Duration.ofHours(1200), start);
    }

    @Test
    void createSerieUndRead() throws SaveException {
        Serie serie = new Serie(serienRepository.neueId(), defaultEventId, start, wiederholung);
        serienRepository.saveSerie(serie);
        Serie actualSerie = serienRepository.getSerie(serie.getId());
        assertEquals(serie.getDefaultEvent(), actualSerie.getDefaultEvent());
        assertEquals(serie.getStart(), actualSerie.getStart());
        assertInstanceOf(ZeitlicherAbstandWiederholung.class, serie.getWiederholung());
        assertEquals(wiederholung.getAbstand(), ((ZeitlicherAbstandWiederholung)actualSerie.getWiederholung()).getAbstand());
    }

    @Test
    void addUndRemoveAngepassteEvents() throws SaveException{
        Serie serie = new Serie(serienRepository.neueId(), defaultEventId, start, wiederholung);
        serienRepository.saveSerie(serie);
        Date angepasstesEventDate = wiederholung.naechsterZeitpunktAb(start);
        EventId angepasstesEvent = new EventId(UUID.randomUUID());
        serie.changeEventAnZeitpunkt(angepasstesEventDate, angepasstesEvent);
        serienRepository.addAngepasstesEvent(serie.getId(), angepasstesEventDate, angepasstesEvent);
        Serie actualSerie = serienRepository.getSerie(serie.getId());
        assertEquals(serie.getAngepassteEventIds(), actualSerie.getAngepassteEventIds());
        serienRepository.removeAngepasstesEvent(serie.getId(), angepasstesEventDate);
        actualSerie = serienRepository.getSerie(serie.getId());
        assertTrue(actualSerie.getAngepassteEventIds().isEmpty());
    }


}
