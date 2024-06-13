package org.cyberspeed.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class GameConfig {
    private int columns;
    private int rows;

    private Probabilities probabilities;

    @JsonProperty("win_combinations")
    private Map<String, WinCombination> winCombinations;
    private Map<String, Symbol> symbols;
}

