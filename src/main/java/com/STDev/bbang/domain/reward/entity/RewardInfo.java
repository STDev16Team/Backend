package com.STDev.bbang.domain.reward.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RewardInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rewardInfoId;

    private int rewardId;

    @Setter
    private Long memberId;

    private boolean successFlag = false;

    public void success() {
        successFlag = true;
    }
}
