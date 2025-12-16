package co.com.fe.api.abilities;

import co.com.fe.api.utils.KafkaConfigHelper;
import net.serenitybdd.screenplay.Ability;
import net.serenitybdd.screenplay.Actor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Properties;

/**
 * Ability que permite al actor conectarse y interactuar con Kafka
 * Maneja tanto productores como consumidores de manera reutilizable
 */
public class ConnectToKafka implements Ability, AutoCloseable {

    private static Producer<String, String> sharedProducer;
    private Consumer<String, String> consumer;
    private final Properties kafkaConfig;

    public ConnectToKafka() {
        this.kafkaConfig = KafkaConfigHelper.getKafkaProperties();
    }

    /**
     * Método estático para crear la habilidad de conectarse a Kafka
     */
    public static ConnectToKafka withDefaultConfiguration() {
        return new ConnectToKafka();
    }

    /**
     * Obtiene un productor de Kafka. Si no existe, lo crea.
     */
    public synchronized Producer<String, String> getProducer() {
        if (sharedProducer == null) {
            Properties producerProps = new Properties();
            producerProps.putAll(kafkaConfig);
            producerProps.put("key.serializer", StringSerializer.class.getName());
            producerProps.put("value.serializer", StringSerializer.class.getName());
            producerProps.put("acks", "all");
            producerProps.put("linger.ms", 1);

            sharedProducer = new KafkaProducer<>(producerProps);

            // Hook para cerrar el producer al apagar la JVM (fin de toda la suite)
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (sharedProducer != null) sharedProducer.close();
            }));
        }
        return sharedProducer;
    }

    /**
     * Obtiene un consumidor de Kafka. Si no existe, lo crea.
     */
    public Consumer<String, String> getConsumer() {
        if (consumer == null) {
            Properties consumerProps = new Properties();
            consumerProps.putAll(kafkaConfig);
            consumerProps.put("key.deserializer", StringDeserializer.class.getName());
            consumerProps.put("value.deserializer", StringDeserializer.class.getName());
            consumerProps.put("group.id", "serenity-automation-worker");
            consumerProps.put("auto.offset.reset", "latest");
            consumerProps.put("enable.auto.commit", "false");
            consumerProps.put("default.api.timeout.ms", "60000");


            consumer = new KafkaConsumer<>(consumerProps);
        }
        return consumer;
    }

    /**
     * Método requerido por la interfaz Ability
     */
    public static ConnectToKafka as(Actor actor) {
        return actor.abilityTo(ConnectToKafka.class);
    }

    /**
     * Cierra las conexiones de Kafka
     */
    @Override
    public void close() {
        if (consumer != null) {
            consumer.close(Duration.ofSeconds(3));
        }
    }
}