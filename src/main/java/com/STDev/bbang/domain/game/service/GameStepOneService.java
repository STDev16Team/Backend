package com.STDev.bbang.domain.game.service;

import com.STDev.bbang.domain.game.dto.DataDto;
import com.STDev.bbang.domain.game.entity.Bread;
import com.STDev.bbang.domain.game.entity.Game;
import com.STDev.bbang.domain.game.repository.GameRepository;
import com.STDev.bbang.domain.member.entity.Member;
import com.STDev.bbang.domain.member.repository.MemberRepository;
import com.STDev.bbang.domain.reward.entity.RewardInfo;
import com.STDev.bbang.domain.reward.repository.RewardInfoRepository;
import com.STDev.bbang.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.STDev.bbang.global.exception.ErrorCode.ACCESS_DENIED;

@Service
@RequiredArgsConstructor
@Transactional
public class GameStepOneService {

    private final GameRepository gameRepository;
    private final MemberRepository memberRepository;
    private final RewardInfoRepository rewardInfoRepository;

    public ApiResponse<?> stepOne(Long memberId, List<Integer> select) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if(optionalMember.isEmpty()) return ApiResponse.createError(ACCESS_DENIED);

        // 게임 시작
        Game newGame = Game.builder()
                .memberId(memberId)
                .build();

        gameRepository.save(newGame);

        DataDto dto = new DataDto();

        // 욕심쟁이 도우냥 업적
        if (isAllSelected(select)) {
            dto.setReward(maxSelectReward(memberId));
        }

        // 조심성 도우냥 업적
        if(isMinSelect(select)) {
            dto.setReward(minSelectReward(memberId));
        }

        // 빵 유형 판단
        Bread breadType = detectBreadType(select);

        // 판별 결과에 따라 게임 업데이트
        if (breadType != null) {
            newGame.setBreadId(breadType.getBreadId());
            newGame.setScore(newGame.getScore() + 50);
            newGame.setStep(1);
            dto.setPass(true);
            dto.setGameId(newGame.getGameId());
            return ApiResponse.createSuccess(dto, "1단계 성공");
        } else {
            newGame.die();
            dto.setPass(false);
            return ApiResponse.createSuccess(dto, "반죽\n밀가루, 물, 소금, 그리고 부풀 수 있는건 꼭 들어가야 해요.. 그리고 너무 많이 넣어서도 안돼요!\n다시 한 번 해보면 잘 할 수 있지 않을까요?");
        }
    }

    private Bread detectBreadType(List<Integer> select) {
        Collections.sort(select);

        // 빵 유형
        List<Integer> sourdough = List.of(1, 3, 5, 7);
        List<Integer> ciabatta1 = List.of(1, 5, 6, 7);
        List<Integer> ciabatta2 = List.of(1, 2, 5, 6, 7);
        List<Integer> focaccia = List.of(1, 2, 2, 5, 6, 7);

        if (select.equals(sourdough)) {
            return Bread.SOURDOUGH;
        } else if (select.equals(ciabatta1) || select.equals(ciabatta2)) {
            return Bread.CIABATTA;
        } else if (select.equals(focaccia)) {
            return Bread.FOCACCIA;
        }
        return null;
    }

    private boolean isAllSelected(List<Integer> select) {
        List<Integer> full = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        return new HashSet<>(select).containsAll(full);
    }

    private String maxSelectReward(Long memberId) {
        Optional<RewardInfo> rewardInfo = rewardInfoRepository.findByMemberIdAndRewardId(memberId, 1);

        if(rewardInfo.isPresent()) {
            RewardInfo info = rewardInfo.get();

            if(!info.isSuccessFlag()) {
                info.success();
                return "업적 달성: 욕심쟁이 도우냥";
            }
        }
        return null;
    }

    private boolean isMinSelect(List<Integer> select) {
        return select.size() <= 2;
    }

    private String minSelectReward(Long memberId) {
        Optional<RewardInfo> rewardInfo = rewardInfoRepository.findByMemberIdAndRewardId(memberId, 2);

        if(rewardInfo.isPresent()) {
            RewardInfo info = rewardInfo.get();

            if(!info.isSuccessFlag()) {
                info.success();
                return "업적 달성: 조심성 도우냥";
            }
        }
        return null;
    }
}
