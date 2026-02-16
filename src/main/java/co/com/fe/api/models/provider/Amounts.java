package co.com.fe.api.models.provider;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Amounts {
    private String payAmount;
    private String taxAmount;
    private String flexAmount;
    private String extraAmount;
    private String totalAmount;
    private String discountAmount;
}