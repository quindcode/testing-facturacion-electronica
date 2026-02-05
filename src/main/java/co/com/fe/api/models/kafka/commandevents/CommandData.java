package co.com.fe.api.models.kafka.commandevents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommandData {
    // Campos Comunes / Compartidos
    private String uniqueTransactionId;

    // Campos exclusivos de Credit Note
    private String rootUniqueTransactionId;
    private String description;

    // Campos exclusivos de Invoice
    private String rootType;
    private String aggregateRootId;
    private String currencyCode;
    private String appliedDate;

    private ExternalReferences externalReferences;
    private InvoiceRecipient invoiceRecipient;
    private InvoiceTotals invoiceTotals;
    private List<Item> items;
}