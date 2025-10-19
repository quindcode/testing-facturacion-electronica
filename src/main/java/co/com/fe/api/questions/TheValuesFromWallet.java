package co.com.fe.api.questions;

import co.com.fe.api.models.invoicecommands.InvoiceCommand;
import co.com.fe.api.models.walletevents.WalletEvent;
import co.com.fe.api.utils.AttributeGetter;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

public class TheValuesFromWallet implements Question<Boolean> {
    private InvoiceCommand command;
    private WalletEvent event;

    public TheValuesFromWallet(WalletEvent event){
        this.event = event;
    }

    public static TheValuesFromWallet of(WalletEvent event){
        return new TheValuesFromWallet(event);
    }

    public TheValuesFromWallet areIn(InvoiceCommand command){
        this.command = command;
        return this;
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        String walletId = String.valueOf(AttributeGetter.getValueFromWalletEvent(event, "walletId"));
        String subAccountId = String.valueOf(AttributeGetter.getValueFromInvoiceCommand(command, "subAccountId"));

        Double amount = (Double) AttributeGetter.getValueFromWalletEvent(event, "amount");
        Double totalAmount = (Double) AttributeGetter.getValueFromInvoiceCommand(command, "totalAmount");

        String externalTransactionIdFromEvent = String.valueOf(AttributeGetter.getValueFromWalletEvent(event, "externalTransactionId"));
        String externalTransactionIdFromCommand = String.valueOf(AttributeGetter.getValueFromInvoiceCommand(command, "externalTransactionId"));

        boolean walletMatches = walletId != null && walletId.equals(subAccountId);
        boolean amountMatches = amount != null && amount.equals(totalAmount);
        boolean transactionIdMatches = externalTransactionIdFromEvent != null && externalTransactionIdFromEvent.equals(externalTransactionIdFromCommand);
        System.out.println("walletMatches: " + walletMatches + ", amountMatches: " + amountMatches +
                ", transactionIdMatches: " + transactionIdMatches);

        return walletMatches && amountMatches && transactionIdMatches;
    }
}
