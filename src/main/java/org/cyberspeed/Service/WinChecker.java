package org.cyberspeed.Service;

import org.cyberspeed.Entity.*;

import java.util.*;

public class WinChecker {
    private GameConfig config;
    private Map<String, List<String>> appliedWinningCombinations;
    private String appliedBonusSymbol = null;

    private final String SAME_SYMBOLS = "same_symbols";
    private final String LINEAR_SYMBOLS = "linear_symbols";
    private final String BONUS = "bonus";
    private final String MULTIPLY_REWARD = "multiply_reward";
    private final String EXTRA_BONUS = "extra_bonus";
    private final String MISS = "miss";

    public WinChecker(GameConfig config) {
        this.config = config;
        this.appliedWinningCombinations = new HashMap<>();
    }

    public GameResult checkWin(String[][] matrix, double betAmount) {
        Map<String, CombinationSymbolReward> highestSameSymbolRewards = new HashMap<>();
        Map<String, CombinationSymbolReward> highestLinearSymbolRewards = new HashMap<>();
        List<CombinationSymbolReward> combinationSymbolRewards = getCombinationSymbolRewards(matrix);

        for (CombinationSymbolReward reward : combinationSymbolRewards) {
            String symbol = reward.getSymbol();
            String when = reward.getWinCombination().getWhen();

            if (when.equals(SAME_SYMBOLS)) {
                highestSameSymbolRewards.merge(symbol, reward, (existing, newReward) ->
                        newReward.getReward() > existing.getReward() ? newReward : existing);
            } else if (when.equals(LINEAR_SYMBOLS)) {
                highestLinearSymbolRewards.merge(symbol, reward, (existing, newReward) ->
                        newReward.getReward() > existing.getReward() ? newReward : existing);
            }
        }

        double totalReward = calculateTotalReward(highestSameSymbolRewards, highestLinearSymbolRewards, betAmount);
        totalReward = applyBonusSymbols(matrix, totalReward);

        return generateResult(matrix, totalReward);
    }

    private GameResult generateResult(String[][] matrix, double reward) {
        return GameResult.builder()
                .matrix(matrix)
                .reward((int) reward)
                .appliedWinningCombinations(appliedWinningCombinations)
                .appliedBonusSymbol(appliedBonusSymbol)
                .build();
    }

    private List<CombinationSymbolReward> getCombinationSymbolRewards(String[][] matrix) {
        List<CombinationSymbolReward> results = new ArrayList<>();
        for (Map.Entry<String, WinCombination> entry : config.getWinCombinations().entrySet()) {
            String combinationName = entry.getKey();
            WinCombination combination = entry.getValue();

            results.addAll(checkCombination(matrix, combination, combinationName));
        }
        return results;
    }

    private List<CombinationSymbolReward> checkCombination(String[][] matrix, WinCombination combination, String combinationName) {
        List<CombinationSymbolReward> results = new ArrayList<>();

        switch (combination.getWhen()) {
            case SAME_SYMBOLS:
                results.addAll(checkSameSymbols(matrix, combination, combinationName));
                break;
            case LINEAR_SYMBOLS:
                results.addAll(checkLinearSymbols(matrix, combination, combinationName));
                break;
        }

        return results;
    }

    private List<CombinationSymbolReward> checkSameSymbols(String[][] matrix, WinCombination combination, String combinationName) {
        Map<String, Integer> symbolCounts = new HashMap<>();
        for (String[] row : matrix) {
            for (String symbol : row) {
                symbolCounts.put(symbol, symbolCounts.getOrDefault(symbol, 0) + 1);
            }
        }

        List<CombinationSymbolReward> results = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : symbolCounts.entrySet()) {
            if (entry.getValue() >= combination.getCount()) {
                results.add(CombinationSymbolReward.builder()
                        .symbol(entry.getKey())
                        .winCombination(combination)
                        .combinationName(combinationName)
                        .build());
            }
        }

        return results;
    }

    private List<CombinationSymbolReward> checkLinearSymbols(String[][] matrix, WinCombination combination, String combinationName) {
        List<CombinationSymbolReward> results = new ArrayList<>();
        for (List<String> area : combination.getCoveredAreas()) {
            String firstSymbol = null;
            boolean allSame = true;

            for (String cell : area) {
                String[] parts = cell.split(":");
                int row = Integer.parseInt(parts[0]);
                int column = Integer.parseInt(parts[1]);
                String symbol = matrix[row][column];

                if (firstSymbol == null) {
                    firstSymbol = symbol;
                } else if (!symbol.equals(firstSymbol)) {
                    allSame = false;
                    break;
                }
            }

            if (allSame) {
                results.add(new CombinationSymbolReward(firstSymbol, combination,combinationName));
            }
        }

        return results;
    }

    private double calculateTotalReward(Map<String, CombinationSymbolReward> highestSameSymbolRewards,
                                        Map<String, CombinationSymbolReward> highestLinearSymbolRewards, double betAmount) {
        double totalReward = 0.0;

        for (Map.Entry<String, CombinationSymbolReward> entry : highestSameSymbolRewards.entrySet()) {
            String symbol = entry.getKey();
            double sameSymbolReward = entry.getValue().getReward();
            CombinationSymbolReward linearSymbolReward = highestLinearSymbolRewards.get(symbol);
            double linearRewardValue = (linearSymbolReward != null) ? linearSymbolReward.getReward() : 1.0;
            double symbolRewardMultiplier = config.getSymbols().get(symbol).getRewardMultiplier();

            appliedWinningCombinations.computeIfAbsent(symbol, k -> new ArrayList<>()).add(entry.getValue().getCombinationName());
            if (linearSymbolReward != null) {
                appliedWinningCombinations.get(symbol).add(linearSymbolReward.getCombinationName());
            }

            totalReward += betAmount * symbolRewardMultiplier * sameSymbolReward * linearRewardValue;
        }

        return totalReward;
    }

    private double applyBonusSymbols(String[][] matrix, double reward) {
        for (String[] row : matrix) {
            for (String symbol : row) {
                if (config.getSymbols().containsKey(symbol) && config.getSymbols().get(symbol).getType().equals(BONUS)) {
                    Symbol bonusSymbol = config.getSymbols().get(symbol);
                    switch (bonusSymbol.getImpact()) {
                        case MULTIPLY_REWARD:
                            if(reward > 0){
                                reward *= bonusSymbol.getRewardMultiplier();
                            }
                            appliedBonusSymbol = symbol;
                            break;
                        case EXTRA_BONUS:
                            if(reward > 0){
                                reward += bonusSymbol.getExtra();
                            }
                            appliedBonusSymbol = symbol;
                            break;
                        case MISS:
                            appliedBonusSymbol = symbol;
                            break;
                    }
                }
            }
        }
        return reward;
    }
}