package co.com.fe.api.abilities;

import co.com.fe.api.utils.DatabaseConnector;
import co.com.fe.api.utils.database.InvoiceRepository;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.Actor;

import java.sql.Connection;
import java.sql.SQLException;

public class UseDatabase implements Ability {
private final Connection connection;

    private UseDatabase() throws SQLException {
        this.connection = DatabaseConnector.getConnection();
    }

    public static UseDatabase connect() {
        try {
            return new UseDatabase();
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo conectar a la base de datos", e);
        }
    }

    public static UseDatabase as(Actor actor) {
        return actor.abilityTo(UseDatabase.class);
    }

    // Punto de entrada a los repositorios
    public InvoiceRepository invoiceRepository() {
        return new InvoiceRepository(connection);
    }
}