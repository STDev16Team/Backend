package com.STDev.bbang.domain.member.service;

import com.STDev.bbang.domain.member.dto.LoginDto;
import com.STDev.bbang.domain.member.dto.LoginResponseDto;
import com.STDev.bbang.domain.member.entity.Member;
import com.STDev.bbang.domain.member.repository.MemberRepository;
import com.STDev.bbang.domain.reward.service.RewardService;
import com.STDev.bbang.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.STDev.bbang.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final RewardService rewardService;
    private final static String START_REWARD = "업적 달성: 기본 도우냥";

    public ApiResponse<?> loginOrSave(LoginDto dto) {
        if(dto.getPhone() == null || dto.getPassword() == null ||
                dto.getPhone().isEmpty() || dto.getPassword().isEmpty() ||
                dto.getPhone().length() != 11) {
            return ApiResponse.createError(BAD_REQUEST_LOGIN);
        }


        Optional<Member> optionalMember = memberRepository.findByPhone(dto.getPhone());

        if (optionalMember.isEmpty()) {
            Member newMember = Member.builder()
                    .phone(dto.getPhone())
                    .password(dto.getPassword())
                    .build();

            Member savedMember = memberRepository.save(newMember);
            rewardService.initializeReward(newMember.getMemberId());

            LoginResponseDto login = new LoginResponseDto(savedMember.getMemberId(), rewardService.updateReward(savedMember.getMemberId(), 15, START_REWARD));
            return ApiResponse.createSuccess(login, "로그인 성공");
        } else {
            Member member = optionalMember.get();
            if (member.getPassword().equals(dto.getPassword())) {

                LoginResponseDto login = new LoginResponseDto(member.getMemberId(), null);
                return ApiResponse.createSuccess(login, "로그인 성공");
            } else {
                return ApiResponse.createError(FAIL_LOGIN);
            }
        }
    }

    public ApiResponse<?> deleteMember(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if(optionalMember.isEmpty()) return ApiResponse.createError(FAIL_DELETE_MEMBER);
        Member member = optionalMember.get();
        member.delete();

        return ApiResponse.createSuccess(null, "탈퇴 성공");
    }
}