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
public class GameStepThreeService {

    private final GameRepository gameRepository;
    private final RewardService rewardService;

    private static final int MAX_TAP = 50;
    private static final int MIN_TAP = 10;
    private static final int IDEAL_TAP = 30;
    private static final String MASTER_TAP_REWARD = "업적 달성: 성형천재 도우냥";
    private static final String MAX_TAP_REWARD = "업적 달성: 찧고 빚고 또 찧고";

    public ApiResponse<?> stepThree(Long memberId, Long gameId, int tap) {
        Optional<Game> optionalGame = gameRepository.findByGameIdAndMemberId(gameId, memberId);
        if (optionalGame.isEmpty()) {
            return ApiResponse.createError(ACCESS_DENIED);
        }

        Game game = optionalGame.get();
        if (game.isDieFlag()) return ApiResponse.createError(DIE_GAME);
//        if(game.getStep() != 2) return ApiResponse.createError(BAD_REQUEST_GAME);

        game.setTap(tap);
        DataDto dto = new DataDto();

        if(tap < MIN_TAP) {
            game.die();
            dto.setPass(false);
            return ApiResponse.createSuccess(dto, "성형\n기포가 제대로 안 잡혔을지도 몰라요… 좀 더 다듬었어야 했는걸요!\n다시 한 번 해보면 잘 할 수 있지 않을까요?");
        }
        else if(tap > MAX_TAP) {
            dto.setReward(rewardService.updateReward(memberId, 7, MAX_TAP_REWARD));

            game.die();
            dto.setPass(false);
            return ApiResponse.createSuccess(dto, "성형\n기포가 다 터지고, 반죽이 지쳐버렸어요.\n다시 한 번 해보면 잘 할 수 있지 않을까요?");
        }
        else {
            if(tap == 30) {
                dto.setReward(rewardService.updateReward(memberId, 6, MASTER_TAP_REWARD));
            }

            int score = 200 - 10 * Math.abs(IDEAL_TAP - tap);
            game.setScore(game.getScore() + score);
            game.setStep(3);

            dto.setPass(true);
            dto.setGameId(gameId);
            return ApiResponse.createSuccess(dto, "3단계 성공");
        }
    }
}

