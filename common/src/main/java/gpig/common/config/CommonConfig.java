package gpig.common.config;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Configuration options that are common across applications
 * 
 */
public abstract class CommonConfig {

    /**
     * Read a config file of a particular type. Example: <br>
     * <code>C2Config c2c = C2Config.getConfig(C2Config.class)</code>
     * 
     * @param clazz
     *            The type of config file to read. Must extend
     *            {@link CommonConfig}
     * @return The config data structure
     * 
     * @throws IOException
     *             If the config file was not readable
     */
    public static <C extends CommonConfig> C getConfig(Class<C> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        C conf = mapper.readValue(new File("config"), clazz);
        return conf;
    }

    // Fields
    public String app;
}
