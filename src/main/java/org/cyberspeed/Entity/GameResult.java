package org.cyberspeed.Entity;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class GameResult {
    private String[][] matrix;
    private int reward;
    private Map<String, List<String>> appliedWinningCombinations;
    private String appliedBonusSymbol;
}