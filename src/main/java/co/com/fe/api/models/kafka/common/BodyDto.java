package co.com.fe.api.models.kafka.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BodyDto<T> {
    private String eventName;
    private String domainEntity;
    private String eventTimestamp;
    private String eventType;
    private T eventData;
}