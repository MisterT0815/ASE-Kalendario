package kalendario.plugin.repositories.SQLite;


import kalendario.domain.repositories.SerienRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @BeforeEach
    void init() throws SQLException {
        serienRepository = new SerienRepositorySQLite(connection);
    }

    @Test
    void createSerieUndRead(){
        assertTrue(true);
    }

}
