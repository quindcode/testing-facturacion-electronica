package co.com.fe.api.assertions;

import co.com.fe.api.models.commandevents.CommandEvent;
import co.com.fe.api.models.commandevents.Tax;
import co.com.fe.api.models.walletevents.WalletEvent;
import org.assertj.core.api.SoftAssertions;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class WalletVsCommandVerifier {

    private WalletVsCommandVerifier(){}

    public static void assertMatch(WalletEvent event, CommandEvent command) {
        var eventData = event.getBody().getEventData();
        var commandData = command.getBody().getCommandData();

        var commandExtReferences = commandData.getExternalReferences();
        var commandInvoiceTotals = commandData.getInvoiceTotals();

        var commandFirstItem = (commandData.getItems() != null && !commandData.getItems().isEmpty())
                ? commandData.getItems().getFirst()
                : null;

        // Instancia de SoftAssertions
        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(commandData.getUniqueTransactionId())
                .as("Unique Transaction ID")
                .isEqualTo(eventData.getUniqueTransactionId());

        softly.assertThat(commandExtReferences.getExternalTransactionId())
                .as("External Transaction ID")
                .isEqualTo(eventData.getExternalTransactionId());

        softly.assertThat(commandExtReferences.getExternalReferenceId())
                .as("External Reference ID")
                .isEqualTo(eventData.getExternalReferenceId());

        softly.assertThat(commandExtReferences.getFlyKey())
                .as("Fly Key")
                .isEqualTo(eventData.getFlyKey());

        softly.assertThat(commandExtReferences.getFlyKeyType())
                .as("Fly Key Type")
                .isEqualTo(eventData.getFlyKeyType());

        softly.assertThat(commandExtReferences.getAttentionPointId())
                .as("Attention Point ID")
                .isEqualTo(eventData.getAttentionPointId());

        softly.assertThat(commandExtReferences.getSubAccountId())
                .as("SubAccount ID")
                .isEqualTo(eventData.getSubAccountId());


        softly.assertThat(commandInvoiceTotals.getTotalAmount())
                .as("Total Amount")
                .isEqualByComparingTo(eventData.getAmount());

        List<Tax> eventTaxes = (eventData.getExtendedAttributes() != null)
                ? eventData.getExtendedAttributes().getTaxes()
                : Collections.emptyList();

        List<Tax> commandTaxes = (commandFirstItem != null)
                ? commandFirstItem.getTaxes()
                : Collections.emptyList();

        softly.assertThat(commandTaxes)
                .as("Detalle de Impuestos")
                .usingRecursiveComparison()
                .ignoringFields("type")
                .ignoringCollectionOrder()
                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .isEqualTo(eventTaxes);

        softly.assertAll();
    }
}