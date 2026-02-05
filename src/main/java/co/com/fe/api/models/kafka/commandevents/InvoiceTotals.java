package co.com.fe.api.models.kafka.commandevents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceTotals {
    private Integer itemsQuantity;
    private BigDecimal subtotalAmount;
    private BigDecimal totalTaxes;
    private BigDecimal totalAmount;
    private String valueInLetters;
}