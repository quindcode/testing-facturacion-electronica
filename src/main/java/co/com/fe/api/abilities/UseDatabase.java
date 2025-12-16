package co.com.fe.api.abilities;

import co.com.fe.api.utils.DatabaseConnector;
import net.serenitybdd.screenplay.Ability;

import java.sql.Connection;
import java.sql.SQLException;

public class UseDatabase implements Ability {
    public Connection getConnection() throws SQLException {
        return DatabaseConnector.getConnection();
    }

    public static UseDatabase connect() {
        return new UseDatabase();
    }
}