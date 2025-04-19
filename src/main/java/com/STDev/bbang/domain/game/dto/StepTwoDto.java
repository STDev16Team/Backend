package com.STDev.bbang.domain.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StepTwoDto {

    private Long gameId;
    private int time;
    private int temperature;
    private int flag;
}
