package co.com.fe.api.questions;

import co.com.fe.api.abilities.ConnectToKafka;
import co.com.fe.api.utils.JsonToClassConverter;
import co.com.fe.api.utils.WaitHelper;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Duration;

/* * Question para buscar un mensaje específico por su key en un topic
 * Retorna Optional con el valor del mensaje si se encuentra
 */
public class TheMessageWithKey<T> implements Question<T> {

    private final String topic;
    private final String key;
    private final Duration timeout;
    private final Class<T> targetClass;

    private TheMessageWithKey(String topic, String key, Duration timeout, Class<T> targetClass) {
        this.topic = topic;
        this.key = key;
        this.timeout = timeout;
        this.targetClass = targetClass;
    }

    public static TheMessageWithKey<String> inTopic(String topic, String key) {
        return new TheMessageWithKey<>(topic, key, Duration.ofSeconds(20), String.class);
    }

    // Método para especificar la clase destino
    public <R> TheMessageWithKey<R> ofClass(Class<R> clazz) {
        return new TheMessageWithKey<>(this.topic, this.key, this.timeout, clazz);
    }

    /**
     * Permite configurar un timeout personalizado
     */
    public TheMessageWithKey<T> withTimeout(Duration timeout) {
        return new TheMessageWithKey<>(this.topic, this.key, timeout, this.targetClass);
    }

    @Override
    public T answeredBy(Actor actor) {
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
                System.out.println(String.format("  📨 Found message - Key: '%s', Value length: %d bytes, Timestamp: %d",
                        record.key(),
                        record.value() != null ? record.value().length() : 0,
                        record.timestamp()));

                // Verificar si la key coincide
                if (key.equals(record.key())) {
                    foundMessage[0] = record.value();
                    System.out.println("  ✅ Message with matching key found!");
                    foundInThisPoll = true;
                    break;
                }
            }

            return foundInThisPoll;
        }, timeout, Duration.ofMillis(300));

        if (!messageFound || foundMessage[0] == null) {
            System.out.println(String.format("  ❌ No message found with key '%s' within %d seconds",
                    key, timeout.getSeconds()));
            return null;
        }

        if (targetClass.equals(String.class)) {
            return (T) foundMessage[0];
        }

        return JsonToClassConverter.fromJson(foundMessage[0], targetClass);
    }
}
