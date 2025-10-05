package co.com.fe.api.models.invoicecommands;

import lombok.Data;

@Data
public class InvoiceCommand {
    private Header header;
    private Body body;
}
