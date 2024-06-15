import org.cyberspeed.Entity.GameConfig;
import org.cyberspeed.Entity.GameResult;
import org.cyberspeed.Service.GameJsonUtils;
import org.cyberspeed.Service.WinChecker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(Lifecycle.PER_CLASS)
public class WinCheckerTest {

    private GameConfig config;
    private WinChecker winChecker;

    @BeforeAll
    public void setUp() throws IOException {
        config = GameJsonUtils.getInstance().parseConfigFromResource("config.json");
        winChecker = new WinChecker(config);
    }

    @Test
    public void testCheckWin_NoWinningCombinations() {
        winChecker = new WinChecker(config);

        String[][] matrix = {
                {"A", "B", "C"},
                {"E", "B", "+1000"},
                {"F", "D", "C"}
        };
        double betAmount = 100;

        GameResult result = winChecker.checkWin(matrix, betAmount);
        assertNotNull(result);
        assertEquals(0, result.getReward());
        assertNotNull(result.getAppliedBonusSymbol());
        assertTrue(result.getAppliedWinningCombinations().isEmpty());
    }
    @Test
    public void testCheckWin_WithBonusSymbol() {
        winChecker = new WinChecker(config);

        String[][] matrix = {
                {"A", "B", "C"},
                {"E", "B", "10x"},
                {"F", "D", "B"}
        };
        double betAmount = 100;

        GameResult result = winChecker.checkWin(matrix, betAmount);
        assertNotNull(result);
        assertTrue(result.getReward() == 3000);
        assertEquals("10x", result.getAppliedBonusSymbol());
    }
    @Test
    public void testCheckWin_OutputExampleofAssignment() throws IOException {
        config = GameJsonUtils.getInstance().parseConfigFromResource("config_output_example_of_assignment.json");
        winChecker = new WinChecker(config);

        String[][] matrix = {
                {"A", "A", "B"},
                {"A", "+1000", "B"},
                {"A", "A", "B"}
        };
        double betAmount = 100;

        GameResult result = winChecker.checkWin(matrix, betAmount);
        assertNotNull(result);
        assertTrue(result.getReward() == 6600);
        assertEquals("+1000", result.getAppliedBonusSymbol());
    }
    @Test
    public void testCheckWin_MoreThan1DifferentSymbols() {
        winChecker = new WinChecker(config);

        String[][] matrix = {
                {"C", "C", "D"},
                {"B", "B", "A"},
                {"A", "B", "C"}
        };
        double betAmount = 100;

        GameResult result = winChecker.checkWin(matrix, betAmount);
        assertNotNull(result);
        assertTrue(result.getReward() == 550);
    }
    @Test
    public void testCheckWin_SameSymbolsHorizontally() {
        winChecker = new WinChecker(config);

        String[][] matrix = {
                {"D", "D", "D"},
                {"B", "B", "C"},
                {"A", "E", "F"}
        };
        double betAmount = 100;

        GameResult result = winChecker.checkWin(matrix, betAmount);
        assertNotNull(result);
        assertTrue(result.getReward() == 400);
    }
}