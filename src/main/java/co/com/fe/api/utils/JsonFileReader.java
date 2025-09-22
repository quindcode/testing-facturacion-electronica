package co.com.fe.api.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonFileReader {
    public static String readJson(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get("src/test/resources/fixtures/" + filePath)));
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file: " + filePath, e);
        }
    }
}
