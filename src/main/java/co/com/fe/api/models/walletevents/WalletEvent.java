package co.com.fe.api.models.walletevents;

import lombok.Data;

@Data
public class WalletEvent {
    private Header header;
    private Body body;
}
