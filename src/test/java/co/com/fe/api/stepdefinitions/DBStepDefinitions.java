package co.com.fe.api.stepdefinitions;

import co.com.fe.api.assertions.InvoiceAuditVerifier;
import co.com.fe.api.models.dbmodels.InvoiceAudit;
import co.com.fe.api.models.kafka.walletevents.WalletEvent;
import co.com.fe.api.questions.InvoiceAuditRecords;
import co.com.fe.api.questions.LastInvoiceRecord;
import co.com.fe.api.tasks.WaitUntilInvoiceStatus;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actors.OnStage;

import java.util.List;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.*;

public class DBStepDefinitions {

    @Then("I can see the invoice in billing database with status {string} or {string}")
    public void iCanSeeTheInvoiceInBillingDatabaseWithStatusOr(String status1, String status2) {
        WalletEvent walletEvent = OnStage.theActorInTheSpotlight().recall("event");
        String uniqueTransactionId = walletEvent.getBody().getEventData().getUniqueTransactionId();

        OnStage.theActorInTheSpotlight().should(
                seeThat("el estado de la factura",
                        LastInvoiceRecord.withUniqueTransactionId(uniqueTransactionId),
                        hasProperty("status", is(oneOf(status1, status2)))));
    }

    @When("I wait until the invoice for has status {string}")
    public void iWaitUntilTheInvoiceForUniqueIdHasStatus(String invoiceStatus) {
        WalletEvent event = OnStage.theActorInTheSpotlight().recall("event");
        String uniqueTransactionId = event.getBody().getEventData().getUniqueTransactionId();
        OnStage.theActorInTheSpotlight().attemptsTo(
                WaitUntilInvoiceStatus.is(invoiceStatus).forUniqueId(uniqueTransactionId));
    }

    @Then("I can verify the audit trail contains the expected transitions")
    public void iCanVerifyTheAuditTrailContainsTheExpectedTransitions() {
        WalletEvent event = OnStage.theActorInTheSpotlight().recall("event");
        String uniqueTransactionId = event.getBody().getEventData().getUniqueTransactionId();

        List<InvoiceAudit> invoiceAudits = OnStage.theActorInTheSpotlight().asksFor(
                InvoiceAuditRecords.forUniqueTransactionId(uniqueTransactionId));

        InvoiceAuditVerifier.assertTrail(invoiceAudits);
    }
}
