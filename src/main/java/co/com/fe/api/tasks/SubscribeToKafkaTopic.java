package co.com.fe.api.tasks;

import co.com.fe.api.abilities.ConnectToKafka;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.OffsetOutOfRangeException;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

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
    public <T extends Actor> void performAs(T actor) {
        // 1. Obtiene la habilidad de conectarse a Kafka
        ConnectToKafka kafkaAbility = ConnectToKafka.as(actor);
        // 2. Usa la habilidad para obtener el consumidor y realizar la acción
         Consumer<String, String> consumer = kafkaAbility.getConsumer();

        consumer.subscribe(Collections.singletonList(topic));

        long start = System.currentTimeMillis();
        // 3. Realiza un poll() para forzar la conexión (pausar la ejecucipon hasta que
//        //  el consumidor esté listo y evitar un race condition)
        while (consumer.assignment().isEmpty() && System.currentTimeMillis() - start < 20_000) {
            consumer.poll(Duration.ofMillis(200));
        }

        if (consumer.assignment().isEmpty()) {
            throw new IllegalStateException("No partition assignment after subscribing to " + topic);
        }

        consumer.seekToEnd(consumer.assignment());

        long deadline = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10);

        for (TopicPartition tp : consumer.assignment()) {
            boolean confirmed = false;
            while (System.currentTimeMillis() < deadline && !confirmed) {
                try {
                    long pos = consumer.position(tp);
                    if (pos >= 0) {
                        confirmed = true;
                    } else {
                        consumer.poll(Duration.ofMillis(100));
                    }
                } catch (OffsetOutOfRangeException e) {
                    consumer.poll(Duration.ofMillis(100)); // refresca metadata/offsets y reintenta
                }
            }
            if (!confirmed) throw new IllegalStateException("No se confirmó position() para " + tp);
        }

        System.out.println("✅ Actor is now actively listening to topic '" + topic + "'");
        System.out.println("Assigned partitions: " + consumer.assignment());
    }
}
