package co.com.fe.api.models.walletevents;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tax {
    private String name;
    private int value;
    private int percentage;
}
