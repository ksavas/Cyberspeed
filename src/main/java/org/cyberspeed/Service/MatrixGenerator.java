package org.cyberspeed.Service;


import org.cyberspeed.Entity.GameConfig;
import org.cyberspeed.Entity.StandardSymbol;

import java.util.Map;
import java.util.Random;
public class MatrixGenerator {
    private GameConfig config;
    private StandardSymbol defaultProbability;

    public MatrixGenerator(GameConfig config) {
        this.config = config;
        this.defaultProbability = StandardSymbol.getDefaultProbability(config);
    }

    public String[][] generateMatrix() {
        int rows = config.getRows();
        int columns = config.getColumns();
        String[][] matrix = new String[rows][columns];

        Random random = new Random();

        generateStandardSymbols(matrix,rows,columns,random);
        placeBonusSymbol(matrix, random);

        return matrix;
    }

    private void generateStandardSymbols(String[][] matrix,int rows, int columns,Random random){
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                StandardSymbol prob = getStandardSymbol(row, column);
                matrix[row][column] = getSymbolFromStandardSymbol(prob.getSymbols(), random);
            }
        }
    }

    private StandardSymbol getStandardSymbol(int row, int column) {
        return config.getProbabilities()
                .getStandardSymbols()
                .stream()
                .filter(prob -> prob.getRow() == row && prob.getColumn() == column)
                .findFirst()
                .orElse(defaultProbability);
    }

    private String getSymbolFromStandardSymbol(Map<String, Integer> symbols, Random random) {
        int totalWeight = symbols
                .values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();

        return getWeightedRandomSymbol(symbols, totalWeight, random);
    }

    private void placeBonusSymbol(String[][] matrix, Random random) {
        Map<String, Integer> bonusSymbols = config
                .getProbabilities()
                .getBonusSymbol()
                .getSymbols();

        int totalWeight = bonusSymbols
                .values()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();

        int row = random.nextInt(matrix.length);
        int column = random.nextInt(matrix[0].length);

        String selectedSymbol = getWeightedRandomSymbol(bonusSymbols, totalWeight, random);
        matrix[row][column] = selectedSymbol;
    }

    private String getWeightedRandomSymbol(Map<String, Integer> symbols, int totalWeight, Random random) {
        int rand = random.nextInt(totalWeight);
        for (Map.Entry<String, Integer> entry : symbols.entrySet()) {
            rand -= entry.getValue();
            if (rand < 0) {
                return entry.getKey();
            }
        }
        return null;
    }
}