package kalendario.plugin.repositories.SQLite;

import kalendario.domain.entities.benutzer.Benutzer;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.repositories.BenutzerRepository;
import kalendario.domain.repositories.SaveException;

import java.sql.*;
import java.util.UUID;

public class BenutzerRepositorySQLite implements BenutzerRepository {

    private static final String CREATE_TABLE = """
                CREATE TABLE IF NOT EXISTS Benutzer (
                    BenutzerId VARCHAR(255) PRIMARY KEY,
                    Name VARCHAR(255),
                    Passwort VARCHAR(255)
                );
            """;
    private static final String INSERT_SQL = "INSERT INTO Benutzer (BenutzerId, Name, Passwort) VALUES (?, ?, ?);";
    private static final String BENUTZER_EXISTIERT_SQL = "SELECT COUNT(*) AS NumRows FROM Benutzer WHERE Name = ?;";
    private static final String BENUTZER_EXISTIERT_MIT_PASSWORT = "SELECT COUNT(*) AS NumBenutzer FROM Benutzer WHERE Name = ? AND Passwort = ?;";
    private static final String ID_OF_NAME = "SELECT BenutzerId FROM Benutzer WHERE Name = ? LIMIT 1;";
    private static final String UPDATE_PASSWORT = "UPDATE Benutzer SET Passwort = ? WHERE BenutzerId = ?";
    private static final String UPDATE_NAME = "UPDATE Benutzer SET Name = ? WHERE BenutzerId = ?";
    private static final String NAME_OF_ID = "SELECT Name FROM Benutzer WHERE BenutzerId = ? LIMIT 1;";

    Connection connection;
    public BenutzerRepositorySQLite(Connection connection) throws SQLException {
        this.connection = connection;
        Statement createTable = connection.createStatement();
        createTable.execute(CREATE_TABLE);
    }

    @Override
    public BenutzerId neueId() {
        return new BenutzerId(UUID.randomUUID());
    }

    @Override
    public boolean benutzerNameExistiert(String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(BENUTZER_EXISTIERT_SQL);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int numRows = resultSet.getInt("NumRows");
            return numRows >= 1;
        }
        return false;
    }

    @Override
    public void saveBenutzer(Benutzer benutzer) throws SaveException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL);
            preparedStatement.setString(1, benutzer.getId().getId().toString());
            preparedStatement.setString(2, benutzer.getName());
            preparedStatement.setString(3, benutzer.getPasswort());
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected != 1){
                throw new SQLException("Save Benutzer soll nur eine Reihe speichern, aber " + rowsAffected + " Reihen wurden geaendert");
            }
        } catch (SQLException e) {
            throw new SaveException(e);
        }
    }

    @Override
    public boolean benutzerExistiert(String name, String passwort) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(BENUTZER_EXISTIERT_MIT_PASSWORT);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, passwort);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int numRows = resultSet.getInt("NumBenutzer");
            return numRows >= 1;
        }
        return false;
    }

    @Override
    public BenutzerId getIdOfName(String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(ID_OF_NAME);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String id = resultSet.getString("BenutzerId");
            return new BenutzerId(UUID.fromString(id));
        }
        return null;
    }

    @Override
    public void updatePasswortOf(BenutzerId benutzerId, String neuesPasswort) throws SaveException {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PASSWORT);
            preparedStatement.setString(1, neuesPasswort);
            preparedStatement.setString(2, benutzerId.getId().toString());
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected != 1){
                throw new SQLException("Update soll nur eine Reihe aendern, aber " + rowsAffected + " Reihen wurden geaendert");
            }
        } catch (SQLException e) {
            throw new SaveException(e);
        }

    }

    @Override
    public void updateNameOf(BenutzerId benutzerId, String neuerName) throws SaveException {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_NAME);
            preparedStatement.setString(1, neuerName);
            preparedStatement.setString(2, benutzerId.getId().toString());
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected != 1){
                throw new SQLException("Update soll nur eine Reihe aendern, aber " + rowsAffected + " Reihen wurden geaendert");
            }
        } catch (SQLException e) {
            throw new SaveException(e);
        }
    }

    @Override
    public String getBenutzerNameOfId(BenutzerId benutzerId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(NAME_OF_ID);
        preparedStatement.setString(1, benutzerId.getId().toString());
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("Name");
        }
        return null;
    }
}
