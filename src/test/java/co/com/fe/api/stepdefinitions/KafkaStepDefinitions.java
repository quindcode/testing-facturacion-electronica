package co.com.fe.api.stepdefinitions;

import co.com.fe.api.models.commandevents.CommandEvent;
import co.com.fe.api.models.walletevents.WalletEvent;
import co.com.fe.api.questions.*;
import co.com.fe.api.tasks.PublishMessage;
import co.com.fe.api.tasks.SubscribeToKafkaTopic;
import co.com.fe.api.tasks.WaitForProcessing;
import co.com.fe.api.utils.ConvertStringToModel;
import co.com.fe.api.utils.JsonFileReader;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actors.OnStage;

import java.time.Duration;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.core.Is.is;

public class KafkaStepDefinitions {

    @Given("I am connected to kafka, listening to topic {string}")
    public void iAmConnectedToKafkaListeningTopic(String topic) {
        // Configurar el escenario con un actor que puede conectarse a Kafka
        OnStage.theActorInTheSpotlight().attemptsTo(
                SubscribeToKafkaTopic.named(topic)
        );
        System.out.println("✅ Actor 'KafkaListener' connected to Kafka");
    }

    @When("I listen for a message with key {string} in topic {string}")
    public void iListenForAMessageWithKeyInTopic(String key, String topic) {
        System.out.println("Do nothing, SKIP!");
    }

    @When("I send a message to topic {string} with key {string}")
    public void iSendAMessageWithKeyToTopic(String topic, String key){
        String message = JsonFileReader.readJson("wallet/wallet-message.json");
        if(!message.isEmpty())
        {
            System.out.println(message.substring(0,300));
        }else {
            System.out.println("No message");
        }
        System.out.println("Sending mesage...");

        OnStage.theActorInTheSpotlight().attemptsTo(
                PublishMessage.to(topic).withKey(key).withPayload(message)
        );
        OnStage.theActorInTheSpotlight().remember("event", ConvertStringToModel.convertToWalletEvent(message));
        OnStage.theActorInTheSpotlight().remember("subAccountId", key);
    }

    @When("I wait {int} seconds for the event to process")
    public void iWaitSecondsForTheEventToProcess(int seconds) {
        OnStage.theActorInTheSpotlight().attemptsTo(
                WaitForProcessing.forSeconds(seconds)
        );
    }

    @Then("I should see a message with key {string} in topic {string} with the corresponding information")
    public void iShouldSeeAMessageWithKeyInTopic(String key, String topic) {

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
}
