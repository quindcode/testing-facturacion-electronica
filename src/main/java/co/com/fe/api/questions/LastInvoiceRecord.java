package co.com.fe.api.questions;

import co.com.fe.api.utils.DatabaseClient;
import co.com.fe.api.models.dbmodels.Invoice;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

import java.sql.SQLException;

public class LastInvoiceRecord implements Question<Invoice> {
    private final String subAccountId;
    private final String uniqueTransactionId;

    private LastInvoiceRecord(String subAccountId, String uniqueTransactionId) {
        this.subAccountId = subAccountId;
        this.uniqueTransactionId = uniqueTransactionId;
    }

    public static LastInvoiceRecord withSubAccountId(String subAccountId){
        return new LastInvoiceRecord(subAccountId, null);
    }

    public static LastInvoiceRecord withUniqueTransactionId(String uniqueTransactionId){
        return new LastInvoiceRecord(null, uniqueTransactionId);
    }

    @Override
    public Invoice answeredBy(Actor actor) {
        try {
            var invoiceRepository = DatabaseClient.getInstance().invoiceRepository();

            if (subAccountId != null) {
                return invoiceRepository.findLastBySubAccountId(subAccountId);
            }
            return invoiceRepository.findInvoiceByUniqueTransactionId(uniqueTransactionId);

        } catch (SQLException e) {
            throw new RuntimeException("Fallo al consultar la factura en la DB", e);
        }
    }
}
