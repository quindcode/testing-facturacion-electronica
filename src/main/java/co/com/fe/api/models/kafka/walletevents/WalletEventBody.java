package co.com.fe.api.models.kafka.walletevents;

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
public class WalletEventBody {
    private String eventName;
    private String eventTimestamp;
    private String domainEntity;
    private EventData eventData;
}