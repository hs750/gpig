package gpig.common.config;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gpig.common.util.Log;

/**
 * Configuration options that are common across applications
 * 
 */
public abstract class CommonConfig {

    // Fields
    public String app;

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
    public static <C extends CommonConfig> C getConfig(String filePath, Class<C> clazz) throws IOException {
        File configFile = new File(filePath);

        if (!configFile.exists() || !configFile.isFile()) {
            Log.error("Config file did not exist: %s", filePath);
        }

        Log.info("Using config: %s", configFile.getAbsolutePath());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        C conf = mapper.readValue(configFile, clazz);
        return conf;
    }
}
