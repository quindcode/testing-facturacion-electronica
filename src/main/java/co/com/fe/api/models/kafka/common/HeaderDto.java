package co.com.fe.api.models.kafka.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HeaderDto {
    private String correlationId;
    private Integer version;
    private String traceId;
}