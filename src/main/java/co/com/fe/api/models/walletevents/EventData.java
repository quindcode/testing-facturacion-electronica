package co.com.fe.api.models.walletevents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventData {
    private String walletId;
    private String pocketId;
    private String pocketName;
    private String transactionId;
    private String uniqueTransactionId;
    private Double balance;
    private String currencyCode;
    private String movementDescription;
    private String transactionType;
    private String createdDate;
    private String externalReferenceId;
    private String subAccountId;
    private ExtendedAttributes extendedAttributes; // Puede ser null
    private Double amount;
    private String flyKey;
    private String flyKeyType;
    private int attentionPointId;
    private int consumptionType;
    private String externalTransactionId;
}
