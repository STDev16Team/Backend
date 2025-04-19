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
import static com.STDev.bbang.global.exception.ErrorCode.DIE_GAME;

@Service
@RequiredArgsConstructor
@Transactional
public class GameStepFiveService {

    private final GameRepository gameRepository;
    private final GameService gameService;
    private final RewardService rewardService;

    private static final int TEMP = 225;
    private static final int TEMP_RATE = 2;
    private static final int HUM = 85;
    private static final int HUM_RATE = 2;
    private static final String TEMP_OVER_REWARD = "업적 달성: 직화구이 천재 도우냥";
    private static final String HUM_OVER_REWARD = "업적 달성: 한증막 프로 설치사 도우냥";
    private static final String SOS_REWARD = "업적 달성: 긴급구조 도우냥";
    private static final String DIE_REWARD = "업적 달성: 무지성 버튼냥";

    public ApiResponse<?> stepFive(Long memberId, Long gameId, int downTem, int topTem, int downHum, int topHum, boolean temDieFlag, boolean humDieFlag, boolean timeFlag) {
        Optional<Game> optionalGame = gameRepository.findByGameIdAndMemberId(gameId, memberId);
        if (optionalGame.isEmpty()) {
            return ApiResponse.createError(ACCESS_DENIED);
        }

        Game game = optionalGame.get();
        if (game.isDieFlag()) return ApiResponse.createError(DIE_GAME);
//        if(game.getStep() != 4) return ApiResponse.createError(BAD_REQUEST_GAME);

        int cnt = downTem + topTem + downHum + topHum;
        DataDto dto = new DataDto();

        // 시간 초과
        if(timeFlag) {
            game.setDieFlag(true);
            return ApiResponse.createSuccess(dto, "굽기\n너무 길게 구워졌어요.\n다시 한 번 해보면 잘 할 수 있지 않을까요?");
        }
        else if(temDieFlag) { // 온도 즉사
            game.setDieFlag(true);
            dto.setReward(rewardService.updateReward(memberId, 9, TEMP_OVER_REWARD));
            return ApiResponse.createSuccess(dto, "굽기\n온도가 맞지 않아요.\n다시 한 번 해보면 잘 할 수 있지 않을까요?");
        }
        else if(humDieFlag) { // 습도 즉사
            game.setDieFlag(true);
            dto.setReward(rewardService.updateReward(memberId, 10, DIE_REWARD));
            return ApiResponse.createSuccess(dto, "굽기\n습도가 맞지 않아요.\n다시 한 번 해보면 잘 할 수 있지 않을까요?");
        }
        else if(cnt >= 3) { // 하트 끝
            game.setDieFlag(true);
            dto.setReward(rewardService.updateReward(memberId, 12, HUM_OVER_REWARD));
            return ApiResponse.createSuccess(dto, "굽기\n오븐이 고장났나봐요.\n다시 한 번 해보면 잘 할 수 있지 않을까요?");
        }
        else {
            if(cnt == 2) dto.setReward(rewardService.updateReward(memberId, 11, SOS_REWARD));

            game.setBakeTem(TEMP - (TEMP_RATE * downTem) + (TEMP_RATE * topTem));
            game.setBakeHum(HUM - (HUM_RATE * downHum) + (HUM_RATE * topHum));
            game.setScore(game.getScore() + (400 - cnt * 50));
            game.setStep(5);
            return ApiResponse.createSuccess(gameService.ending(game), "게임 정보입니다.");
        }
    }
}
