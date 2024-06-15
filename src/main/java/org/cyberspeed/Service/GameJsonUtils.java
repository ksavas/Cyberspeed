package org.cyberspeed.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.cyberspeed.Entity.GameConfig;
import org.cyberspeed.Entity.GameResult;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GameJsonUtils {
    private static ObjectMapper mapper;

    private static GameJsonUtils instance;

    private GameJsonUtils(){

    }

    public static GameJsonUtils getInstance(){
        if(instance == null){
            instance = new GameJsonUtils();
            mapper = new ObjectMapper();
        }
        return instance;
    }

    public GameConfig parseConfig(String configPath) throws IOException {
        return this.mapper.readValue(new File(configPath), GameConfig.class);
    }

    public GameConfig parseConfigFromResource(String resourcePath) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }
        return this.mapper.readValue(inputStream, GameConfig.class);
    }

    public String generateJSONResult(GameResult gameResult) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonString = objectMapper.writeValueAsString(gameResult);
        return jsonString;

    }
}