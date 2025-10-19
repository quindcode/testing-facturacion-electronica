package co.com.fe.api.models.invoicecommands;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceItem {
    private String description;
    private Double totalAmount;
    private List<ItemTaxes> taxes;
}
