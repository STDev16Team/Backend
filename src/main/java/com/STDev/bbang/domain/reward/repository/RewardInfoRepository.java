package com.STDev.bbang.domain.reward.repository;

import com.STDev.bbang.domain.reward.entity.RewardInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RewardInfoRepository extends JpaRepository<RewardInfo, Long> {
    Optional<RewardInfo> findByMemberIdAndRewardId(Long memberId, int rewardId);
    Optional<List<RewardInfo>> findByMemberId(Long memberId);
}
