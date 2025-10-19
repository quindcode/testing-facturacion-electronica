package co.com.fe.api.models.walletevents;

import lombok.Data;

@Data
public class Body {
    private String eventName;
    private String eventTimestamp;
    private String domainEntity;
    private EventData eventData;
}
