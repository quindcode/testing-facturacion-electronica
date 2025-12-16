package co.com.fe.api.tasks;

import co.com.fe.api.models.dbmodels.Invoice;
import co.com.fe.api.questions.LastInvoiceRecord;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import org.awaitility.Awaitility;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class WaitUntilInvoiceStatus implements Interaction {
    private final String id;
    private final String expectedStatus;
    private final boolean searchByUniqueId;

    public WaitUntilInvoiceStatus(String id, String expectedStatus, Boolean searchByUniqueId) {
        this.id = id;
        this.expectedStatus = expectedStatus;
        this.searchByUniqueId = searchByUniqueId;
    }

    public static WaitUntilInvoiceStatus is(String expectedStatus) {
        return instrumented(WaitUntilInvoiceStatus.class, "", expectedStatus, true);
    }

    public WaitUntilInvoiceStatus forSubAccount(String subAccountId){
        return instrumented(WaitUntilInvoiceStatus.class, subAccountId, this.expectedStatus, false);
    }

    public WaitUntilInvoiceStatus forUniqueId(String uniqueTransactionId){
        return instrumented(WaitUntilInvoiceStatus.class, uniqueTransactionId, this.expectedStatus, true);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        Awaitility.await()
                .alias("Esperando que la factura llegue al estado: " + expectedStatus)
                .atMost(60, TimeUnit.SECONDS)
                .pollInterval(5, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until(
                        () -> {
                            Invoice invoice;
                            if(searchByUniqueId){
                                invoice = LastInvoiceRecord.withUniqueTransactionId(id).answeredBy(actor);
                            }else{
                                invoice = LastInvoiceRecord.withSubAccountId(id).answeredBy(actor);
                            }
                            String status = invoice != null ? invoice.getStatus() : "NOT_FOUND";

                            return Objects.equals(expectedStatus, status);
                        }
                );
    }
}
