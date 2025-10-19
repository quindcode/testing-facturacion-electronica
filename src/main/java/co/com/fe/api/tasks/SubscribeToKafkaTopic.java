package co.com.fe.api.tasks;

import co.com.fe.api.abilities.ConnectToKafka;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import org.apache.kafka.clients.consumer.Consumer;

import java.time.Duration;
import java.util.Collections;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class SubscribeToKafkaTopic implements Task {
    private final String topic;

    public SubscribeToKafkaTopic(String topic) {
        this.topic = topic;
    }

    public static SubscribeToKafkaTopic named(String topic) {
        return instrumented(SubscribeToKafkaTopic.class, topic);
    }

    @Override
//    @Step("{0} subscribes to the kafka topic '{1}'")
    public <T extends Actor> void performAs(T actor) {
//        // 1. Obtiene la habilidad de conectarse a Kafka
//        ConnectToKafka kafkaAbility = ConnectToKafka.as(actor);
//
//        // 2. Usa la habilidad para obtener el consumidor y realizar la acción
//        kafkaAbility.getConsumer().subscribe(Collections.singletonList(topic));
//
//        // 3. Realiza un poll() para forzar la conexión (pausar la ejecucipon hasta que
//        //  el consumidor esté listo y evitar un race condition)
//        kafkaAbility.getConsumer().poll(Duration.ofMillis(1000));
//
//        System.out.println(String.format("✅ Actor is now actively listening to topic '%s'", topic));
        ConnectToKafka kafkaAbility = ConnectToKafka.as(actor);
        Consumer<String, String> consumer = kafkaAbility.getConsumer();

        consumer.subscribe(Collections.singletonList(topic));

        long start = System.currentTimeMillis();
        while (consumer.assignment().isEmpty() && System.currentTimeMillis() - start < 20_000) {
            consumer.poll(Duration.ofMillis(200));
        }

        if (consumer.assignment().isEmpty()) {
            throw new IllegalStateException("No partition assignment after subscribing to " + topic);
        }

        consumer.seekToEnd(consumer.assignment());

        System.out.println("✅ Actor is now actively listening to topic '" + topic + "'");
        System.out.println("Assigned partitions: " + consumer.assignment());
    }
}
