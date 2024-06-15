package org.cyberspeed.Entity;

import lombok.Data;

import java.util.Map;

@Data
public class StandardSymbol {
    private int column;
    private int row;

    private Map<String, Integer> symbols;

    public static StandardSymbol getDefaultProbability(GameConfig config) {
        return config.getProbabilities().getStandardSymbols().stream()
                .filter(prob -> prob.getRow() == 0 && prob.getColumn() == 0)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No default probability found for standard_symbols[0][0]"));
    }
}
