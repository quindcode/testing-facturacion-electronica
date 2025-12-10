package co.com.fe.api.models.commandevents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalReferences {
    private String accountId;
    private String subAccountId;
    private String flyKey;
    private String flyKeyType;
    private Long attentionPointId;
    private String transactionId;
    private String externalReferenceId;
    private String externalTransactionId;
}