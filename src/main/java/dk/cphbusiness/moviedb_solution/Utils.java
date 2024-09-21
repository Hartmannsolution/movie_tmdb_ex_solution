package dk.cphbusiness.moviedb_solution;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Purpose:
 *
 * @author: Thomas Hartmann
 */
public class Utils {
    public static void main(String[] args) {
        String key = new Utils().getPropertyValue("movie.api.key");
        System.out.println("Key: " + key);
    }

    public String getPropertyValue(String key) {
        Properties props = new Properties();
        try {
            props.load(Main.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace(); // OK to print stack trace here because this is a only going to be used in development
        }
        return props.getProperty(key);
    }

    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.writer(new DefaultPrettyPrinter());
        return objectMapper;
    }
//    public static String getPropertyValue(String propName, String ressourceName)  {
//        // REMEMBER TO BUILD WITH MAVEN FIRST. Read the property file if not deployed (else read system vars instead)
//        // Read from ressources/config.properties or from pom.xml depending on the ressourceName
//        try (InputStream is = Utils.class.getClassLoader().getResourceAsStream(ressourceName)) { //"config.properties" or "properties-from-pom.properties"
//            Properties prop = new Properties();
//            prop.load(is);
//            return prop.getProperty(propName);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }

}
