package org.cyberspeed.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WinCombination {
    @JsonProperty("reward_multiplier")
    private Double rewardMultiplier;

    @JsonProperty("covered_areas")
    private List<List<String>> coveredAreas;

    private String when;
    private Integer count;
    private String group;
}

