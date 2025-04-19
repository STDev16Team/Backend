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
public class GameStepFourService {

    private final GameRepository gameRepository;
    private final RewardService rewardService;

    private static final String FAIL_QUIZ_REWARD = "업적 달성: 공부가 필요한 도우냥";

    public ApiResponse<?> stepFour(Long memberId, Long gameId, int quiz) {
        Optional<Game> optionalGame = gameRepository.findByGameIdAndMemberId(gameId, memberId);
        if (optionalGame.isEmpty()) {
            return ApiResponse.createError(ACCESS_DENIED);
        }

        Game game = optionalGame.get();
        if (game.isDieFlag()) return ApiResponse.createError(DIE_GAME);
//        if(game.getStep() != 3) return ApiResponse.createError(BAD_REQUEST_GAME);

        DataDto dto = new DataDto();

        game.setQuiz(quiz);
        game.setScore(game.getScore() + quiz * 100);

        if(quiz == 3) {
            game.setStep(4);

            dto.setPass(true);
            dto.setGameId(gameId);
            return ApiResponse.createSuccess(dto, "4단계 성공");
        }

        dto.setReward(rewardService.updateReward(memberId, 8, FAIL_QUIZ_REWARD));
        game.die();
        dto.setPass(false);
        return ApiResponse.createSuccess(dto, "2차 발효\n퀴즈를 틀렸어요.\n다시 한 번 해보면 잘 할 수 있지 않을까요?");
    }
}
