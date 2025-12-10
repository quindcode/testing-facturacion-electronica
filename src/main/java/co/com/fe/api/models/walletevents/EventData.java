package co.com.fe.api.models.walletevents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventData {
    // Campos comunes
    private String walletId;
    private String pocketId;
    private String pocketName;
    private String transactionId;
    private String uniqueTransactionId;
    private BigDecimal balance;
    private String currencyCode;
    private String movementDescription;
    private String transactionType;
    private String createdDate;
    private String externalReferenceId;
    private String subAccountId;
    private BigDecimal amount;
    private ExtendedAttributes extendedAttributes;
    private String flyKey;
    private String flyKeyType;
    private Long attentionPointId;
    private Integer consumptionType;
    private String externalTransactionId;

    private String origin;
    private String rootUniqueTransactionId;
    private String adjustmentType;
}
