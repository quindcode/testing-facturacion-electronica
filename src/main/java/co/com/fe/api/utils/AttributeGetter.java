package co.com.fe.api.utils;

import co.com.fe.api.models.invoicecommands.InvoiceCommand;
import co.com.fe.api.models.walletevents.WalletEvent;

public class AttributeGetter {
    private AttributeGetter(){}
    public static Object getValueFromWalletEvent(WalletEvent event, String fieldName){
        switch (fieldName) {
            case "walletId":
                return event.getBody().getEventData().getWalletId();
            case "amount":
                return event.getBody().getEventData().getAmount();
            case "externalTransactionId":
                return event.getBody().getEventData().getExternalTransactionId();
            default:
                return null;
        }
    }

    public static Object getValueFromInvoiceCommand(InvoiceCommand command, String fieldName) {
        switch (fieldName){
            case "subAccountId":
                return command.getBody().getCommandData().getExternalReferences().getSubAccountId();
            case "totalAmount":
                return command.getBody().getCommandData().getInvoiceTotals().getTotalAmount();
            case "externalTransactionId":
                return command.getBody().getCommandData().getExternalReferences().getExternalTransactionId();
            default:
                return null;
        }
    }
}
