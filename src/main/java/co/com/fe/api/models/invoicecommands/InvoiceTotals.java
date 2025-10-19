package co.com.fe.api.models.invoicecommands;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceTotals {
    private Integer itemsQuantity;
    private Double totalAmount;
    private Double totalTaxes;
    private String valueInLetters;
}
