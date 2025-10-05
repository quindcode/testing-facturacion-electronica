package co.com.fe.api.models.invoicecommands;

import lombok.Data;

@Data
public class Body {
    private String commandName;
    private String commandTimestamp;
    private CommandData commandData;
}
