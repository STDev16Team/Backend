package com.STDev.bbang.domain.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StepFiveDto {

    private Long gameId;
    private int downTem;
    private int topTem;
    private int downHum;
    private int topHum;
    private boolean temDieFlag;
    private boolean humDieFlag;
    private boolean timeFlag;
}
