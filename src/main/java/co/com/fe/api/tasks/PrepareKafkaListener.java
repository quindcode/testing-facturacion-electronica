package co.com.fe.api.tasks;

import co.com.fe.api.abilities.ConnectToKafka;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.List;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class PrepareKafkaListener implements Task {
    private final String topic;

    public PrepareKafkaListener(String topic) {
        this.topic = topic;
    }

    public static PrepareKafkaListener onTopic(String topic) {
        return instrumented(PrepareKafkaListener.class, topic);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        ConnectToKafka ability = ConnectToKafka.as(actor);
        Consumer<String, String> consumer = ability.getConsumer();

        if (!consumer.assignment().isEmpty()) {
            System.out.println("⚠️ El consumidor ya tenía asignaciones: " + consumer.assignment());
            return;
        }

        List<PartitionInfo> partitionInfos = consumer.partitionsFor(topic);
        if (partitionInfos == null) throw new RuntimeException("Topic no encontrado: " + topic);

        List<TopicPartition> partitions = new ArrayList<>();
        for (PartitionInfo pi : partitionInfos) {
            partitions.add(new TopicPartition(pi.topic(), pi.partition()));
        }
        consumer.assign(partitions);

        consumer.seekToEnd(partitions);

        // Confirmar posición (opcional, asegura contacto con broker)
        partitions.forEach(consumer::position);

        System.out.println("🏁 Listener posicionado al final de '" + topic + "'. Listo para capturar nuevos eventos.");
    }
}