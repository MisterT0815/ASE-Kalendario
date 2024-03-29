package kalendario.plugin.repositories.SQLite;

import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.event.*;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.entities.serie.SerienId;
import kalendario.domain.repositories.EventRepository;
import kalendario.domain.repositories.SaveException;
import kalendario.domain.value_objects.Zeitraum;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class EventRepositoryIntegrationTest {

    static Connection connection;

    @BeforeAll
    static void initializeConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:eventrepositorytest.db");
        System.out.println("connection initialized");
    }

    @AfterAll
    static void deleteConnection() throws SQLException {
        connection.close();
        boolean result = new File("eventrepositorytest.db").delete();
        System.out.println("Temp Database deleted: " + result);
    }

    EventRepository eventRepository;
    PrivateSichtbarkeit privateSichtbarkeit;
    Zeitraum zeitraum;
    Date date1;
    Date date2;

    @BeforeEach
    void init() throws SQLException {
        eventRepository = new EventRepositorySQLite(connection);
        Set<BenutzerId> sichtbareBenutzer = new HashSet();
        sichtbareBenutzer.add(new BenutzerId(UUID.randomUUID()));
        sichtbareBenutzer.add(new BenutzerId(UUID.randomUUID()));
        privateSichtbarkeit = new PrivateSichtbarkeit(sichtbareBenutzer);
        date1 = new Calendar.Builder().setDate(2000, 1, 15).setTimeOfDay(10, 20, 54).build().getTime();
        date2 = new Calendar.Builder().setDate(2000, 1, 15).setTimeOfDay(12, 20, 54).build().getTime();
        zeitraum = new Zeitraum(date1, date2);
    }

    @Test
    void createTerminUndRead() throws SaveException {
        Termin termin_1 = new Termin(eventRepository.neueId(), "Titel", new HerkunftId(UUID.randomUUID()), new PublicSichtbarkeit(), "Beschreibung", new SerienId(UUID.randomUUID()), new Zeitraum(new Date(1000L), new Date(2000L)));
        Termin termin_2 = new Termin(eventRepository.neueId(), "Titel2", new HerkunftId(UUID.randomUUID()), privateSichtbarkeit, "Beschreibung2", zeitraum);
        eventRepository.saveTermin(termin_1);
        eventRepository.saveTermin(termin_2);
        Event event_1 = eventRepository.getEvent(termin_1.getId());
        Event event_2 = eventRepository.getEvent(termin_2.getId());
        assertInstanceOf(Termin.class, event_1);
        assertInstanceOf(Termin.class, event_2);
        equalsEvent(termin_1, event_1);
        equalsEvent(termin_2, event_2);
        assertEquals(termin_1.getZeitraum(), ((Termin) event_1).getZeitraum());
        assertEquals(termin_2.getZeitraum(), ((Termin) event_2).getZeitraum());
    }

    @Test
    void createAufgabeUndRead() throws SaveException {
        Aufgabe aufgabe1 = new Aufgabe(eventRepository.neueId(), "Titel", new HerkunftId(UUID.randomUUID()), new PublicSichtbarkeit(), "Beschreibung1", new Date(1000L));
        Aufgabe aufgabe2 = new Aufgabe(eventRepository.neueId(), "Titel2", new HerkunftId(UUID.randomUUID()), privateSichtbarkeit, "Beschreibung", new SerienId(UUID.randomUUID()), date1);
        BenutzerId getanVon = new BenutzerId(UUID.randomUUID());
        aufgabe2.setGetan(getanVon, true);
        eventRepository.saveAufgabe(aufgabe1);
        eventRepository.saveAufgabe(aufgabe2);
        Event event1 = eventRepository.getEvent(aufgabe1.getId());
        Event event2 = eventRepository.getEvent(aufgabe2.getId());
        assertInstanceOf(Aufgabe.class, event1);
        assertInstanceOf(Aufgabe.class, event2);
        equalsEvent(aufgabe1, event1);
        equalsEvent(aufgabe2, event2);
        Aufgabe actual_aufgabe1 = (Aufgabe) event1;
        Aufgabe actual_aufgabe2 = (Aufgabe) event2;
        assertEquals(aufgabe1.istGetan(), actual_aufgabe1.istGetan());
        assertEquals(aufgabe2.istGetan(), actual_aufgabe2.istGetan());
        assertEquals(aufgabe1.wurdeGemachtVon(), actual_aufgabe1.wurdeGemachtVon());
        assertEquals(aufgabe2.wurdeGemachtVon(), actual_aufgabe2.wurdeGemachtVon());
        assertEquals(aufgabe1.getDeadline(), actual_aufgabe1.getDeadline());
        assertEquals(aufgabe2.getDeadline(), actual_aufgabe2.getDeadline());
    }


    void equalsEvent(Event expected, Event actual){
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitel(), actual.getTitel());
        assertEquals(expected.getHerkunftId(), actual.getHerkunftId());
        assertEquals(expected.getBeschreibung(), actual.getBeschreibung());
        assertEquals(expected.getSerienId(), actual.getSerienId());
        if(expected.getSichtbarkeit() instanceof PublicSichtbarkeit){
            assertInstanceOf(PublicSichtbarkeit.class, actual.getSichtbarkeit());
        }else if(expected.getSichtbarkeit() instanceof PrivateSichtbarkeit){
            assertEquals(((PrivateSichtbarkeit) expected.getSichtbarkeit()).getSichtbarFuer(), ((PrivateSichtbarkeit) actual.getSichtbarkeit()).getSichtbarFuer());
        }
    }


}
