package co.com.fe.api.models.invoicecommands;

import lombok.Data;

@Data
public class InvoiceTotals {
    private Integer itemsQuantity;
    private Double totalAmount;
    private Double totalTaxes;
    private String valueInLetters;
}
