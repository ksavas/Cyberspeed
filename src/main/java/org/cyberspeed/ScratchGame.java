package org.cyberspeed;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.cyberspeed.Entity.GameConfig;
import org.cyberspeed.Entity.GameResult;
import org.cyberspeed.Service.GameJsonUtils;
import org.cyberspeed.Service.MatrixGenerator;
import org.cyberspeed.Service.WinChecker;

import java.io.IOException;
import java.util.logging.Logger;

public class ScratchGame {
    private static final Logger LOGGER = Logger.getLogger(ScratchGame.class.getName());
    private static final String CONFIG_FILE_PATH = "src/main/resources/config.json";

    public static void main(String[] args) throws IOException {
        GameConfig config = GameJsonUtils.getInstance().parseConfig(CONFIG_FILE_PATH);
        LOGGER.info("Configuration has been loaded");

        MatrixGenerator matrixGenerator = new MatrixGenerator(config);
        String[][] matrix = matrixGenerator.generateMatrix();
        LOGGER.info("Random matrix has been generated");

        Integer betAmount = Integer.valueOf(100);

        WinChecker winChecker = new WinChecker(config);
        GameResult gameResult = winChecker.checkWin(matrix,betAmount);

        printResult(gameResult,betAmount);
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