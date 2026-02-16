package co.com.fe.api.models.kafka.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountEvent {
    private Long accountId;
    private Long personId;
    private Long walletId;
    private Long subAccountId;
    private String subAccountState;
    private String administrativeArea;
}
