package co.com.fe.api.models.provider;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private String code;
    private String brand;
    private String model;
    private List<Tax> taxes;
    private String total;
    private String agency;
    private String quantity;
    private String standard;
    private String allowance;
    private String unitPrice;
    private String description;
}