package com.STDev.bbang.domain.reward.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RewardInfoDto {

    private int rewardId;
    private String title;
    private String content;
    private String image;
}
