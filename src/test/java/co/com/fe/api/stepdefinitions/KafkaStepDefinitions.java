package co.com.fe.api.stepdefinitions;

import co.com.fe.api.abilities.ConnectToKafka;
import co.com.fe.api.models.invoicecommands.InvoiceCommand;
import co.com.fe.api.models.walletevents.WalletEvent;
import co.com.fe.api.questions.*;
import co.com.fe.api.tasks.PublishMessage;
import co.com.fe.api.tasks.SubscribeToKafkaTopic;
import co.com.fe.api.tasks.WaitForProcessing;
import co.com.fe.api.utils.ConvertStringToModel;
import co.com.fe.api.utils.JsonFileReader;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actors.OnStage;

import java.time.Duration;
import java.util.Optional;

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
//        OnStage.theActorInTheSpotlight().attemptsTo(
//                PublishMessage.to(topic).withKey(key).withPayload(message).then(
//                        WaitForProcessing.forDuration(Duration.ofSeconds(10))
//                )
//        );
        OnStage.theActorInTheSpotlight().attemptsTo(
                PublishMessage.to(topic).withKey(key).withPayload(message)
        );
        OnStage.theActorInTheSpotlight().remember("event", ConvertStringToModel.convertToWalletEvent(message));
    }

    @Then("the message with key {string} in topic {string} should not be empty")
    public void theMessageWithKeyInTopicShouldNotBeEmpty(String key, String topic) {
        Optional<String> foundMessage = OnStage.theActorInTheSpotlight().asksFor(
                TheMessageWithKey.inTopic(topic, key).withTimeout(Duration.ofSeconds(25))
        );
        InvoiceCommand command = null;
        if (foundMessage.isPresent()) {
            command = OnStage.theActorInTheSpotlight().asksFor(
                    MessageAsInvoiceCommand.from(foundMessage.get())
            );
        }

        // Verificar que el mensaje existe
        assert foundMessage.isPresent() :
                String.format("❌ No message found with key '%s' in topic '%s'", key, topic);

        // Verificar que no está vacío
        String messageValue = foundMessage.get();
        assert messageValue != null && !messageValue.trim().isEmpty() :
                String.format("❌ Message with key '%s' is empty or null", key);

        // Mostrar información del mensaje encontrado
        System.out.println("\n" + "=".repeat(60));
        System.out.println("MENSAJE");
        System.out.println("full name: " + command.getBody().getCommandData().getInvoiceData().getFullName());
        System.out.println("total amount: " + command.getBody().getCommandData().getInvoiceTotals().getTotalAmount());
    }

    @Then("I should see a message with key {string} in topic {string} with the corresponding information")
    public void iShouldSeeAMessageWithKeyInTopicWithTheCorrespondingInformation(String key, String topic) {
        Optional<String> foundMessage = OnStage.theActorInTheSpotlight().asksFor(
                TheMessageWithKey.inTopic(topic, key).withTimeout(Duration.ofSeconds(100))
        );
        InvoiceCommand command = null;
        if(foundMessage.isPresent()) {
            command = ConvertStringToModel.convertToInvoiceCommand(foundMessage.get());
            System.out.println("Command timestamp: " + command.getBody().getCommandTimestamp());
        }else{
            System.out.println("No message found with key " + key);
        }
        WalletEvent event = OnStage.theActorInTheSpotlight().recall("event");

        OnStage.theActorInTheSpotlight().should(
                seeThat(TheValuesFromWallet.of(event).areIn(command))
        );
    }
}
