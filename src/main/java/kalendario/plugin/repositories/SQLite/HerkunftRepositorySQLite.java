package kalendario.plugin.repositories.SQLite;

import kalendario.application.herkunft.CommandLineHerkunft;
import kalendario.domain.entities.benutzer.BenutzerId;
import kalendario.domain.entities.herkunft.Herkunft;
import kalendario.domain.entities.herkunft.HerkunftId;
import kalendario.domain.repositories.BenutzerRepository;
import kalendario.domain.repositories.HerkunftRepository;
import kalendario.domain.repositories.SaveException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HerkunftRepositorySQLite implements HerkunftRepository {

    Connection connection;
    private static final String TYP_CLI = "CLI";
    private static final String CREATE_TABLE_CLI_HERKUNFT = """
            CREATE TABLE IF NOT EXISTS Herkunft (
                HerkunftId VARCHAR(255) PRIMARY KEY,
                BenutzerId VARCHAR(255),
                Typ VARCHAR(255),
                FOREIGN KEY (BenutzerId) REFERENCES Benutzer(BenutzerId) ON UPDATE CASCADE ON DELETE CASCADE
            );
            """;
    private static final String INSERT_HERKUNFT = "INSERT INTO Herkunft (HerkunftId, BenutzerId, Typ) VALUES (?, ?, ?);";
    private static final String GET_BY_HERKUNFT = "SELECT * FROM Herkunft WHERE HerkunftId = ?;";
    private static final String GET_BY_BENUTZER = "SELECT * FROM Herkunft WHERE BenutzerId = ?;";


    public HerkunftRepositorySQLite(Connection connection) throws SQLException {
        this.connection = connection;
        Statement createTable = connection.createStatement();
        createTable.execute(CREATE_TABLE_CLI_HERKUNFT);
    }

    @Override
    public HerkunftId neueId() {
        return new HerkunftId(UUID.randomUUID());
    }

    @Override
    public Herkunft getHerkunftMitId(HerkunftId herkunftId) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_HERKUNFT);
            preparedStatement.setString(1, herkunftId.getId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return extractHerkunftVonResultSet(resultSet);
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    @Override
    public List<Herkunft> getHerkuenfteVonBesitzer(BenutzerId besitzer) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_BENUTZER);
            preparedStatement.setString(1, besitzer.getId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Herkunft> herkuenfte = new ArrayList<>();
            while(resultSet.next()){
                herkuenfte.add(extractHerkunftVonResultSet(resultSet));
            }
            return herkuenfte;
        } catch (SQLException e) {
            return List.of();
        }
    }

    @Override
    public void saveHerkunft(Herkunft herkunft) throws SaveException {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_HERKUNFT);
            preparedStatement.setString(1, herkunft.getId().getId().toString());
            preparedStatement.setString(2, herkunft.getBesitzerId().getId().toString());
            if(herkunft instanceof CommandLineHerkunft){
                preparedStatement.setString(3, TYP_CLI);
            }else{
                throw new SQLException("Herkunft Typ nicht bekannt");
            }
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new SaveException(e);
        }
    }
    private static CommandLineHerkunft extractHerkunftVonResultSet(ResultSet resultSet) throws SQLException {
        BenutzerId benutzerId = new BenutzerId(UUID.fromString(resultSet.getString("BenutzerId")));
        HerkunftId herkunftId = new HerkunftId(UUID.fromString(resultSet.getString("HerkunftId")));
        String typ = resultSet.getString("Typ");
        if (typ.equals(TYP_CLI)) {
            return new CommandLineHerkunft(benutzerId, herkunftId);
        }
        return null;
    }


}
