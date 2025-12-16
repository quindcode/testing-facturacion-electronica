package co.com.fe.api.stepdefinitions;

import co.com.fe.api.assertions.WalletVsCommandVerifier;
import co.com.fe.api.models.commandevents.CommandEvent;
import co.com.fe.api.models.walletevents.WalletEvent;
import co.com.fe.api.questions.*;
import co.com.fe.api.tasks.PrepareKafkaListener;
import co.com.fe.api.tasks.PublishMessage;
import co.com.fe.api.tasks.SubscribeToKafkaTopic;
import co.com.fe.api.tasks.WaitForProcessing;
import co.com.fe.api.utils.JsonConverter;
import co.com.fe.api.utils.WalletEventBuilder;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actors.OnStage;

import java.time.Duration;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;

public class KafkaStepDefinitions {

    @Given("I am connected to kafka, listening to topic {string}")
    public void iAmConnectedToKafkaListeningTopic(String topic) {
        // Configurar el escenario con un actor que puede conectarse a Kafka
        OnStage.theActorInTheSpotlight().attemptsTo(
                SubscribeToKafkaTopic.named(topic)
        );
        System.out.println("✅ Actor 'KafkaListener' connected to Kafka");
    }

    @When("I send a message to topic {string} with key {string}")
    public void iSendAMessageWithKeyToTopic(String topic, String key){
        WalletEvent event = WalletEventBuilder.random().build();
        String payloadMessage = JsonConverter.toString(event);

        System.out.println("Sending mesage...");

        OnStage.theActorInTheSpotlight().attemptsTo(
                PublishMessage.to(topic).withKey(key).withPayload(payloadMessage)
        );
        OnStage.theActorInTheSpotlight().remember("event", event);
        OnStage.theActorInTheSpotlight().remember("subAccountId", key);
        OnStage.theActorInTheSpotlight().remember("uniqueTransactionId", event.getBody().getEventData().getUniqueTransactionId());
    }

    @When("I wait {int} seconds for the event to process")
    public void iWaitSecondsForTheEventToProcess(int seconds) {
        OnStage.theActorInTheSpotlight().attemptsTo(
                WaitForProcessing.forSeconds(seconds)
        );
    }

    @Then("I should see a message with key {string} in topic {string} with the corresponding information")
    public void iShouldSeeAMessageWithKeyInTopicWithInformation(String key, String topic) {

        CommandEvent receivedCommand = OnStage.theActorInTheSpotlight().asksFor(
                TheMessageWithKey.inTopic(topic, key)
                        .ofClass(CommandEvent.class)
                        .withTimeout(Duration.ofSeconds(100))
        );

        WalletEvent walletEvent = OnStage.theActorInTheSpotlight().recall("event");

        OnStage.theActorInTheSpotlight().should(
                seeThat(TheValuesFromWallet.of(walletEvent).areIn(receivedCommand))
        );
    }

    @Given("I am prepared to listen the topic {string}")
    public void iAmPreparedToListenTheTopic(String topic) {
        OnStage.theActorInTheSpotlight().attemptsTo(
                PrepareKafkaListener.onTopic(topic)
        );
    }

    @Then("I should see a message with uniqueId {string} in topic {string}")
    public void iShouldSeeAMessageWithUniqueIdInTopic(String uniqueId, String topic) {
        CommandEvent receivedCommand = OnStage.theActorInTheSpotlight().asksFor(
                TheMessageWithUniqueId.inTopic(topic, uniqueId)
                            .ofClass(CommandEvent.class)
        );

        WalletEvent originalEvent = OnStage.theActorInTheSpotlight().recall("event");

        WalletVsCommandVerifier.assertMatch(originalEvent, receivedCommand);
    }
}
