package co.com.fe.api.questions;

import co.com.fe.api.models.invoicecommands.InvoiceCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class MessageAsInvoiceCommand implements Question<InvoiceCommand> {
    private final String jsonMessage;

    private MessageAsInvoiceCommand(String jsonMessage) {
        this.jsonMessage = jsonMessage;
    }

    @Override
    public InvoiceCommand answeredBy(Actor actor) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(jsonMessage, InvoiceCommand.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize message to InvoiceCommand", e);
        }
    }

    public static Question<InvoiceCommand> from(String jsonMessage) {
        return new MessageAsInvoiceCommand(jsonMessage);
    }
}
