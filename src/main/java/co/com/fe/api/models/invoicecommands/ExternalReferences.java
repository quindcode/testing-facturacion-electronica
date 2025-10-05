package co.com.fe.api.models.invoicecommands;

import lombok.Data;

@Data
public class ExternalReferences {
    private String accountId;
    private String subAccountId;
    private Integer attentionPointId;
    private String transactionId;
    private String uniqueTransactionId;
    private String rootUniqueTransactionId;
    private String externalReferenceId;
    private String rootExternalReferenceId;
    private String externalTransactionId;
}
