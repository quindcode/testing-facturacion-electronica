package co.com.fe.api.utils;

import co.com.fe.api.models.invoicecommands.InvoiceCommand;
import co.com.fe.api.models.walletevents.WalletEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertStringToModel {
    public static InvoiceCommand convertToInvoiceCommand(String jsonMessage){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(jsonMessage, InvoiceCommand.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize message to InvoiceCommand", e);
        }
    }

    public static WalletEvent convertToWalletEvent(String jsonMessage){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return mapper.readValue(jsonMessage, WalletEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize message to InvoiceCommand", e);
        }
    }
}
