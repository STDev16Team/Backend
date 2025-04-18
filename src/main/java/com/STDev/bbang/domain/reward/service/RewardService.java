package com.STDev.bbang.domain.reward.service;

import com.STDev.bbang.domain.member.entity.Member;
import com.STDev.bbang.domain.member.repository.MemberRepository;
import com.STDev.bbang.domain.reward.dto.RewardDto;
import com.STDev.bbang.domain.reward.dto.RewardInfoDto;
import com.STDev.bbang.domain.reward.entity.Reward;
import com.STDev.bbang.domain.reward.entity.RewardInfo;
import com.STDev.bbang.domain.reward.repository.RewardInfoRepository;
import com.STDev.bbang.domain.reward.repository.RewardRepository;
import com.STDev.bbang.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.STDev.bbang.global.exception.ErrorCode.ACCESS_DENIED;

@Service
@RequiredArgsConstructor
@Transactional
public class RewardService {

    private final RewardRepository rewardRepository;
    private final RewardInfoRepository rewardInfoRepository;
    private final MemberRepository memberRepository;

    public ApiResponse<?> getReward(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if(optionalMember.isEmpty()) return ApiResponse.createError(ACCESS_DENIED);

        // 모든 업적 조회
        List<Reward> allRewards = rewardRepository.findAll();

        // 해당 멤버의 업적 달성 정보 조회
        List<RewardInfo> rewardInfos = rewardInfoRepository.findByMemberId(memberId)
                .orElse(Collections.emptyList());

        // 달성한 업적의 rewardId만 추출
        Set<Integer> achievedRewardIds = rewardInfos.stream()
                .filter(RewardInfo::isSuccessFlag)
                .map(RewardInfo::getRewardId)
                .collect(Collectors.toSet());

        // Reward 처리 - 못한 건 ?? 처리
        List<RewardDto> dtoList = allRewards.stream()
                .map(reward -> {
                    boolean achieved = achievedRewardIds.contains(reward.getRewardId());
                    return new RewardDto(
                            reward.getRewardId(),
                            achieved ? reward.getTitle() : "??",
                            achieved ? reward.getImage() : "??"
                    );
                })
                .collect(Collectors.toList());

        return ApiResponse.createSuccess(dtoList, "업적 조회 성공");
    }

    public ApiResponse<?> getRewardInfo(Long memberId, int rewardId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if(optionalMember.isEmpty()) return ApiResponse.createError(ACCESS_DENIED);

        Optional<RewardInfo> optionalRewardInfo = rewardInfoRepository.findByMemberIdAndRewardId(memberId, rewardId);

        if(optionalRewardInfo.isPresent()) {
            RewardInfo rewardInfo = optionalRewardInfo.get();

            if(rewardInfo.isSuccessFlag()) {
                Reward reward = rewardRepository.findByRewardId(rewardId);
                RewardInfoDto dto = new RewardInfoDto(rewardId, reward.getTitle(), reward.getContent(), reward.getImage());
                return ApiResponse.createSuccess(dto, "업적 상세 조회 성공");
            }
        }
        return ApiResponse.createSuccess(null, "업적 비밀!!");
    }

    public void initializeReward(Long memberId) {
        List<Reward> rewards = rewardRepository.findAll();

        for(Reward reward : rewards) {
            RewardInfo info = RewardInfo.builder()
                    .rewardId(reward.getRewardId())
                    .memberId(memberId)
                    .build();

            rewardInfoRepository.save(info);
        }
    }

    public String updateReward(Long memberId, int rewardId, String reward) {
        Optional<RewardInfo> rewardInfo = rewardInfoRepository.findByMemberIdAndRewardId(memberId, rewardId);

        if(rewardInfo.isPresent()) {
            RewardInfo info = rewardInfo.get();

            if(!info.isSuccessFlag()) {
                info.success();
                return reward;
            }
        }
        return null;
    }
}