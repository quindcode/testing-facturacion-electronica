package co.com.fe.api.utils;

import co.com.fe.api.utils.database.InvoiceRepository;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseClient {

    private static DatabaseClient instance;
    private final Connection connection;

    private DatabaseClient() throws SQLException {
        this.connection = DatabaseConnector.getConnection();
    }

    public static synchronized DatabaseClient getInstance() {
        if (instance == null) {
            try {
                instance = new DatabaseClient();
            } catch (SQLException e) {
                throw new RuntimeException("Error initializing DatabaseClient", e);
            }
        }
        return instance;
    }

    public InvoiceRepository invoiceRepository() {
        return new InvoiceRepository(connection);
    }
}
