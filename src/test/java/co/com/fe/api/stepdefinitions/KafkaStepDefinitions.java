package co.com.fe.api.stepdefinitions;

import co.com.fe.api.assertions.WalletVsCommandVerifier;
import co.com.fe.api.models.kafka.commandevents.CommandEvent;
import co.com.fe.api.models.kafka.walletevents.WalletEvent;
import co.com.fe.api.questions.*;
import co.com.fe.api.tasks.PrepareKafkaListener;
import co.com.fe.api.tasks.PublishMessage;
import co.com.fe.api.tasks.WaitForProcessing;
import co.com.fe.api.utils.Constants;
import co.com.fe.api.utils.GlobalTestData;
import co.com.fe.api.utils.JsonConverter;
import co.com.fe.api.utils.WalletEventBuilder;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actors.OnStage;

public class KafkaStepDefinitions {

    @Given("I am prepared to listen the topic {string}")
    public void iAmPreparedToListenTheTopic(String topic) {
        OnStage.theActorInTheSpotlight().attemptsTo(
                PrepareKafkaListener.onTopic(topic));
    }

    @Given("I consume a {string} as a {string}")
    public void iConsumeAsA(String consumptionType, String userProfile) {
        WalletEvent event = WalletEventBuilder.random().build();
        switch (userProfile) {
            case "BASIC_USER":
                event = WalletEventBuilder.basedOnUser(GlobalTestData.get("BASIC_USER")).build();
                break;
            case "USER_WITH_INVOICE_DATA":
                event = WalletEventBuilder.basedOnUser(GlobalTestData.get("USER_WITH_INVOICE_DATA")).build();
                break;
            case "THIRD_PARTY_MANDATE":
                event = WalletEventBuilder.basedOnUser(GlobalTestData.get("THIRD_PARTY_MANDATE")).build();
                break;
            case "THIRD_PARTY_NO_MANDATE":
                event = WalletEventBuilder.basedOnUser(GlobalTestData.get("THIRD_PARTY_NO_MANDATE")).build();
                break;
            default:
                break;
        }
        OnStage.theActorInTheSpotlight().remember("event", event);
        OnStage.theActorInTheSpotlight().attemptsTo(
                PublishMessage.to(Constants.TOPIC_WALLET_EVENTS)
                        .withKey(event.getBody().getEventData().getSubAccountId())
                        .withPayload(JsonConverter.toString(event)));
    }

    @When("I send a message to topic {string} with key {string}")
    public void iSendAMessageWithKeyToTopic(String topic, String key) {
        WalletEvent event = WalletEventBuilder.random().build();
        String payloadMessage = JsonConverter.toString(event);

        System.out.println("Sending mesage...");

        OnStage.theActorInTheSpotlight().attemptsTo(
                PublishMessage.to(topic).withKey(key).withPayload(payloadMessage));
        OnStage.theActorInTheSpotlight().remember("event", event);
    }

    @When("I wait {int} seconds for the event to process")
    public void iWaitSecondsForTheEventToProcess(int seconds) {
        OnStage.theActorInTheSpotlight().attemptsTo(
                WaitForProcessing.forSeconds(seconds));
    }

    @Then("I should see a message with uniqueId {string} in topic {string}")
    public void iShouldSeeAMessageWithUniqueIdInTopic(String uniqueId, String topic) {
        CommandEvent receivedCommand = OnStage.theActorInTheSpotlight().asksFor(
                TheMessageWithUniqueId.inTopic(topic, uniqueId)
                        .ofClass(CommandEvent.class));

        WalletEvent originalEvent = OnStage.theActorInTheSpotlight().recall("event");

        WalletVsCommandVerifier.assertMatch(originalEvent, receivedCommand);
    }
}
