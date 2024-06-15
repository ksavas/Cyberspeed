import org.cyberspeed.Entity.GameConfig;
import org.cyberspeed.Service.GameJsonUtils;
import org.cyberspeed.Service.MatrixGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MatrixGeneratorTest {

    private GameConfig config;
    private MatrixGenerator generator;

    private final String CONFIG = "config.json";

    @BeforeAll
    public void setUp() throws IOException {
        config = GameJsonUtils.getInstance().parseConfigFromResource(CONFIG);
        generator = new MatrixGenerator(config);
    }

    @Test
    public void testGenerateMatrix_NotNull() {
        String[][] matrix = generator.generateMatrix();
        Assertions.assertNotNull(matrix);
    }

    @Test
    public void testGenerateMatrix_Size() {
        String[][] matrix = generator.generateMatrix();
        Assertions.assertNotNull(matrix);
        Assertions.assertEquals(config.getRows(), matrix.length);
        Assertions.assertEquals(config.getColumns(), matrix[0].length);
    }

    @Test
    public void testGenerateMatrix_ContentNotNull() {
        String[][] matrix = generator.generateMatrix();
        Assertions.assertNotNull(matrix);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Assertions.assertNotNull(matrix[i][j]);
            }
        }
    }
}