package co.com.fe.api.models.invoicecommands;

import lombok.Data;

@Data
public class Header {
    private String traceId;
    private String hash;
}