package co.com.fe.api.utils;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Properties;

/**
 * Singleton utility to manage Kafka connections (Producer and Consumer)
 * Replaces the per-actor ConnectToKafka ability.
 */
public class KafkaClient {

    private static KafkaClient instance;
    private Producer<String, String> producer;
    private Consumer<String, String> consumer;
    private final Properties kafkaConfig;

    private KafkaClient() {
        this.kafkaConfig = KafkaConfigHelper.getKafkaProperties();
    }

    public static synchronized KafkaClient getInstance() {
        if (instance == null) {
            instance = new KafkaClient();
        }
        return instance;
    }

    /**
     * Gets or creates the shared Kafka Producer.
     */
    public synchronized Producer<String, String> getProducer() {
        if (producer == null) {
            Properties producerProps = new Properties();
            producerProps.putAll(kafkaConfig);
            producerProps.put("key.serializer", StringSerializer.class.getName());
            producerProps.put("value.serializer", StringSerializer.class.getName());
            producerProps.put("acks", "all");
            producerProps.put("linger.ms", 1);

            producer = new KafkaProducer<>(producerProps);

            // Shutdown hook to ensure producer is closed
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (producer != null) {
                    try {
                        producer.close();
                    } catch (Exception e) {
                        System.err.println("Error closing Kafka producer in shutdown hook: " + e.getMessage());
                    }
                }
            }));
        }
        return producer;
    }

    /**
     * Gets or creates the shared Kafka Consumer.
     */
    public synchronized Consumer<String, String> getConsumer() {
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
     * Closes the consumer connection.
     * The producer is handled by the shutdown hook, but can be closed here if needed.
     */
    public synchronized void close() {
        if (consumer != null) {
            try {
                consumer.close(Duration.ofSeconds(3));
            } catch (Exception e) {
                System.err.println("Error closing Kafka consumer: " + e.getMessage());
            } finally {
                consumer = null;
            }
        }
    }
}
