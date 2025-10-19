package co.com.fe.api.models.invoicecommands;

import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommandData {
    private String resolutionType;
    private String currencyCode;
    private ExternalReferences externalReferences;
    private InvoiceData invoiceData;
    private InvoiceTotals invoiceTotals;
    private List<InvoiceItem> items;
}
