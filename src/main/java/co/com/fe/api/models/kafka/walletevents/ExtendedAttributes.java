package co.com.fe.api.models.kafka.walletevents;

import co.com.fe.api.models.kafka.commandevents.Tax;
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
public class ExtendedAttributes {
    private Integer serviceType;
    private Integer readingType;
    private String module;
    private String description;
    private String appliedAgreement;
    private String origin;
    private List<Tax> taxes;
}