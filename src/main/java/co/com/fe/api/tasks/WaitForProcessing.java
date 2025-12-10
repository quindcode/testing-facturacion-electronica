package co.com.fe.api.tasks;

import co.com.fe.api.utils.WaitHelper;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.annotations.Subject;

import java.time.Duration;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Task para esperar que el Kafka Stream procese los mensajes
 * Implementa una espera inteligente basada en condiciones
 */
public class WaitForProcessing implements Task {

    private final Duration waitTime;
    private final String description;

    public WaitForProcessing(Duration waitTime, String description) {
        this.waitTime = waitTime;
        this.description = description;
    }

    /**
     * Espera un tiempo específico para el procesamiento
     */
    public static WaitForProcessing forDuration(Duration duration) {
        return instrumented(WaitForProcessing.class, duration, "stream processing");
    }

    public static WaitForProcessing forSeconds(int seconds) {
        return instrumented(WaitForProcessing.class, Duration.ofSeconds(seconds), "stream processing");
    }

    /**
     * Espera por defecto (5 segundos) para procesamiento de streams
     */
    public static WaitForProcessing forStreamProcessing() {
        return instrumented(WaitForProcessing.class, Duration.ofSeconds(5), "stream processing");
    }

    /**
     * Añade descripción personalizada a la espera
     */
    public WaitForProcessing withDescription(String description) {
        return instrumented(WaitForProcessing.class, waitTime, description);
    }

    @Override
    @Subject("waits for {1}")
    public <T extends Actor> void performAs(T actor) {
        System.out.println("Waiting for " + description + " (" + waitTime.getSeconds() + " seconds)...");

        // En lugar de Thread.sleep, usamos una espera que puede ser interrumpida
        WaitHelper.waitUntil(
                () -> false, // Siempre false para esperar el tiempo completo
                waitTime,
                Duration.ofMillis(100)
        );

        System.out.println("Finished waiting for " + description);
    }
}