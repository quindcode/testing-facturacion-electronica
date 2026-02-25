package co.com.fe.api.assertions;

import co.com.fe.api.models.dbmodels.InvoiceAudit;
import org.assertj.core.api.SoftAssertions;
import java.util.List;

public class InvoiceAuditVerifier {
    public static void assertTrail(List<InvoiceAudit> audits) {
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(audits)
                .as("Cantidad de registros de auditoría")
                .hasSize(3);

        if (audits == null || audits.isEmpty()) {
            softly.assertAll();
            return;
        }

        softly.assertThat(audits)
                .as("Secuencia de Estados")
                .extracting("newStatus")
                .containsExactly("PENDING", "ACCEPTED_IN_PROCESS", "ISSUED");

        for (InvoiceAudit audit : audits) {
            String status = audit.getNewStatus();

            if ("PENDING".equals(status)) {
                softly.assertThat(audit.getPayloadCommand())
                        .as("Payload Command está presente")
                        .isNotNull()
                        .isNotEmpty();
            }

            else if ("ACCEPTED".equals(status)) {
                softly.assertThat(audit.getRequest())
                        .as("Request (API) está presente")
                        .isNotNull();
                softly.assertThat(audit.getResponse())
                        .as("Response (API) está presente")
                        .isNotNull();
            }

            else if ("ISSUED".equals(status)) {
                softly.assertThat(audit.getRequest())
                        .as("Request (Webhook) está presente")
                        .isNotNull();
            }
        }

        softly.assertAll();
    }
}
