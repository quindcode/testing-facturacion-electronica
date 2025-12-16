package co.com.fe.api.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public class JsonConverter {
    private static final ObjectMapper MAPPER = new ObjectMapper()
//            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) // Para que las fechas se vean como ISO-8601 (String) y no números
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private JsonConverter() {}

    /**
     * Convierte un JSON String a cualquier clase Java pasada por parámetro
     * @param json El string JSON
     * @param clazz La clase destino (ej: CommandEvent.class)
     * @return El objeto mapeado
     */
    public static <T> T fromJsonString(String json, Class<T> clazz) {
        if (json == null) return null;
        try {
            return MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializando JSON a la clase " + clazz.getSimpleName(), e);
        }
    }

    /**
     * Convierte un Objeto DTO Java a un String con el JSON de los datos
     * @param object El objeto a convertir
     * @return El string del objeto
     */
    public static String toString(Object object) {
        if (object == null) return null;
        try {
            return MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException("Error convirtiendo el objeto a String", e);
        }
    }
}
