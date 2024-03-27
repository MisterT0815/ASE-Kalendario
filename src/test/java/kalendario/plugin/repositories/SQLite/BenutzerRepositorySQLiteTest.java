package kalendario.plugin.repositories.SQLite;

import kalendario.domain.entities.benutzer.Benutzer;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.repositories.BenutzerRepository;
import kalendario.domain.repositories.SaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class BenutzerRepositorySQLiteTest {

    UUID uId = UUID.randomUUID();
    BenutzerId id = mock();
    String name = "name";
    String passwort = "passwort";
    Benutzer benutzer = mock();
    Connection connection = mock();
    BenutzerRepository repository;
    Statement statement = mock();
    PreparedStatement preparedStatement = mock();

    @BeforeEach
    void init() throws SQLException {
        when(benutzer.getId()).thenReturn(id);
        when(benutzer.getName()).thenReturn(name);
        when(benutzer.getPasswort()).thenReturn(passwort);
        when(id.getId()).thenReturn(uId);
        when(connection.createStatement()).thenReturn(statement);
        when(connection.prepareStatement(any())).thenReturn(preparedStatement);
        repository = new BenutzerRepositorySQLite(connection);
    }

    @Test
    void repositoryErstellungSollTableCreationWennNichtExistiert() throws SQLException {
        verify(statement).execute(any());
    }

    @Test
    void saveBenutzerSollCreateStatementMitAttributenRufen() throws SQLException, SaveException {
        when(preparedStatement.executeUpdate()).thenReturn(1);
        repository.saveBenutzer(benutzer);
        verify(connection).prepareStatement(any());
        verify(preparedStatement).setString(1, uId.toString());
        verify(preparedStatement).setString(2, name);
        verify(preparedStatement).setString(3, passwort);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void saveBenutzerSollFehlschlagenBeiSQLExceptionOderZuVieleReihenVeraendert() throws SQLException {
        when(preparedStatement.executeUpdate()).thenReturn(0);
        assertThrows(SaveException.class, () -> repository.saveBenutzer(benutzer));
        when(preparedStatement.executeUpdate()).thenThrow(SQLException.class);
        assertThrows(SaveException.class, () -> repository.saveBenutzer(benutzer));
    }

    @Test
    void getIdSollUUIDErstellen(){
        try(MockedStatic mock = mockStatic(UUID.class)){
            repository.neueId();
            mock.verify(UUID::randomUUID);
        }
    }



}
