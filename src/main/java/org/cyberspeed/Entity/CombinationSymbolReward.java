package org.cyberspeed.Entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CombinationSymbolReward {
    private String symbol;
    private WinCombination winCombination;
    private String combinationName;


    public CombinationSymbolReward(String symbol,WinCombination winCombination,String combinationName) {
        this.winCombination = winCombination;
        this.symbol = symbol;
        this.combinationName = combinationName;
    }

    public Double getReward(){
        return this.winCombination.getRewardMultiplier();
    }
}