package co.com.fe.api.questions;

import co.com.fe.api.abilities.UseDatabase;
import co.com.fe.api.models.dbmodels.InvoiceAudit;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.util.List;

public class InvoiceAuditRecords implements Question<List<InvoiceAudit>> {
    private final String invoiceId;
    private final String uniqueTransactionId;

    public InvoiceAuditRecords(String invoiceId, String uniqueTransactionId) {
        this.invoiceId = invoiceId;
        this.uniqueTransactionId = uniqueTransactionId;
    }

    public static InvoiceAuditRecords forInvoiceId(String invoiceId){
        return new InvoiceAuditRecords(invoiceId, null);
    }

    public static InvoiceAuditRecords forUniqueTransactionId(String uniqueTransactionId){
        return new InvoiceAuditRecords(null, uniqueTransactionId);
    }

    @Override
    public List<InvoiceAudit> answeredBy(Actor actor) {
        UseDatabase ability = actor.abilityTo(UseDatabase.class);
        if (ability == null) {
            throw new IllegalStateException("Actor needs the UseDatabase ability.");
        }

        RowProcessor rowProcessor = new BasicRowProcessor(new GenerousBeanProcessor());
        QueryRunner run = new QueryRunner();

        String query;

        if(invoiceId != null && !invoiceId.isEmpty()){
            query = "SELECT * FROM toll_invoice_db.billing.invoice_audit " +
                "WHERE invoice_id = ? " +
                "ORDER BY changed_at ASC";
        }else if(uniqueTransactionId != null && !uniqueTransactionId.isEmpty()){
            query = "SELECT ia.* FROM toll_invoice_db.billing.invoice_audit ia " +
                    "LEFT JOIN toll_invoice_db.billing.invoice i ON ia.invoice_id = i.id " +
                    "WHERE i.unique_transaction_id = ? " +
                    "ORDER BY ia.changed_at ASC";
        }else{
            throw new RuntimeException("Se debe utilizar un atributo para consultar en base de datos");
        }

        try {
            Connection conn = ability.getConnection();

            if(invoiceId != null) {
                return run.query(conn, query, new BeanListHandler<>(InvoiceAudit.class, rowProcessor), invoiceId);
            }else{
                return run.query(conn, query, new BeanListHandler<>(InvoiceAudit.class, rowProcessor), uniqueTransactionId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error consultando auditoría para invoiceId: " + invoiceId, e);
        }
    }
}
