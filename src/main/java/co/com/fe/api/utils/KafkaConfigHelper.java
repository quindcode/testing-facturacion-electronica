package co.com.fe.api.utils;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Properties;

/**
 * Helper para configurar las propiedades de conexión a Kafka
 * En el futuro, estas propiedades vendrán de un archivo .env
 */
public class KafkaConfigHelper {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String KAFKA_BROKERS = dotenv.get("KAFKA_BROKERS");
    private static final String KAFKA_TLS_ENABLED = dotenv.get("KAFKA_TLS_ENABLED");
    private static final String KAFKA_SASL_ENABLED = dotenv.get("KAFKA_SASL_ENABLED");
    private static final String KAFKA_SASL_MECHANISM = dotenv.get("KAFKA_SASL_MECHANISM");
    private static final String KAFKA_SASL_USERNAME = dotenv.get("KAFKA_SASL_USERNAME");
    private static final String KAFKA_SASL_PASSWORD = dotenv.get("KAFKA_SASL_PASSWORD");

    /**
     * Retorna las propiedades base de Kafka configuradas para el entorno de pruebas
     */
    public static Properties getKafkaProperties() {
        Properties props = new Properties();

        // Configuración básica
        props.put("bootstrap.servers", KAFKA_BROKERS);

        // Configuración de seguridad
        if ("true".equals(KAFKA_TLS_ENABLED)) {
            props.put("security.protocol", "SASL_SSL");
        }

        if ("true".equals(KAFKA_SASL_ENABLED)) {
            props.put("sasl.mechanism", KAFKA_SASL_MECHANISM);
            props.put("sasl.jaas.config",
                    String.format("org.apache.kafka.common.security.scram.ScramLoginModule required " +
                                    "username=\"%s\" password=\"%s\";",
                            KAFKA_SASL_USERNAME, KAFKA_SASL_PASSWORD));
        }

        return props;
    }
}