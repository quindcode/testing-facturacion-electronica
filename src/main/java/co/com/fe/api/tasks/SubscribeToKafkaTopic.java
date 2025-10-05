package co.com.fe.api.tasks;

import co.com.fe.api.abilities.ConnectToKafka;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

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
        // 1. Obtiene la habilidad de conectarse a Kafka
        ConnectToKafka kafkaAbility = ConnectToKafka.as(actor);

        // 2. Usa la habilidad para obtener el consumidor y realizar la acción
        kafkaAbility.getConsumer().subscribe(Collections.singletonList(topic));
    }
}
