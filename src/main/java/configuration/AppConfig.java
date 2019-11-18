package configuration;

import java.io.IOException;
import java.util.Properties;

/**
 * Reference Class
 * Load Properties File
 */
public class AppConfig {

    public static Properties PROPERTIES = new Properties();

    public static void initializeProperties() throws IOException{
        AppConfig.PROPERTIES.load(AppConfig.class.getClassLoader().getResourceAsStream("application.properties"));
    }

}
