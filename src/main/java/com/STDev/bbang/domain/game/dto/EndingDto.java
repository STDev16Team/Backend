package com.STDev.bbang.domain.game.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndingDto {

    @Setter
    private String bread;
    private int time;
    private double temperature;
    private int tap;
    private int quiz;
    private int bakeTem;
    private int bakeHum;
    private boolean dieFlag;
    private int step;
    @Setter
    private int score;
}
