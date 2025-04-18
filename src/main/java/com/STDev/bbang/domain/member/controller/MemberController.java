package com.STDev.bbang.domain.member.controller;

import com.STDev.bbang.domain.member.dto.LoginDto;
import com.STDev.bbang.domain.member.service.MemberService;
import com.STDev.bbang.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ApiResponse<?> loginOrSave(@RequestBody LoginDto dto) {
        return memberService.loginOrSave(dto);
    }

    @DeleteMapping
    public ApiResponse<?> deleteMember(@RequestHeader("X-USER-ID") Long memberId) {
        return memberService.deleteMember(memberId);
    }
}
