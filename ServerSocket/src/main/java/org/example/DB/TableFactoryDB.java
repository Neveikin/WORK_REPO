package org.example.DB;

import jdk.jshell.spi.SPIResolutionException;
import org.example.Kabinet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class TableFactoryDB{

    public void createTable(Connection connection) {
        try {
            DataBaseConection dbConnection = new DataBaseConection("", "", "");

            connection = dbConnection.connectToMyDatabase();
            dbConnection.makeQuery(connection,
                    "CREATE TABLE IF NOT EXISTS kabinets (" +
                            "id VARCHAR(50) PRIMARY KEY, " +
                            "places INTEGER NOT NULL, " +
                            "comp_places INTEGER NOT NULL" +
                            ")");

            connection.close();

        } catch (SQLException e) {
            System.err.println("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }

    public Kabinet createCab(Connection connection, Kabinet kabinet) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO \"kabinets\" (\"id\", \"places\", \"comp_places\") values (?,?,?)");
        preparedStatement.setString(1, kabinet.getId());
        preparedStatement.setInt(2, kabinet.getPlaces());
        preparedStatement.setInt(3, kabinet.getCompsPlaces());

        int row = preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println(String.format("Kол-вл вставленных = %d", row));
        return kabinet;
    }

    public void deleteCab(Connection connection, String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM \"kabinets\" WHERE \"id\" = ?");
        statement.setString(1, id);
        int row = statement.executeUpdate();
        statement.close();
        System.out.println(row);
    }

    public Kabinet readCab(Connection connection, String id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"kabinets\" WHERE \"id\" = ?");
        statement.setString(1, id);
        ResultSet resultSet = statement.executeQuery();
        Kabinet kabinet = new Kabinet();
        while (resultSet.next()) {
            kabinet.setId(resultSet.getString(1));
            kabinet.setPlaces(resultSet.getInt(2));
            kabinet.setCompsPlaces(resultSet.getInt(2));
        }
        resultSet.close();
        statement.close();
        return kabinet;
    }

    public Kabinet updateCab(Connection connection, Kabinet kabinet) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE \"kabinets\" SET \"places\" = ?, \"comp_places\" = ? WHERE \"id\" = ?");
        preparedStatement.setInt(1, kabinet.getPlaces());
        preparedStatement.setInt(2, kabinet.getCompsPlaces());
        preparedStatement.setString(3, kabinet.getId());

        int row = preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println(String.format("Kол-во обновленных = %d", row));
        return kabinet;
    }

    public void dropTable(Connection connection) {
        try {
            DataBaseConection dbConnection = new DataBaseConection("", "", "");

            connection = dbConnection.connectToMyDatabase();
            dbConnection.makeQuery(connection, "DROP TABLE IF EXISTS \"kabinets\"");

            connection.close();
            System.out.println("Таблица kabinets успешно удалена");

        } catch (SQLException e) {
            System.err.println("Ошибка при удалении таблицы: " + e.getMessage());
        }
    }

    public LinkedList<Kabinet> getAllCabinets(Connection connection) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"kabinets\"");
        ResultSet resultSet = statement.executeQuery();
        LinkedList<Kabinet> cabinets = new LinkedList<>();
        
        while (resultSet.next()) {
            Kabinet kabinet = new Kabinet();
            kabinet.setId(resultSet.getString(1));
            kabinet.setPlaces(resultSet.getInt(2));
            kabinet.setCompsPlaces(resultSet.getInt(2));
            cabinets.add(kabinet);
        }
        
        resultSet.close();
        statement.close();
        return cabinets;
    }


}
