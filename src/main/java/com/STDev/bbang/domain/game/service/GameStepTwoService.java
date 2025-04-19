package com.STDev.bbang.domain.game.service;

import com.STDev.bbang.domain.game.dto.DataDto;
import com.STDev.bbang.domain.game.entity.Game;
import com.STDev.bbang.domain.game.repository.GameRepository;
import com.STDev.bbang.domain.reward.service.RewardService;
import com.STDev.bbang.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.STDev.bbang.global.exception.ErrorCode.ACCESS_DENIED;
import static com.STDev.bbang.global.exception.ErrorCode.BAD_REQUEST_GAME;

@Service
@RequiredArgsConstructor
@Transactional
public class GameStepTwoService {

    private final GameRepository gameRepository;
    private final RewardService rewardService;

    private static final int TEMP = 85;
    private static final int TEMP_RATE = 5;
    private static final int TIME = 21;
    private static final int TIME_RATE = 1;
    private static final String STEP_NAME = "2단계";
    private static final String MASTER_STEP_TWO_REWARD = "업적 달성: 1차 발효 마스터";
    private static final String MAX_TEMP_REWARD = "업적 달성: 화끈한 이스트 지옥탕";
    private static final String MIN_TEMP_REWARD = "업적 달성: 여기가 북극이야.. 남극이야..";

    public ApiResponse<?> stepTwo(Long memberId, Long gameId, int time, int temperature) {
        Optional<Game> optionalGame = gameRepository.findByGameIdAndMemberId(gameId, memberId);
        if (optionalGame.isEmpty()) {
            return ApiResponse.createError(ACCESS_DENIED);
        }

        Game game = optionalGame.get();
        if(game.isDieFlag()) return ApiResponse.createError(BAD_REQUEST_GAME);
//        if(game.getStep() != 1) return ApiResponse.createError(BAD_REQUEST_GAME);

        // 게임 - 시간, 온도 설정
        game.setTime(time);

        StringBuilder info = new StringBuilder("1차 발효");
        boolean isSuccess = true;

        DataDto dto = new DataDto();

        // 시간 범위
        if (time > TIME+TIME_RATE) {
            info.append("\n너무 발효되었어요.. 알콜 냄새가 나요.");
            isSuccess = false;
        }
        else if (time < TIME-TIME_RATE) {
            info.append("\n부풀지도 않은걸요.");
            isSuccess = false;
        }

        // 온도 범위
        if (temperature > TEMP+TEMP_RATE) {
            game.setTemperature(40);
            dto.setReward(rewardService.updateReward(memberId, 4, MAX_TEMP_REWARD));
            info.append("\n효모가 익어버렸어요.");
            isSuccess = false;
        }
        else if (temperature < TEMP-TEMP_RATE) {
            game.setTemperature(10);
            dto.setReward(rewardService.updateReward(memberId, 5, MIN_TEMP_REWARD));
            info.append("\n효모가 얼어버렸어요.");
            isSuccess = false;
        }

        dto.setPass(isSuccess);

        if (isSuccess) {
            game.setTemperature((double) temperature / time + 25);
            int score = 100 - (Math.abs(TIME - time) * 10) - (Math.abs(TEMP - temperature) * 5);
            game.setScore(game.getScore() + score);
            game.setStep(2);
            dto.setGameId(gameId);

            if(temperature == TEMP && time == TIME)
                dto.setReward(rewardService.updateReward(memberId, 3, MASTER_STEP_TWO_REWARD));

            return ApiResponse.createSuccess(dto, STEP_NAME + " 성공");
        } else {
            game.die();
            return ApiResponse.createSuccess(dto, info + "\n다시 한 번 해보면 잘 할 수 있지 않을까요?");
        }
    }
}

