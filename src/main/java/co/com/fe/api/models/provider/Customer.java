package co.com.fe.api.models.provider;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {
    private String city;
    private String name;
    private String email;
    private String telephone;
    private String emailStyle;

    @JsonProperty("internalID")
    private String internalId;

    private String addressLine;
    private String countryCode;
    private String countryName;
    private String documentType;
    private String documentNumber;
    private String countrySubentity;

    @JsonProperty("additionalAccountID")
    private String additionalAccountId;
}
