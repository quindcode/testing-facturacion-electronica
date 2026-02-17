package co.com.fe.api.tasks;

import co.com.fe.api.utils.KafkaClient;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.annotations.Subject;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.Future;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Task para publicar mensajes en un topic de Kafka
 * Sigue el patrón Screenplay donde las tareas describen QUÉ hace el actor
 */
public class PublishMessage implements Task {

    private final String topic;
    private final String key;
    private final String message;

    public PublishMessage(String topic, String key, String message) {
        this.topic = topic;
        this.key = key;
        this.message = message;
    }

    /**
     * Método estático para crear la tarea de forma fluida
     */
    public static PublishMessage to(String topic) {
        return instrumented(PublishMessage.class, topic, null, null);
    }

    /**
     * Especifica la clave del mensaje
     */
    public PublishMessage withKey(String key) {
        return instrumented(PublishMessage.class, topic, key, message);
    }

    /**
     * Especifica el contenido del mensaje
     */
    public PublishMessage withPayload(String payload) {
        return instrumented(PublishMessage.class, topic, key, payload);
    }

    /**
     * Ejecuta la tarea de publicar el mensaje
     */
    @Override
    @Subject("publishes message to topic {0}")
    public <T extends Actor> void performAs(T actor) {
        KafkaClient kafkaClient = KafkaClient.getInstance();

        ProducerRecord<String, String> recordToPublish = new ProducerRecord<>(topic, key, message);

        try {
            // Enviar el mensaje y esperar confirmación
            Future<RecordMetadata> future = kafkaClient.getProducer().send(recordToPublish);
            RecordMetadata metadata = future.get(); // Espera síncrona para asegurar entrega
            System.out.println("Published -> topic:" + metadata.topic() + " partition:" + metadata.partition() + " offset:" + metadata.offset() + " timestamp:" + metadata.timestamp());


        } catch (Exception e) {
            throw new RuntimeException("Failed to publish message to topic: " + topic, e);
        }
    }
}