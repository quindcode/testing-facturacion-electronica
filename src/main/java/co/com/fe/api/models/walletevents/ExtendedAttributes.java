package co.com.fe.api.models.walletevents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtendedAttributes {
    private int serviceType;
    private int readingType;
    private String module;
    private String description;
    private String appliedAgreement;
    private String origin;
    private List<Tax> taxes;
}
