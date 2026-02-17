package co.com.fe.api.questions;

import co.com.fe.api.utils.KafkaClient;
import co.com.fe.api.utils.JsonConverter;
import co.com.fe.api.utils.WaitHelper;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import org.apache.kafka.clients.consumer.Consumer;

import java.time.Duration;

public class TheMessageWithUniqueId<T> implements Question<T> {
    private final String topic;
    private final String uniqueId; // El UUID que generamos
    private final Class<T> targetClass;

    private TheMessageWithUniqueId(String topic, String uniqueId, Class<T> targetClass){
        this.topic = topic;
        this.uniqueId = uniqueId;
        this.targetClass = targetClass;
    }

    public static TheMessageWithUniqueId<String> inTopic(String topic, String uniqueId){
        return new TheMessageWithUniqueId<>(topic, uniqueId, String.class);
    }

    public <R> TheMessageWithUniqueId<R> ofClass(Class<R> clazz) {
        return new TheMessageWithUniqueId<>(this.topic, this.uniqueId, clazz);
    }

    @Override
    public T answeredBy(Actor actor) {
        KafkaClient client = KafkaClient.getInstance();
        Consumer<String, String> consumer = client.getConsumer();

        // Se asume que el actor ya está escuchando el topic

        final String[] foundJson = {null};

        WaitHelper.waitUntil(() -> {
            var records = consumer.poll(Duration.ofMillis(500));
            for (var record : records) {
                // Buscar el ID dentro del String del body
                // Esto funciona para JSONs anidados sin necesidad de deserializar todo
                if (record.value() != null && record.value().contains(uniqueId)) {
                    foundJson[0] = record.value();
                    return true;
                }
            }
            return false;
        }, Duration.ofSeconds(10), Duration.ofMillis(200));

        if (foundJson[0] == null) return null;

        System.out.println(JsonConverter.fromJsonString(foundJson[0], targetClass).toString());

        return JsonConverter.fromJsonString(foundJson[0], targetClass);
    }
}