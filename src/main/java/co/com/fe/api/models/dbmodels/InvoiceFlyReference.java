package co.com.fe.api.models.dbmodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceFlyReference {
    private String invoiceId;
    private String accountId;
    private String subAccountId;
    private Long attentionPointId;
    private String flyKey;
    private String flyKeyType;
    private String transactionId;
    private String externalReferenceId;
    private String externalTransactionId;
}