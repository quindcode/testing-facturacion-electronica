package co.com.fe.api.utils;

import co.com.fe.api.models.commandevents.Tax;
import co.com.fe.api.models.walletevents.EventData;
import co.com.fe.api.models.walletevents.ExtendedAttributes;
import co.com.fe.api.models.walletevents.WalletEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class WalletEventBuilder {

    private final WalletEvent event;
    private String uniqueTransactionId;
    private String rootUniqueTransactionId;
    private String transactionId;
    private String externalTransactionId;
    private String externalReferenceId;

    private BigDecimal amount = BigDecimal.valueOf(10000);
    private BigDecimal taxPercentage;

    private String eventTimestamp;

    private String walletId = "40312";
    private String flyKey = "JTM005";

    private WalletEventBuilder() {
        this.event = JsonConverter.fromJsonString(JsonFileReader.readJson("wallet/wallet-message.json"), WalletEvent.class);

        this.uniqueTransactionId = UUID.randomUUID().toString();
        this.transactionId = UUID.randomUUID().toString();
        this.externalTransactionId = UUID.randomUUID().toString();
        this.externalReferenceId = UUID.randomUUID().toString();

        // Genera un monto aleatorio entre 10.000 y 100.000
//        double randomAmount = ThreadLocalRandom.current().nextDouble(10000, 100000);
//        this.amount = BigDecimal.valueOf(randomAmount).setScale(2, RoundingMode.HALF_UP);

        this.taxPercentage = BigDecimal.valueOf(19); // 19% por defecto

        this.eventTimestamp = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
    }

    public static WalletEventBuilder random() {
        return new WalletEventBuilder();
    }

    public WalletEventBuilder withUniqueTransactionId(String id) {
        this.uniqueTransactionId = id;
        return this;
    }

    public WalletEventBuilder withRootUniqueTransactionId(String id) {
        this.rootUniqueTransactionId = id;
        return this;
    }

    public WalletEventBuilder withTransactionId(String id) {
        this.transactionId = id;
        return this;
    }

    public WalletEventBuilder withExternalTransactionId(String id) {
        this.externalTransactionId = id;
        return this;
    }

    public WalletEventBuilder withExternalReferenceId(String id) {
        this.externalReferenceId = id;
        return this;
    }

    public WalletEventBuilder withAmount(double amount) {
        this.amount = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
        return this;
    }

    public WalletEventBuilder withDate(String isoDate) {
        this.eventTimestamp = isoDate;
        return this;
    }

    public WalletEventBuilder withWalletId(String walletId) {
        this.walletId = walletId;
        return this;
    }

    public WalletEvent build() {
        BigDecimal divisor = BigDecimal.valueOf(100).add(taxPercentage);
        BigDecimal taxValue = amount.multiply(taxPercentage).divide(divisor, 2, RoundingMode.HALF_EVEN);

        Tax tax = new Tax();
        tax.setName("Iva " + taxPercentage + "%");
        tax.setPercentage(taxPercentage);
        tax.setValue(taxValue);

        ExtendedAttributes extAttr = new ExtendedAttributes();
        extAttr.setTaxes(Collections.singletonList(tax));

        EventData eventData = this.event.getBody().getEventData();
        eventData.setWalletId(this.walletId);
        eventData.setAmount(this.amount);

        eventData.setUniqueTransactionId(this.uniqueTransactionId);
        eventData.setTransactionId(this.transactionId);
        eventData.setExternalTransactionId(this.externalTransactionId);
        eventData.setExternalReferenceId(this.externalReferenceId);
        if(this.rootUniqueTransactionId != null){
            eventData.setRootUniqueTransactionId(this.rootUniqueTransactionId);
        }

        eventData.setCreatedDate(this.eventTimestamp);

        eventData.setFlyKey(this.flyKey);

        event.getBody().setEventData(eventData);
        event.getBody().setEventTimestamp(this.eventTimestamp);

        return event;
    }
}
