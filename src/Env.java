import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.HashMap;

/*
 * Class used to interact with the .env environment file.
 */
public class Env {
    // maps variable names to their value as a string
    private static final Map<String, String> map =
        new HashMap<String, String>();

    static {
        try {
            // for each line of the .env file
            for (String line : Files.readAllLines(Path.of(".env"))) {
                // skip empty lines and comments
                if (line.isEmpty() || line.startsWith("#")) continue;

                // find equals sign
                int separator = line.indexOf('=');

                // if no equals sign, skip the line
                if (separator == -1) continue;

                // seperate into key and value
                String key = line.substring(0, separator).trim();
                String value = line.substring(separator + 1).trim();

                // if either key or value are empty, skip the line
                if (key.isEmpty() || value.isEmpty()) continue;

                // add the pair to the map
                map.put(key, value);
            }
        } catch(Exception e) {
            System.err.println("Error: Failed to read [.env] file");
            System.err.println("\t[.env] file must be in working directory");
            e.printStackTrace();
        }
    }

    /**
     * Returns the value associated with a variable name.
     * @param  key the variable name
     * @return the value associated with key
     */
    public static String get(String key) {
        return map.get(key);
    }
}
