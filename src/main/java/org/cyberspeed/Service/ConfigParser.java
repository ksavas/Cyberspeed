package org.cyberspeed.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cyberspeed.Entity.GameConfig;

import java.io.File;
import java.io.IOException;

public class ConfigParser {
    public static GameConfig parseConfig(String configPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(configPath), GameConfig.class);
    }
}
