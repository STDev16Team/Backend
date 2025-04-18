package com.STDev.bbang.domain.reward.controller;

import com.STDev.bbang.domain.reward.service.RewardService;
import com.STDev.bbang.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/reward")
public class RewardController {

    private final RewardService rewardService;
    @GetMapping
    public ApiResponse<?> getReward(@RequestHeader("X-USER-ID") Long memberId) {
        return rewardService.getReward(memberId);
    }

    @GetMapping("/{gameId}")
    public ApiResponse<?> getReward(@RequestHeader("X-USER-ID") Long memberId, @PathVariable int gameId) {
        return rewardService.getRewardInfo(memberId, gameId);
    }
}
