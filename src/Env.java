import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Env {
    private static final HashMap<String, String> map =
        new HashMap<String, String>();

    static {
        try {
            for (String line : Files.readAllLines(Path.of(".env"))) {
                if (line.isEmpty() || line.startsWith("#")) continue;

                int separator = line.indexOf('=');

                if (separator == -1) continue;

                String key = line.substring(0, separator).trim();
                String value = line.substring(separator + 1).trim();

                if (key.isEmpty() || value.isEmpty()) continue;

                map.put(key, value);
            }
        } catch(Exception e) {
            System.err.println("Error: Failed to read [.env] file");
            System.err.println("\t[.env] file must be in working directory");
        }
    }

    public static String get(String key) {
        return map.get(key);
    }
}
