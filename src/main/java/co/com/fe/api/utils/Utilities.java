package co.com.fe.api.utils;

import co.com.fe.api.models.commandevents.Tax;
import co.com.fe.api.models.walletevents.EventData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Utilities {
    public static boolean compareTaxLists(List<Tax> eventTaxes, List<Tax> commandTaxes) {

        boolean eventTaxesEmpty = (eventTaxes == null || eventTaxes.isEmpty());
        boolean commandTaxesEmpty = (commandTaxes == null || commandTaxes.isEmpty());

        if (eventTaxesEmpty && commandTaxesEmpty) {
            return true;
        }
        if (eventTaxesEmpty != commandTaxesEmpty) {
            return false;
        }
        if (eventTaxes.size() != commandTaxes.size()) {
            return false;
        }
        for (int i = 0; i < eventTaxes.size(); i++) {
            Tax eventTax = eventTaxes.get(i);
            Tax commandTax = commandTaxes.get(i);

            if (eventTax == null || commandTax == null) {
                return false;
            }

            boolean nameMatches = Objects.equals(eventTax.getName(), commandTax.getName());
            boolean valueMatches = Objects.equals(eventTax.getValue(), commandTax.getValue());
            boolean percentageMatches = Objects.equals(eventTax.getPercentage(), commandTax.getPercentage());

            if (!(nameMatches && valueMatches && percentageMatches)) {
                return false;
            }
        }
        return true;
    }

    public static BigDecimal getSubtotalFromWallet(EventData data){
        List<Tax> taxes = data.getExtendedAttributes().getTaxes();
        BigDecimal totalTaxes = BigDecimal.valueOf(0);
        if(taxes != null){
            for (Tax tax: taxes){
                totalTaxes = totalTaxes.add(tax.getValue());
            }
        }
        return data.getAmount().subtract(totalTaxes);
    }
}
