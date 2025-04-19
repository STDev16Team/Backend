package com.STDev.bbang.domain.game.service;

import com.STDev.bbang.domain.game.dto.EndingDto;
import com.STDev.bbang.domain.game.entity.Game;
import com.STDev.bbang.domain.game.repository.GameRepository;
import com.STDev.bbang.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.STDev.bbang.global.exception.ErrorCode.ACCESS_DENIED;

@Service
@RequiredArgsConstructor
@Transactional
public class GameService {

    private final GameRepository gameRepository;

    public ApiResponse<?> gameEnding(Long memberId, Long gameId) {
        Optional<Game> optionalGame = gameRepository.findByGameIdAndMemberId(gameId, memberId);
        if (optionalGame.isEmpty()) {
            return ApiResponse.createError(ACCESS_DENIED);
        }

        Game game = optionalGame.get();
        return ApiResponse.createSuccess(ending(game), "게임 정보입니다.");
    }

    public EndingDto ending(Game game) {
        EndingDto dto = EndingDto.builder()
                .quiz(game.getQuiz())
                .tap(game.getTap())
                .step(game.getStep())
                .time(game.getTime())
                .temperature(Math.round(game.getTemperature() * 100) /100.0)
                .bakeTem(game.getBakeTem())
                .bakeHum(game.getBakeHum())
                .dieFlag(game.isDieFlag())
                .build();

        if(game.getStep() == 5 && !game.isDieFlag()) {
            dto.setBread(BreadName(game.getBreadId()));
            dto.setScore(game.getScore());
        }

        return dto;
    }

    private String BreadName(int breadId) {
        if(breadId == 1) return "치아바타";
        else if(breadId == 2) return "포카치아";
        else if(breadId == 3) return "사워도우";
        return null;
    }
}
