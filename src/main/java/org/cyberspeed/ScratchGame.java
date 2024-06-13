package org.cyberspeed;

import org.cyberspeed.Entity.GameConfig;
import org.cyberspeed.Service.ConfigParser;
import org.cyberspeed.Service.MatrixGenerator;
import org.cyberspeed.Service.WinChecker;

import java.io.IOException;
import java.util.logging.Logger;

public class ScratchGame {

    private static final Logger LOGGER = Logger.getLogger(ScratchGame.class.getName());
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.json";
    public static void main(String[] args) throws IOException {
        GameConfig config = ConfigParser.parseConfig(CONFIG_FILE_PATH);
        LOGGER.info("Configuration has been loaded");

        MatrixGenerator matrixGenerator = new MatrixGenerator(config);
        String[][] matrix = matrixGenerator.generateMatrix();
        LOGGER.info("Random matrix has been generated");
    }
}