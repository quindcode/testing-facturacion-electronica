package co.com.fe.api.models.dbmodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceAudit {
    private String id;
    private String invoiceId;
    private String oldStatus;
    private String newStatus;
    private Timestamp changedAt;
    private String changedBy;
    private String payloadCommand;
    private String request;
    private String response;
}