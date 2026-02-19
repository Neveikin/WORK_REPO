package org.example.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseConection {
    private String URL;
    private String user;
    private String psw;

    public DataBaseConection(String URL, String user, String psw) {
        this.URL = URL;
        this.user = user;
        this.psw = psw;
    }

    public Connection getConnetcion(String URL, String user, String psw) throws Exception {
        Connection connection = DriverManager.getConnection(URL, user, psw);
        return connection;
    }

    public Connection connectToMyDatabase() throws SQLException {
        String user = "marketplace_user";
        String psw = "ilyanev2008";

        String url = "jdbc:postgresql://localhost:5432/mydatabase";
        Connection connection = null;
        
        try {
            connection = getConnetcion(url, user, psw);
            if (connection != null && !connection.isClosed()) {
                System.out.println("Соединение с базой данных установлено");
                System.out.println(connection.isValid(10));
                return connection;
            } else {
                throw new SQLException("Соединение не установлено");
            }
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void makeQuery(Connection connection, String sql) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("Запрос выполнен");
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
}
