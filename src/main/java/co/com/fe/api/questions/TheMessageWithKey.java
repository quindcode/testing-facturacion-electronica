package co.com.fe.api.questions;

import co.com.fe.api.abilities.ConnectToKafka;
import co.com.fe.api.utils.WaitHelper;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.annotations.Subject;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Duration;
import java.util.Optional;

/* * Question para buscar un mensaje específico por su key en un topic
 * Retorna Optional con el valor del mensaje si se encuentra
 */
public class TheMessageWithKey implements Question<Optional<String>> {

    private final String topic;
    private final String key;
    private final Duration timeout;

    private TheMessageWithKey(String topic, String key, Duration timeout) {
        this.topic = topic;
        this.key = key;
        this.timeout = timeout;
    }

    /**
     * Crea una pregunta para buscar un mensaje con una key específica
     * Ejemplo: TheMessageWithKey.inTopic("account_events", "2500")
     */
    public static TheMessageWithKey inTopic(String topic, String key) {
        return new TheMessageWithKey(topic, key, Duration.ofSeconds(20));
    }

    /**
     * Permite configurar un timeout personalizado
     */
    public TheMessageWithKey withTimeout(Duration timeout) {
        return new TheMessageWithKey(topic, key, timeout);
    }

    @Override
//    @Subject("the message with key '{1}' in topic '{0}'")
    public Optional<String> answeredBy(Actor actor) {
        ConnectToKafka kafkaAbility = ConnectToKafka.as(actor);

        // Variable para guardar el mensaje encontrado
        final String[] foundMessage = {null};

        System.out.println(String.format("🔍 Searching for message with key '%s' in topic '%s'...", key, topic));

        // Usar polling para buscar el mensaje con la key específica
        boolean messageFound = WaitHelper.waitUntil(() -> {
            ConsumerRecords<String, String> records = kafkaAbility.getConsumer().poll(Duration.ofMillis(200));

            boolean foundInThisPoll = false;
            for (ConsumerRecord<String, String> record : records) {
                // Mostrar cada mensaje encontrado para debugging
                System.out.println(String.format("  📨 Found message - Key: '%s', Value length: %d bytes",
                        record.key(),
                        record.value() != null ? record.value().length() : 0));

                // Verificar si la key coincide
                if (key.equals(record.key())) {
                    foundMessage[0] = record.value();
                    System.out.println("  ✅ Message with matching key found!");
                    foundInThisPoll = true;
                }
            }

            return foundInThisPoll;
        }, timeout, Duration.ofMillis(300));

        if (!messageFound) {
            System.out.println(String.format("  ❌ No message found with key '%s' within %d seconds",
                    key, timeout.getSeconds()));
        }

        return Optional.ofNullable(foundMessage[0]);
    }
}
