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
    private static final String INSERT_SQL = "INSERT INTO Benutzer (BenutzerId, Name, Passwort) VALUES (?, ?, ?)";
    private static final String BENUTZER_EXITIERT_SQL = "SELECT COUNT(*) AS NumRows FROM Benutzer WHERE Name = ?";


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
        PreparedStatement preparedStatement = connection.prepareStatement(BENUTZER_EXITIERT_SQL);
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
    public boolean benutzerExistiert(String name, String passwort) {
        return false;
    }

    @Override
    public BenutzerId getIdOfName(String name) {
        return null;
    }

    @Override
    public void updatePasswortOf(BenutzerId benutzerId, String neuesPasswort) throws SaveException {

    }

    @Override
    public void updateNameOf(BenutzerId benutzerId, String neuerName) throws SaveException {

    }

    @Override
    public String getBenutzerNameOfId(BenutzerId benutzerId) {
        return null;
    }
}
