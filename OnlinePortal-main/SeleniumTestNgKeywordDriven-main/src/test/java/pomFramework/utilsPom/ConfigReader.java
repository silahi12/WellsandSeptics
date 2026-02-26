package pomFramework.utilsPom;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class to read properties from config.properties file.
 */
public class ConfigReader {

    private static Properties properties;
    private final static String PROPERTY_FILE_PATH = "src/test/resources/resourcesPOM/config.properties";

    // Static block to load properties when the class is loaded
    static {
        try {
            FileInputStream fis = new FileInputStream(PROPERTY_FILE_PATH);
            properties = new Properties();
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            System.err.println("Failed to load properties from " + PROPERTY_FILE_PATH);
            e.printStackTrace();
            // You might want to throw a runtime exception here to fail fast if config isn't loaded
            throw new RuntimeException("Could not load config.properties file: " + e.getMessage());
        }
    }

    /**
     * Returns the value of a property from the config.properties file.
     *
     * @param propertyName The name of the property to retrieve.
     * @return The value of the property.
     */
    public static String getProperty(String propertyName) {
        String value = properties.getProperty(propertyName);
        if (value == null) {
            System.err.println("Property '" + propertyName + "' not found in config.properties.");
            // Optionally throw an exception if a required property is missing
            // throw new RuntimeException("Property '" + propertyName + "' not found.");
        }
        return value;
    }
}