package co.com.fe.api.questions;

import co.com.fe.api.models.commandevents.CommandEvent;
import co.com.fe.api.models.commandevents.Tax;
import co.com.fe.api.models.walletevents.WalletEvent;
import co.com.fe.api.utils.Utilities;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class TheValuesFromWallet implements Question<Boolean> {
    private CommandEvent command;
    private WalletEvent event;

    public TheValuesFromWallet(WalletEvent event){
        this.event = event;
    }

    public static TheValuesFromWallet of(WalletEvent event){
        return new TheValuesFromWallet(event);
    }

    public TheValuesFromWallet areIn(CommandEvent command){
        this.command = command;
        return this;
    }

    @Override
    public Boolean answeredBy(Actor actor) {
        var eventData = event.getBody().getEventData();
        var eventAttrs = eventData.getExtendedAttributes();

        List<Tax> eventTaxes = (eventAttrs != null) ? eventAttrs.getTaxes() : null;

        var commandData = command.getBody().getCommandData();
        var commandExtRef = commandData.getExternalReferences();
        var commandTotals = commandData.getInvoiceTotals();

        List<co.com.fe.api.models.commandevents.Tax> commandTaxes = null;
        if (commandData.getItems() != null && !commandData.getItems().isEmpty() && commandData.getItems().getFirst() != null) {
            commandTaxes = commandData.getItems().getFirst().getTaxes();
        }

        boolean subAccountIdMatches = Objects.equals(
                eventData.getSubAccountId(),
                commandExtRef.getSubAccountId()
        );
        System.out.println(subAccountIdMatches);

        boolean uniqueIdMatches = Objects.equals(
                eventData.getUniqueTransactionId(),
                commandData.getUniqueTransactionId()
        );
        System.out.println(uniqueIdMatches);

        boolean transactionIdMatches = Objects.equals(
                eventData.getTransactionId(),
                commandExtRef.getTransactionId()
        );
        System.out.println(transactionIdMatches);

        boolean amountMatches = Objects.equals(
                eventData.getAmount(),
                commandTotals.getTotalAmount()
        );
        System.out.println(eventData.getAmount() + " " + commandTotals.getTotalAmount());
        System.out.println(amountMatches);

        boolean subTotalMatches = Objects.equals(
                Utilities.getSubtotalFromWallet(eventData),
                commandData.getItems().getFirst().getSubTotalAmount()
        );
        System.out.println(Utilities.getSubtotalFromWallet(eventData)+ " " + commandData.getItems().getFirst().getSubTotalAmount());
        System.out.println(subTotalMatches);

        boolean taxesMatch = Utilities.compareTaxLists(eventTaxes, commandTaxes);

        System.out.println(eventTaxes.toString());
        System.out.println(commandTaxes.toString());
        System.out.println(taxesMatch);

        return Stream.of(
                subAccountIdMatches,
                uniqueIdMatches,
                transactionIdMatches,
                amountMatches,
                subTotalMatches,
                taxesMatch
        ).allMatch(Boolean::booleanValue);
    }
}
