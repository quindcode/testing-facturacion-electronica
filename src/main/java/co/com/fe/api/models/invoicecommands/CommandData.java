package co.com.fe.api.models.invoicecommands;

import lombok.Data;
import java.util.List;

@Data
public class CommandData {
    private String resolutionType;
    private String currencyCode;
    private ExternalReferences externalReferences;
    private InvoiceData invoiceData;
    private InvoiceTotals invoiceTotals;
    private List<InvoiceItem> items;
}
