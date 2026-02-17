package co.com.fe.api.tasks;

import co.com.fe.api.utils.GlobalTestData;
import co.com.fe.api.utils.JsonConverter;
import co.com.fe.api.utils.KafkaEventFactory;
import co.com.fe.api.utils.UserTestContext;
import co.com.fe.api.utils.enums.UserProfile;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class PrepareUserData implements Task {
    private final UserProfile profile;
    private final String name;

    public PrepareUserData(UserProfile profile, String name) {
        this.profile = profile;
        this.name = name;
    }

    public PrepareUserData(UserProfile profile) {
        this.profile = profile;
        this.name = "";
    }

    public static PrepareUserData withProfile(UserProfile profile) {
        return instrumented(PrepareUserData.class, profile);
    }

    public PrepareUserData rememberUserAs(String name) {
        return instrumented(PrepareUserData.class, profile, name);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        UserTestContext ctx = new UserTestContext();

        actor.attemptsTo(
                PublishMessage.to("cert_customers_person_events")
                        .withKey(String.valueOf(ctx.getPersonId()))
                        .withPayload(JsonConverter.toString(KafkaEventFactory.buildPersonEvent(ctx), true)),
                PublishMessage.to("cert_customers_accounts_events")
                        .withKey(String.valueOf(ctx.getSubAccountId()))
                        .withPayload(JsonConverter.toString(KafkaEventFactory.buildAccountEvent(ctx), true)),
                PublishMessage.to("cert_customers_account_contacts_events")
                        .withKey(String.valueOf(ctx.getPersonId()) + '-' + ctx.getDocumentNumber())
                        .withPayload(JsonConverter.toString(KafkaEventFactory.buildContactEvent(ctx), true)));

        if (profile == UserProfile.USER_WITH_INVOICE_DATA) {
            actor.attemptsTo(
                    PublishMessage.to("cert_finance_billing_invoice_generation_data_events")
                            .withKey(String.valueOf(ctx.getSubAccountId()))
                            .withPayload(JsonConverter.toString(KafkaEventFactory.buildBillingDataEvent(ctx), true)));
        } else if (profile == UserProfile.THIRD_PARTY_MANDATE) {
            actor.attemptsTo(
                    PublishMessage.to("cert_finance_billing_mandates_third_party")
                            .withKey(String.valueOf(ctx.getPlate() + "-PLATE"))
                            .withPayload(
                                    JsonConverter.toString(KafkaEventFactory.buildThirdPartyEvent(ctx, true), true)));
        } else if (profile == UserProfile.THIRD_PARTY_NO_MANDATE) {
            actor.attemptsTo(
                    PublishMessage.to("cert_finance_billing_mandates_third_party")
                            .withKey(String.valueOf(ctx.getPlate() + "-PLATE"))
                            .withPayload(
                                    JsonConverter.toString(KafkaEventFactory.buildThirdPartyEvent(ctx, false), true)));
        }

        GlobalTestData.save(name, ctx);
    }
}
