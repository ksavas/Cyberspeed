package org.cyberspeed.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Symbol {
    @JsonProperty("reward_multiplier")
    private Double rewardMultiplier;
    private Double extra;

    private String type;
    private String impact;
}
