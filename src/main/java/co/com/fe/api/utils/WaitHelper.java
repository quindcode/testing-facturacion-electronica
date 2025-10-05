package co.com.fe.api.utils;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * Utilidad para implementar esperas inteligentes sin usar Thread.sleep
 * Usa polling con timeouts configurables
 */
public class WaitHelper {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(20);
    private static final Duration DEFAULT_POLLING_INTERVAL = Duration.ofMillis(500);

    /**
     * Espera hasta que una condición se cumpla o se agote el timeout
     * @param condition - función que retorna true cuando se cumple la condición
     * @param timeout - tiempo máximo de espera
     * @param pollingInterval - intervalo entre verificaciones
     * @return true si la condición se cumplió, false si se agotó el timeout
     */
    public static boolean waitUntil(Supplier<Boolean> condition,
                                    Duration timeout,
                                    Duration pollingInterval) {
        long endTime = System.currentTimeMillis() + timeout.toMillis();

        while (System.currentTimeMillis() < endTime) {
            if (condition.get()) {
                return true;
            }

            try {
                Thread.sleep(pollingInterval.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        return false;
    }

    /**
     * Espera con configuración por defecto
     */
    public static boolean waitUntil(Supplier<Boolean> condition) {
        return waitUntil(condition, DEFAULT_TIMEOUT, DEFAULT_POLLING_INTERVAL);
    }
}