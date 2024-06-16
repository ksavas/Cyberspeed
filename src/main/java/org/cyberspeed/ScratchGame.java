package org.cyberspeed;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.cyberspeed.Entity.GameConfig;
import org.cyberspeed.Entity.GameResult;
import org.cyberspeed.Service.GameJsonUtils;
import org.cyberspeed.Service.MatrixGenerator;
import org.cyberspeed.Service.WinChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ScratchGame {
    private static final Logger LOGGER = LogManager.getLogger(ScratchGame.class);
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.json";

    public static void main(String[] args) throws IOException {
        Parameters parameters = readParameters(args);

        GameConfig config = GameJsonUtils.getInstance().parseConfig(parameters.getConfigPath());
        LOGGER.info("Configuration has been loaded");

        MatrixGenerator matrixGenerator = new MatrixGenerator(config);
        String[][] matrix = matrixGenerator.generateMatrix();
        LOGGER.info("Random matrix has been generated");

        WinChecker winChecker = new WinChecker(config);
        GameResult gameResult = winChecker.checkWin(matrix,parameters.getBetAmount());

        printResult(gameResult,parameters.getBetAmount());
    }

    private static Parameters readParameters(String[] args){
        Parameters parameters = new Parameters();
        String configPath = null;
        Integer betAmount = 0;

        if (args.length < 2) {
            LOGGER.info("Usage: java -jar <your-jar-file> --config <config-file-path> --betting-amount <betting-amount>");
            System.exit(1);
            return null;
        }
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--config":
                    configPath = args[++i];
                    break;
                case "--betting-amount":
                    betAmount = Integer.parseInt(args[++i]);
                    break;
            }
        }

        if (configPath == null || betAmount <= 0) {
            System.err.println("Invalid arguments");
            System.exit(1);
        }

        parameters.setConfigPath(configPath);
        parameters.setBetAmount(betAmount);

        return parameters;
    }

    private static void printResult(GameResult gameResult, Integer betAmount) throws JsonProcessingException {
        if(gameResult.getReward() > 0){
            LOGGER.info("user placed a bet with " + betAmount + " betting amount and generated matrix has " +
                    gameResult.getAppliedWinningCombinations().keySet().size() +
                    " same symbols which matches with the winning combination, also " + gameResult.getAppliedBonusSymbol() + " bonus also will be applied.");
        } else {
            LOGGER.info("The game is settled as LOST, so bonus symbol has not been applied because the reward is 0.");
        }

        String strResult = GameJsonUtils.getInstance().generateJSONResult(gameResult);
        LOGGER.info(strResult);
    }
}