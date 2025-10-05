package co.com.fe.api.models.invoicecommands;

import lombok.Data;

import java.util.List;

@Data
public class InvoiceItem {
    private String description;
    private Double totalAmount;
    private List<ItemTaxes> taxes;
}
