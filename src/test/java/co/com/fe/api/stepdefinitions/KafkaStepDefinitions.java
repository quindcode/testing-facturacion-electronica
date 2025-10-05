package co.com.fe.api.stepdefinitions;

import co.com.fe.api.abilities.ConnectToKafka;
import co.com.fe.api.models.invoicecommands.InvoiceCommand;
import co.com.fe.api.questions.MessageAsInvoiceCommand;
import co.com.fe.api.questions.TheMessageWithKey;
import co.com.fe.api.tasks.SubscribeToKafkaTopic;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

import java.time.Duration;
import java.util.Optional;

public class KafkaStepDefinitions {
    // Variable para guardar el mensaje encontrado entre steps
//    private Optional<String> foundMessage;

    // ============================================================================
    // STEPS PARA PRUEBAS DE SOLO LECTURA (LISTEN)
    // ============================================================================

    @Given("I am connected to kafka")
    public void iAmConnectedToKafka() {
        // Configurar el escenario con un actor que puede conectarse a Kafka
        OnStage.setTheStage(new OnlineCast());
        OnStage.theActorCalled("KafkaListener").whoCan(ConnectToKafka.withDefaultConfiguration());
        System.out.println("✅ Actor 'KafkaListener' connected to Kafka");
    }

    @Given("I am connected to kafka, listening to topic {string}")
    public void iAmConnectedToKafkaListeningTopic(String topic) {
        // Configurar el escenario con un actor que puede conectarse a Kafka
//        OnStage.setTheStage(new OnlineCast());
//        OnStage.theActorCalled("KafkaListener").whoCan(ConnectToKafka.withDefaultConfiguration());
        OnStage.theActorInTheSpotlight().attemptsTo(
                SubscribeToKafkaTopic.named(topic)
        );
        System.out.println("✅ Actor 'KafkaListener' connected to Kafka");
    }

    @When("I listen for a message with key {string} in topic {string}")
    public void iListenForAMessageWithKeyInTopic(String key, String topic) {
        // Buscar el mensaje y guardarlo para validaciones posteriores
//        foundMessage = OnStage.theActorInTheSpotlight().asksFor(
//                TheMessageWithKey.inTopic(topic, key).withTimeout(Duration.ofSeconds(25))
//        );
        System.out.println("When step, SKIP!");
    }

    @Then("the message with key {string} in topic {string} should not be empty")
    public void theMessageWithKeyInTopicShouldNotBeEmpty(String key, String topic) {
        // Si no se ejecutó el When antes, buscar el mensaje ahora
//        if (foundMessage == null) {
            Optional<String> foundMessage = OnStage.theActorInTheSpotlight().asksFor(
                    TheMessageWithKey.inTopic(topic, key).withTimeout(Duration.ofSeconds(25))
            );
        InvoiceCommand command = null;
        if(foundMessage.isPresent()) {
                command = OnStage.theActorInTheSpotlight().asksFor(
                        MessageAsInvoiceCommand.from(foundMessage.get())
                );
            }
//        }

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
//        System.out.println("✅ MESSAGE VALIDATION PASSED");
//        System.out.println("=".repeat(60));
//        System.out.println(String.format("Key: %s", key));
//        System.out.println(String.format("Topic: %s", topic));
//        System.out.println(String.format("Length: %d characters", messageValue.length()));
//        System.out.println(String.format("Is Empty: %s", messageValue.trim().isEmpty()));
//        System.out.println("\nMessage Preview (first 600 characters):");
//        System.out.println("-".repeat(60));
//        System.out.println(messageValue.length() > 300 ?
//                messageValue.substring(0, 600) + "..." :
//                messageValue);
//        System.out.println("=".repeat(60) + "\n");

    }
//    @Then("the message with key {string} in topic {string} should not be empty")
//    public void theMessageWithKeyInTopicShouldNotBeEmpty(String key, String topic) {
//        // Si no se ejecutó el When antes, buscar el mensaje ahora
//        if (foundMessage == null) {
//            foundMessage = OnStage.theActorInTheSpotlight().asksFor(
//                    TheMessageWithKey.inTopic(topic, key).withTimeout(Duration.ofSeconds(25))
//            );
//        }
//
//        // Verificar que el mensaje existe
//        assert foundMessage.isPresent() :
//                String.format("❌ No message found with key '%s' in topic '%s'", key, topic);
//
//        // Verificar que no está vacío
//        String messageValue = foundMessage.get();
//        assert messageValue != null && !messageValue.trim().isEmpty() :
//                String.format("❌ Message with key '%s' is empty or null", key);
//
//        // Mostrar información del mensaje encontrado
//        System.out.println("\n" + "=".repeat(60));
//        System.out.println("✅ MESSAGE VALIDATION PASSED");
//        System.out.println("=".repeat(60));
//        System.out.println(String.format("Key: %s", key));
//        System.out.println(String.format("Topic: %s", topic));
//        System.out.println(String.format("Length: %d characters", messageValue.length()));
//        System.out.println(String.format("Is Empty: %s", messageValue.trim().isEmpty()));
//        System.out.println("\nMessage Preview (first 600 characters):");
//        System.out.println("-".repeat(60));
//        System.out.println(messageValue.length() > 300 ?
//                messageValue.substring(0, 600) + "..." :
//                messageValue);
//        System.out.println("=".repeat(60) + "\n");
//
//        // Limpiar para el siguiente escenario
//        foundMessage = null;
//    }
}
