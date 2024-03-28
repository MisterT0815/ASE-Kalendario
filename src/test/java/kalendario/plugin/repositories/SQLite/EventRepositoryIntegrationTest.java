package kalendario.plugin.repositories.SQLite;

import kalendario.domain.repositories.EventRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
        System.out.println("connection dead: " + result);
    }

    EventRepository eventRepository;

    @BeforeEach
    void init() throws SQLException {
        eventRepository = new EventRepositorySQLite(connection);
    }

    @Test
    void createEventUndRead(){
        eventRepository.neueId();
    }


}
