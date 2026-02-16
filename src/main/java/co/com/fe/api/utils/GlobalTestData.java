package co.com.fe.api.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalTestData {
    private static final Map<String, UserTestContext> contexts = new ConcurrentHashMap<>();

    public static void save(String name, UserTestContext ctx) {
        contexts.put(name, ctx);
    }

    public static UserTestContext get(String name) {
        return contexts.get(name);
    }
}
