package com.STDev.bbang.domain.reward.repository;

import com.STDev.bbang.domain.reward.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardRepository extends JpaRepository<Reward, Long> {

    Reward findByRewardId(int rewardId);
}
