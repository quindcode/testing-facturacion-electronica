package co.com.fe.api.utils.database;

import co.com.fe.api.models.dbmodels.Invoice;
import co.com.fe.api.models.dbmodels.InvoiceAudit;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class InvoiceRepository {
    private final Connection connection;
    private final QueryRunner run;
    private final RowProcessor rowProcessor;

    private final String INVOICE_TABLE = "toll_invoice_db.billing.invoice";
    private final String INVOICE_AUDIT_TABLE = "toll_invoice_db.billing.invoice_audit";

    public InvoiceRepository(Connection connection) {
        this.connection = connection;
        this.run = new QueryRunner();
        this.rowProcessor = new BasicRowProcessor(new GenerousBeanProcessor());
    }

    public Invoice findLastBySubAccountId(String subAccountId) throws SQLException {
        String query = String.format("SELECT * FROM %s WHERE aggregate_root_id = ? " +
                "ORDER BY created_date DESC LIMIT 1", INVOICE_TABLE);
        return run.query(connection, query, new BeanHandler<>(Invoice.class, rowProcessor), subAccountId);
    }

    public Invoice findInvoiceByUniqueTransactionId(String txId) throws SQLException {
        String query = String.format("SELECT * FROM %s WHERE unique_transaction_id = ?", INVOICE_TABLE);
        return run.query(connection, query, new BeanHandler<>(Invoice.class, rowProcessor), txId);
    }

    public List<InvoiceAudit> findAuditTrailByUniqueTransactionId(String txId) throws SQLException {
        String query = String.format("SELECT ia.* FROM %s ia LEFT JOIN %s i ON ia.invoice_id = i.id " +
                        "WHERE i.unique_transaction_id = ? ORDER BY ia.changed_at ASC",
                INVOICE_AUDIT_TABLE, INVOICE_TABLE);
        return run.query(connection, query, new BeanListHandler<>(InvoiceAudit.class, rowProcessor), txId);
    }
}