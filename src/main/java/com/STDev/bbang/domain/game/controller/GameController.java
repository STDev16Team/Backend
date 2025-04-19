package com.STDev.bbang.domain.game.controller;

import com.STDev.bbang.domain.game.dto.*;
import com.STDev.bbang.domain.game.service.*;
import com.STDev.bbang.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/game")
public class GameController {

    private final GameStepOneService gameStepOneService;
    private final GameStepTwoService gameStepTwoService;

    @PostMapping("/step1")
    public ApiResponse<?> stepOne(@RequestHeader("X-USER-ID") Long memberId, @RequestBody StepOneDto dto) {
        return gameStepOneService.stepOne(memberId, dto.getSelect());
    }

    @PostMapping("/step2")
    public ApiResponse<?> stepTwo(@RequestHeader("X-USER-ID") Long memberId, @RequestBody StepTwoDto dto) {
        return gameStepTwoService.stepTwo(memberId, dto.getGameId(), dto.getTime(), dto.getTemperature());
    }
}
