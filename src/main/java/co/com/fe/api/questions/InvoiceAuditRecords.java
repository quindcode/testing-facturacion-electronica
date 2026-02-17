package co.com.fe.api.questions;

import co.com.fe.api.utils.DatabaseClient;
import co.com.fe.api.models.dbmodels.InvoiceAudit;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

import java.sql.SQLException;
import java.util.List;

public class InvoiceAuditRecords implements Question<List<InvoiceAudit>> {
    private final String uniqueTransactionId;

    public InvoiceAuditRecords(String uniqueTransactionId) {
        this.uniqueTransactionId = uniqueTransactionId;
    }

    public static InvoiceAuditRecords forUniqueTransactionId(String uniqueTransactionId){
        return new InvoiceAuditRecords(uniqueTransactionId);
    }

    @Override
    public List<InvoiceAudit> answeredBy(Actor actor) {
        try {
            var invoiceRepository = DatabaseClient.getInstance().invoiceRepository();

            return invoiceRepository.findAuditTrailByUniqueTransactionId(uniqueTransactionId);

        } catch (SQLException e) {
            throw new RuntimeException("Fallo al consultar la factura en la DB", e);
        }
    }
}
