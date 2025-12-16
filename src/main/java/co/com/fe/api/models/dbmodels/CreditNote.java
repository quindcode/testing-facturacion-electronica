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
public class CreditNote {
    private String id;
    private String invoiceId;
    private String uniqueTransactionId;
    private String cude;
    private String description;
    private Timestamp createdAt;
    private String createdBy;
    private String payloadCommand;
    private String request;
    private String response;
}